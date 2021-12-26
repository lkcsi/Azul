package com.project.azul.creators;

import com.project.azul.models.*;

import java.util.ArrayList;
import java.util.List;

public class TileCollectionCreator {

    public static TileCollection createBag()
    {
        var bag = new TileCollection(100);

        bag.addTiles(createTiles(TileColor.BLACK, 20));
        bag.addTiles(createTiles(TileColor.RED, 20));
        bag.addTiles(createTiles(TileColor.BLUE, 20));
        bag.addTiles(createTiles(TileColor.WHITE, 20));
        bag.addTiles(createTiles(TileColor.YELLOW, 20));

        return bag;
    }

    public static TileCollection createCenter(){
        var center = new TileCollection(100);
        center.addTile(new Tile(TileColor.MARKER));
        return center;
    }

    public static List<Factory> createFactories(int players){
        var result = new ArrayList<Factory>();
        int factoryId = 0;
        for(int i = 0 ; i < players * 2 + 1; i++){
            result.add(new Factory(factoryId++));
        }

        return result;
    }

    private static ArrayList<Tile> createTiles(TileColor color, int number){
        var tiles = new ArrayList<Tile>();
        for(int i = 0; i < number; i++){
            tiles.add(new Tile(color));
        }
        return tiles;
    }

    public static TileCollection createDrop() {
        return new TileCollection(100);
    }

    public static TileCollection createFloor() {
        return  new TileCollection(7);
    }

    public static List<PatternLine> createPatternLines() {
        var patternLines = new ArrayList<PatternLine>();
        patternLines.add(new PatternLine(1));
        patternLines.add(new PatternLine(2));
        patternLines.add(new PatternLine(3));
        patternLines.add(new PatternLine(4));
        patternLines.add(new PatternLine(5));
        return patternLines;
    }
}
