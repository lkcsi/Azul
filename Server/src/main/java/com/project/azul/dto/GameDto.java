package com.project.azul.dto;

import com.project.azul.models.Game;
import com.project.azul.models.State;

import java.util.UUID;

public class GameDto {
    private final UUID id;
    private final int players;
    private final State state;
    private String currentPlayer;

    public GameDto(Game game){
        this.id = game.getId();
        this.players = game.getPlayers().size();
        this.state = game.getState();
        this.currentPlayer = "N/A";
        var player = game.getCurrentPlayer();
        if(player != null)
            this.currentPlayer = player.getName();
    }

    public int getPlayers() {
        return players;
    }

    public State getState() {
        return state;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public UUID getId() { return id; }
}
