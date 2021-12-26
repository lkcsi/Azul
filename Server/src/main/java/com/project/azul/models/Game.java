package com.project.azul.models;

import com.project.azul.creators.TileCollectionCreator;
import com.project.azul.dto.PickForm;
import com.project.azul.models.helpers.PickHelper;
import com.project.azul.models.helpers.PlayerRegister;
import com.project.azul.models.helpers.StateHandler;

import java.util.*;

public class Game {
    private UUID id;

    private List<Player> players;
    private List<Factory> factories;
    private TileCollection bag;
    private TileCollection drop;
    private TileCollection center;

    private PickHelper pickHelper;
    private StateHandler stateHandler;
    private PlayerRegister playerRegister;

    private Player currentPlayer;

    private int numberOfPlayers;
    private State state;

    public Game(UUID id, int numberOfPlayers)
    {
        if(numberOfPlayers < 2 || numberOfPlayers > 4)
            throw new RuntimeException("Number of players should be between 2 and 4");

        this.numberOfPlayers = numberOfPlayers;
        this.id = id;

        state = State.WAITING_FOR_PLAYERS;
        factories = TileCollectionCreator.createFactories(numberOfPlayers);
        bag = TileCollectionCreator.createBag();
        drop = TileCollectionCreator.createDrop();
        center = TileCollectionCreator.createCenter();
        players = new ArrayList<>();

        stateHandler = new StateHandler(this);
        pickHelper = new PickHelper(this);
        playerRegister = new PlayerRegister(this);
    }

    public List<Player> getPlayers() {
        return players;
    }
    public List<Factory> getFactories() {
        return factories;
    }
    public TileCollection getBag() {
        return bag;
    }

    public void pickFromFactory(PickForm pickForm) {
        pickHelper.pickFromFactory(pickForm);
        stateHandler.setGameState();
    }

    public void pickFromCenter(PickForm pickForm){
        pickHelper.pickFromCenter(pickForm);
        stateHandler.setGameState();
    }

    public Player registerPlayer(String playerName){
        return playerRegister.registerPlayer(playerName);
    }

    public State getState(){
        return state;
    }

    public TileCollection getCenter() {
        return center;
    }

    public TileCollection getDrop() {
        return drop;
    }

    public UUID getId() {
        return id;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
