package com.project.azul.models;

import com.project.azul.api.Code;

import java.util.ArrayList;
import java.util.Collection;

public class Floor extends TileCollection{

    private int capacity = 7;

    public Floor() {
    }
    @Override
    public Code addTiles(ArrayList<Tile> tilesToAdd){
        while(capacity - size() != 0 && tilesToAdd.size() > 0){
            var tile = tilesToAdd.stream().findFirst().get();
            tilesToAdd.remove(tile);
            addTile(tile);
        }
        return Code.SUCCESS;
    }

    public Floor clone(){
        var result = new Floor();
        result.addTiles(getTiles());
        return result;
    }
}
