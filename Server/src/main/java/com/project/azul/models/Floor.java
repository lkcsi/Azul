package com.project.azul.models;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Floor extends TileCollection{

    public Floor() {
    }

    public ArrayList<String> getString(){
        return new ArrayList<>(getTiles()
                .stream()
                .limit(7)
                .map(t -> t.getColor().toString())
                .collect(Collectors.toList()));
    }

    public Floor clone(){
        var result = new Floor();
        getTiles().forEach(t -> result.addTile(new Tile(t.getColor())));
        return result;
    }
}
