package com.project.azul.models;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private UUID id;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Factory> factories = new ArrayList<>();
    private TileCollection bag = new TileCollection();
    private TileCollection drop = new TileCollection();
    private Factory center = new Factory(10);
    private int numberOfPlayers;
    private State state;
    private int currentPlayerId;

    public Game(UUID id, int numberOfPlayers)
    {
        this.numberOfPlayers = numberOfPlayers;
        this.id = id;
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

    public Player registerPlayer(String name) {
        if(state != State.WAITING_FOR_PLAYERS)
            throw new RuntimeException("Game is full");

        if(players.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name)))
            throw new RuntimeException("Player name is occupied");

        int id = players.size();
        Player player = new Player(id, name);
        players.add(player);

        if(numberOfPlayers == players.size()){
            state = State.READY;
            currentPlayerId = players.get(0).getId();
        }
        return player;
    }


    private void pick(String playerName, Factory factory, int lineNumber, String colorName, boolean toFloor) {

        if(state != State.READY)
            throw new RuntimeException("Game not started yet");

        var currentPlayer = getCurrentPlayer();
        if(!playerName.equalsIgnoreCase(currentPlayer.getName()))
            throw new RuntimeException("Wrong player order");

        var color = TileColor.valueOf(colorName);
        if(color == null)
            throw new RuntimeException("Color does not exist");

        var pickedFromFactoryTiles = factory.removeTiles(color);
        if(pickedFromFactoryTiles.size() == 0)
            throw new RuntimeException("Color not found in factory");

        if(toFloor){
            currentPlayer.getFloor().addTiles(pickedFromFactoryTiles);
        }
        else{
            currentPlayer.addTilesToLine(pickedFromFactoryTiles, lineNumber);
        }

        drop.addTiles(pickedFromFactoryTiles);
        if(factory != center) {
            center.addTiles(factory.removeTiles());
        }
        checkGameState();
    }

    public void pickFromCenter(String playerName, int lineNumber, String colorName, boolean toFloor) {
        pick(playerName, center, lineNumber, colorName, toFloor);
    }

    public void pickFromFactory(String playerName, int factoryId, int lineNumber, String colorName, boolean toFloor) {
        var factory = factories.stream().filter(f -> f.getId() == factoryId).findFirst();
        if(!factory.isPresent())
            throw new RuntimeException("Factory does not exist");

        pick(playerName, factory.get(), lineNumber, colorName, toFloor);
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
        var game = new Game(id, numberOfPlayers);
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

    public UUID getId() {
        return id;
    }
}
