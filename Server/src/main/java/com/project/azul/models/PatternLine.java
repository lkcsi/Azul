package com.project.azul.models;


public class PatternLine extends TileCollection{

    private int lineNumber;

    public PatternLine(int capacity) {
        super("PatternLine " + (capacity-1), capacity);
        this.lineNumber = capacity -1;
    }

    public TileColor getColor() {
        var tile = getTiles().stream().filter(t -> t.getColor() != null).findFirst();
        if(tile.isPresent())
            return tile.get().getColor();
        return null;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
