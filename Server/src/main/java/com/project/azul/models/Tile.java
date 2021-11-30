package com.project.azul.models;

public class Tile {
    private final TileColor color;

    public Tile(TileColor color){
        this.color = color;
    }

    public TileColor getColor() {
        return color;
    }
}
