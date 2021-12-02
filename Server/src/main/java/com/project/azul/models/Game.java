package com.project.azul.models;

import com.project.azul.api.Code;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Factory> factories = new ArrayList<>();
    private TileCollection bag = new TileCollection();
    private TileCollection drop = new TileCollection();
    private Factory center = new Factory(10);
    private int numberOfPlayers;
    private State state;
    private int currentPlayerId;

    public Game(int numberOfPlayers)
    {
        this.numberOfPlayers = numberOfPlayers;
        state = State.WAITING_FOR_PLAYERS;
        createFactories(numberOfPlayers);
        initBag();
        fillFactories();
    }

    private void createFactories(int numberOfPlayers){
        int factoryId = 0;
        for(int i = 0 ; i < numberOfPlayers * 2 + 1; i++){
            factories.add(new Factory(factoryId++));
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public ArrayList<Factory> getFactories() {
        return factories;
    }
    public TileCollection getBag() {return bag;}

    public int addPlayer(Player player) {
       players.add(player);
       return Code.SUCCESS.getCode();
    }

    public boolean isRoundOver() {
        return (center.size() == 0 && !factories.stream().anyMatch(f->f.size() > 0));
    }

    public void fillFactories() {
        for(var factory : factories){
           factory.addTiles(getFourTileFromBag());
        }
    }

    private ArrayList<Tile> getFourTileFromBag()
    {
        var result = new ArrayList<Tile>();
        if(bag.size() < 4)
            refillBag();

        for(int i = 0; i < 4 ; i++){
            int max = bag.size();
            var random = new Random();
            int index = random.nextInt(max);
            var tile = bag.getTiles().get(index);
            result.add(tile);
            bag.removeTile(tile);
        }
        return result;
    }

    public void refillBag() {
        bag.addTiles(drop.getTiles());
        drop.clear();
    }

    public void initBag()
    {
        bag.addTiles(createTiles(TileColor.BLACK));
        bag.addTiles(createTiles(TileColor.RED));
        bag.addTiles(createTiles(TileColor.BLUE));
        bag.addTiles(createTiles(TileColor.WHITE));
        bag.addTiles(createTiles(TileColor.YELLOW));
    }

    private ArrayList<Tile> createTiles(TileColor color){
        var tiles = new ArrayList<Tile>();
        for(int i = 0; i < 20; i++){
            tiles.add(new Tile(color));
        }
        return tiles;
    }

    public Code registerPlayer(String name)
    {
        if(state != State.WAITING_FOR_PLAYERS)
            return Code.GAME_IS_FULL;

        if(players.stream().anyMatch(p -> p.getName() == name))
            return Code.PLAYER_NAME_EXISTS;

        int id = players.size();
        Player player = new Player(id, name);
        addPlayer(player);

        if(numberOfPlayers == players.size()){
            state = State.READY;
            currentPlayerId = players.get(0).getId();
        }
        return Code.SUCCESS;
    }


    private Code pick(String playerName, Factory factory, int lineNumber, String colorName, boolean toFloor){

        var currentPlayer = getCurrentPlayer();
        if(!playerName.equalsIgnoreCase(currentPlayer.getName()))
            return Code.WRONG_PLAYER_ORDER;

        var color = TileColor.valueOf(colorName);
        if(color == null)
            return Code.COLOR_DOES_NOT_EXIST;

        var pickedFromFactoryTiles = factory.removeTiles(color);
        if(pickedFromFactoryTiles.size() == 0)
            return Code.COLOR_NOT_FOUND_IN_FACTORY;

        if(toFloor){
            currentPlayer.getFloor().addTiles(pickedFromFactoryTiles);
        }
        else{
            var result = currentPlayer.addTilesToLine(pickedFromFactoryTiles, lineNumber);
            if(result != Code.SUCCESS)
                return result;
        }

        drop.addTiles(pickedFromFactoryTiles);
        if(factory != center) {
            center.addTiles(factory.removeTiles());
        }
        checkGameState();
        return Code.SUCCESS;
    }

    public Code pickFromCenter(String playerName, int lineNumber, String colorName, boolean toFloor){
        if(state != State.READY)
            return Code.GAME_NOT_STARTED;

        return pick(playerName, center, lineNumber, colorName, toFloor);
    }

    public Code pickFromFactory(String playerName, int factoryId, int lineNumber, String colorName, boolean toFloor)
    {
        if(state != State.READY)
            return Code.GAME_NOT_STARTED;

        var factory = factories.stream().filter(f -> f.getId() == factoryId).findFirst();
        if(!factory.isPresent())
            return Code.FACTORY_DOES_NOT_EXIST;

        return pick(playerName, factory.get(), lineNumber, colorName, toFloor);
    }

    private void checkGameState() {
        if(isRoundOver()){
            calculateScore();
            if(players.stream().anyMatch(p -> p.getWall().isComplete())){
                calculateBonus();
                state = State.FINISHED;
                return;
            }
            players.forEach(p -> drop.addTiles(p.getFloor().removeTiles()));
            currentPlayerId = players.get(0).getId();
            fillFactories();
            return;
        }
        nextPlayer();
    }

    private void calculateBonus() {
        players.stream().forEach(p -> p.countBonus());
    }

    private void calculateScore() {
        players.stream().forEach((p) -> {
            drop.addTiles(p.wallTiling());
            drop.addTiles(p.getFloor().removeTiles());
        });
    }

    private void nextPlayer()
    {
        var currentPlayer = getCurrentPlayer();
        int index = players.indexOf(currentPlayer);
        index = index + 1;

        if(index >= players.size())
            index = 0;

        currentPlayerId = players.get(index).getId();
    }

    public State getState(){
        return state;
    }

    public Player getCurrentPlayer() {
        var player = players.stream().filter(p -> p.getId() == currentPlayerId).findFirst();
        if(player.isPresent())
            return player.get();
        return null;
    }

    public Game clone(){
        var game = new Game(numberOfPlayers);
        game.players = players.stream().map(p -> p.clone()).collect(Collectors.toCollection(ArrayList::new));
        game.currentPlayerId = currentPlayerId;
        game.factories = factories.stream().map(f -> f.clone()).collect(Collectors.toCollection(ArrayList::new));
        game.center = center.clone();
        game.drop = drop.clone();
        game.bag = bag.clone();
        game.state = state;

        return game;
    }

    public Factory getCenter() {
        return center;
    }

    public TileCollection getDrop() {
        return drop;
    }
}
