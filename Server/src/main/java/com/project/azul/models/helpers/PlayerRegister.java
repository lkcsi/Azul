package com.project.azul.models.helpers;

import com.project.azul.models.Game;
import com.project.azul.models.Player;
import com.project.azul.models.State;

public class PlayerRegister {
    private Game game;

    public PlayerRegister(Game game){
        this.game = game;
    }

    public Player registerPlayer(String name) {

        var players = game.getPlayers();
        var state = game.getState();

        if(state != State.WAITING_FOR_PLAYERS)
            throw new RuntimeException("Game is full");

        if(players.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name)))
            throw new RuntimeException("Player name is occupied");

        int id = players.size();
        Player player = new Player(id, name);
        players.add(player);

        if(players.size() == game.getNumberOfPlayers()) {
            game.setState(State.GAME_READY);
            game.setCurrentPlayer(players.get(0));
        }

        return player;
    }
}
