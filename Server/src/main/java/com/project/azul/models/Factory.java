package com.project.azul.models;

public class Factory extends TileCollection{
    private int id;
    public Factory(int id) {
        super(4);
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
