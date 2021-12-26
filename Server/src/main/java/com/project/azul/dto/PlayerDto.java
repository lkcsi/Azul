package com.project.azul.dto;

import com.project.azul.models.PatternLine;
import com.project.azul.models.Player;
import com.project.azul.models.TileCollection;
import com.project.azul.models.Wall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class PlayerDto {
    private List<List<String>> wall;
    private List<List<String>> patternLines;
    private List<String> floor;
    private int score;
    private int id;
    private String name;

    public PlayerDto(Player player){
       id = player.getId();
       name = player.getName();
       score = player.getScore();
       wall = getString(player.getWall());
       patternLines = getString(player.getPatternLines());
       floor = getString(player.getFloor());
    }

    public List<List<String>> getWall() {
        return wall;
    }

    public List<List<String>> getPatternLines() {
        return patternLines;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getFloor() {
        return floor;
    }
    private List<List<String>> getString(List<PatternLine> patternLines){
        return patternLines.stream().map(line -> getString(line))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<List<String>> getString(Wall wall){
        return wall.getTiles().stream().map((line) -> {
            ArrayList<String> tiles = new ArrayList<String>(Collections.nCopies(5,"empty"));
            for(var tile : line){
                if(tile.isPlaced()){
                    tiles.set(line.indexOf(tile), tile.getColor().toString());
                }
            }
            return tiles;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    private List<String> getString(TileCollection tileCollection){
        List<String> tiles = new ArrayList<String>(Collections.nCopies(tileCollection.capacity(), "empty"));
        int index = tileCollection.capacity() - 1;
        for(var tile : tileCollection.getTiles()) {
            tiles.set(index--, tile.getColor().toString());
        }
        return tiles;
    }
}
