package com.project.azul.models;

import com.project.azul.api.Code;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class PatternLine extends TileCollection{

    private int capacity;
    private int lineNumber;

    public PatternLine(int capacity) {
        this.capacity = capacity;
        this.lineNumber = capacity -1;
    }

    @Override
    public Code addTiles(ArrayList<Tile> tilesToAdd) {
        if (capacity - size() == 0)
            return Code.PATTERN_LINE_IS_FULL;

        var color = getColor();

        if (color != null && color != tilesToAdd.get(0).getColor()){
            return Code.PATTERN_LINE_COLOR_MISMATCH;
        }
        while(capacity - size() != 0 && tilesToAdd.size() > 0){
            var tile = tilesToAdd.remove(0);
            addTile(tile);
        }

        return Code.SUCCESS;
    }

    public Color getColor() {
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

    public PatternLine clone(){
        var result = new PatternLine(capacity);
        result.addTiles(getTiles());
        return result;
    }
}
