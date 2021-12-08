package com.project.azul.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class PatternLines {

    private ArrayList<PatternLine> patternLines = new ArrayList<PatternLine>();
    public PatternLines(){
        patternLines.add(new PatternLine(1));
        patternLines.add(new PatternLine(2));
        patternLines.add(new PatternLine(3));
        patternLines.add(new PatternLine(4));
        patternLines.add(new PatternLine(5));
    }

    public void addTilesToLine(ArrayList<Tile> tiles, int lineNumber) {
        if(lineNumber < 0 || lineNumber > 4)
            throw new RuntimeException("Pattern line does not exist");

        var line = patternLines.get(lineNumber);
        line.addTiles(tiles);
    }

    public ArrayList<ArrayList<String>> getString(){
        return patternLines.stream().map((line) -> {
            ArrayList<String> tiles = new ArrayList<String>(Collections.nCopies(line.capacity(), "empty"));
            int index = line.capacity() - 1;
            for(var tile : line.getTiles()) {
                tiles.set(index--, tile.getColor().toString());
            }
            return tiles;
        }).collect(Collectors.toCollection(ArrayList::new));
    }
    public PatternLine get(int index){
        return patternLines.get(index);
    }

    public PatternLines clone(){
        var result = new PatternLines();
        result.patternLines = patternLines.stream()
                .map(p -> p.clone())
                .collect(Collectors.toCollection(ArrayList::new));

        return result;
    }
}
