package com.project.azul.models;

public class Factory extends TileCollection{
    private int id;

    public Factory(int id)
    {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Factory clone(){
        var factory = new Factory(id);
        factory.addTiles(getTiles());
        return factory;
    }
}
