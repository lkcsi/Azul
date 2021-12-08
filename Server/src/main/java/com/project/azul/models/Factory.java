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
        getTiles().forEach(t -> factory.addTile(new Tile(t.getColor())));
        return factory;
    }
}
