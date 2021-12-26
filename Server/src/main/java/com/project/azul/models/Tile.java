package com.project.azul.models;

public class Tile {
    private final TileColor color;
    private boolean placed;

    public Tile(TileColor color){
        this.color = color;
        this.placed = false;
    }

    public Tile(TileColor color, boolean placed){
        this.color = color;
        this.placed = placed;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(Boolean placed){
        this.placed = placed;
    }

    public TileColor getColor() {
        return color;
    }
}
