package com.project.azul.dto;

import com.project.azul.models.TileCollection;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TileCollectionDto {
    private ArrayList<String> tiles = new ArrayList<>();
    public TileCollectionDto(TileCollection bag){
        tiles.addAll(bag.getTiles()
                .stream().map(t -> t.getColor().toString())
                .collect(Collectors.toList()));
    }

    public ArrayList<String> getTiles(){
        return tiles;
    }

}
