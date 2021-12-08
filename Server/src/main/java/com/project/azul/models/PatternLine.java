package com.project.azul.models;

import java.util.ArrayList;

public class PatternLine extends TileCollection{

    private int capacity;
    private int lineNumber;

    public PatternLine(int capacity) {
        this.capacity = capacity;
        this.lineNumber = capacity -1;
    }

    public void addTiles(ArrayList<Tile> tilesToAdd) {
        if (isFull())
            throw new RuntimeException("Pattern line is full");

        var color = getColor();
        if (color != null && color != tilesToAdd.get(0).getColor()){
            throw new RuntimeException("Pattern line color mismatch");
        }
        while(!isFull() && tilesToAdd.size() > 0){
            var tile = tilesToAdd.remove(0);
            addTile(tile);
        }
    }

    public TileColor getColor() {
        var tile = getTiles().stream().filter(t -> t.getColor() != null).findFirst();
        if(tile.isPresent())
            return tile.get().getColor();
        return null;
    }

    public int capacity() {
        return capacity;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public boolean isFull(){
        return capacity == size();
    }

    public PatternLine clone(){
        var result = new PatternLine(capacity);
        getTiles().forEach(t -> result.addTile(new Tile(getColor())));
        return result;
    }
}
