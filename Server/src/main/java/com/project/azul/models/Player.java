package com.project.azul.models;

import com.project.azul.creators.TileCollectionCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player {

    private Wall wall = new Wall();
    private TileCollection floor;
    private List<PatternLine> patternLines;

    private int score = 0;
    private final int id;
    private final String name;


    public Player(int id, String name) {
       this.id = id;
       this.name = name;

       floor = TileCollectionCreator.createFloor();
       patternLines = TileCollectionCreator.createPatternLines();
    }

    public void addTilesToLine(List<Tile> tiles, int lineNumber) {
        if(lineNumber < 0 || lineNumber > 4)
            throw new RuntimeException("Patter line does not exist with index: " + lineNumber);
        addTilesToLine(tiles, patternLines.get(lineNumber));
    }

    public List<Tile> addTilesToLine(List<Tile> tiles, PatternLine patternLine) {
        var patternLineColor = patternLine.getColor();
        var firstTile = tiles.get(0);

        if (patternLine.isFull())
            throw new RuntimeException("Pattern line is full");

        if (patternLineColor != null && patternLineColor != firstTile.getColor())
            throw new RuntimeException("Pattern line color mismatch");

        if (wall.isPlaced(patternLine, firstTile)){
            throw new RuntimeException("Color is filled in wall");
        }

        var addedTiles = new ArrayList<>();
        addedTiles.addAll(patternLine.addTiles(tiles));
        addedTiles.addAll(floor.addTiles(tiles));

        return tiles;
    }

    public List<Tile> addTilesToFloor(List<Tile> tiles){
        return floor.addTiles(tiles);
    }

    public void discount(){
        addScore(countDiscount());
    }

    public void calculateBonus(){
        addScore(wall.countBonusScore());
    }


    public List<Tile> wallTiling(){
        List<Tile> remainder = new ArrayList<>();
        for(var line : patternLines){
            if(line.isFull()){
                var tile = line.removeTile(0);
                wall.setPlaced(line, tile);
                addScore(wall.countScore(line, tile));
                remainder.addAll(line.removeTiles());
            }
        }
        return remainder;
    }

    private int countDiscount(){
        switch (floor.getTiles().size()){
            case 0: return 0;
            case 1: return -1;
            case 2: return -2;
            case 3: return -4;
            case 4: return -6;
            case 5: return -8;
            case 6: return -11;
            default: return -14;
        }
    }

    public List<PatternLine> getPatternLines(){
        return patternLines;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public Wall getWall() {
        return wall;
    }

    public TileCollection getFloor(){
        return floor;
    }

    public String getName() {
        return name;
    }

    public void addScore(int score) {
        this.score += score;
    }
}
