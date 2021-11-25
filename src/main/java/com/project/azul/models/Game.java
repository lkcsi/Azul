package com.project.azul.models;

import com.project.azul.api.Code;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Factory> factories = new ArrayList<Factory>();
    private TileCollection bag = new TileCollection();
    private TileCollection drop = new TileCollection();
    private Factory center = new Factory(0);
    private int numberOfPlayers;
    private State state;
    private Player currentPlayer;

    public Game(int numberOfPlayers)
    {
        this.numberOfPlayers = numberOfPlayers;
        state = State.WAITING_FOR_PLAYERS;
        createFactories(numberOfPlayers);
        createTiles();
        fillFactories();
    }

    private void createFactories(int numberOfPlayers){
        int factoryId = 1;
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

    private void createTiles()
    {
        bag.addTiles(new ArrayList<Tile>(Collections.nCopies(20, new Tile(Color.BLACK))));
        bag.addTiles(new ArrayList<Tile>(Collections.nCopies(20, new Tile(Color.RED))));
        bag.addTiles(new ArrayList<Tile>(Collections.nCopies(20, new Tile(Color.BLUE))));
        bag.addTiles(new ArrayList<Tile>(Collections.nCopies(20, new Tile(Color.CYAN))));
        bag.addTiles(new ArrayList<Tile>(Collections.nCopies(20, new Tile(Color.YELLOW))));
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
            currentPlayer = players.get(0);
        }
        return Code.SUCCESS;
    }

    private Code pick(String playerName, Factory factory, int lineNumber, String colorName){

        if(!playerName.equalsIgnoreCase(currentPlayer.getName()))
            return Code.WRONG_PLAYER_ORDER;

        var color = Arrays.stream(Color.values())
                .filter(c -> c.toString().equalsIgnoreCase(colorName)).findFirst();
        if(!color.isPresent())
            return Code.COLOR_DOES_NOT_EXIST;

        var pickedFromFactoryTiles = factory.removeTiles(color.get());
        if(pickedFromFactoryTiles.size() == 0)
            return Code.COLOR_NOT_FOUND_IN_FACTORY;

        var result = currentPlayer.addTilesToLine(pickedFromFactoryTiles, lineNumber);
        if(result != Code.SUCCESS)
            return result;

        bag.addTiles(pickedFromFactoryTiles);

        if(factory != center) {
            center.addTiles(factory.removeTiles());
        }

        checkGameState();
        return Code.SUCCESS;
    }

    public Code pickFromCenter(String playerName, int lineNumber, String colorName){
        if(state != State.READY)
            return Code.GAME_NOT_STARTED;

        return pick(playerName, center, lineNumber, colorName);
    }

    public Code pickFromFactory(String playerName, int factoryId, int lineNumber, String colorName)
    {
        if(state != State.READY)
            return Code.GAME_NOT_STARTED;

        var factory = factories.stream().filter(f -> f.getId() == factoryId).findFirst();
        if(!factory.isPresent())
            return Code.FACTORY_DOES_NOT_EXIST;

        return pick(playerName, factory.get(), lineNumber, colorName);
    }

    private void checkGameState() {
        if(isRoundOver()){
            calculateScore();
            if(players.stream().anyMatch(p -> p.getWall().isComplete())){
                calculateBonus();
                state = State.FINISHED;
                return;
            }
            fillFactories();
            currentPlayer = players.get(0);
            return;
        }
        nextPlayer();
    }

    private void calculateBonus() {
        players.stream().forEach(p -> p.countBonus());
    }

    private void calculateScore() {
        players.stream().forEach(p -> p.wallTiling());
    }

    private void nextPlayer()
    {
        int index = players.indexOf(currentPlayer);
        index = index + 1;

        if(index >= players.size())
            index = 0;

        currentPlayer = players.get(index);
    }

    public State getState(){
        return state;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Game clone(){
        var game = new Game(numberOfPlayers);
        game.players = players.stream().map(p -> p.clone()).collect(Collectors.toCollection(ArrayList::new));
        game.currentPlayer = currentPlayer;
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
}
