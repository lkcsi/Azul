package com.azul.client.dtos;

import java.util.UUID;

public class GameDto {
    private UUID id;
    private int players;
    private String state;
    private String currentPlayer;

    public int getPlayers() { return players; }
    public String getState() { return state; }
    public String getCurrentPlayer() { return currentPlayer; }
    public UUID getId() { return id; }
}
