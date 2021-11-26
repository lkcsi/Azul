package com.project.azul.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Wall {
    private ArrayList<ArrayList<TilePlace>> wall = new ArrayList<ArrayList<TilePlace>>();

    public Wall()
    {
        wall.add(new ArrayList<TilePlace>(Arrays.asList(new TilePlace(Color.BLUE), new TilePlace(Color.YELLOW), new TilePlace(Color.RED), new TilePlace(Color.BLACK), new TilePlace(Color.CYAN))));
        wall.add(new ArrayList<TilePlace>(Arrays.asList(new TilePlace(Color.CYAN), new TilePlace(Color.BLUE), new TilePlace(Color.YELLOW), new TilePlace(Color.RED), new TilePlace(Color.BLACK))));
        wall.add(new ArrayList<TilePlace>(Arrays.asList(new TilePlace(Color.BLACK), new TilePlace(Color.CYAN), new TilePlace(Color.BLUE), new TilePlace(Color.YELLOW), new TilePlace(Color.RED))));
        wall.add(new ArrayList<TilePlace>(Arrays.asList(new TilePlace(Color.RED), new TilePlace(Color.BLACK), new TilePlace(Color.CYAN), new TilePlace(Color.BLUE), new TilePlace(Color.YELLOW))));
        wall.add(new ArrayList<TilePlace>(Arrays.asList(new TilePlace(Color.YELLOW), new TilePlace(Color.RED), new TilePlace(Color.BLACK), new TilePlace(Color.CYAN), new TilePlace(Color.BLUE))));
    }

    public void add(PatternLine line, Tile tile)
    {
        wall.get(line.getLineNumber())
                .stream()
                .filter(t -> t.getColor() == tile.getColor())
                .findFirst().get().setPlaced(true);

    }
    public boolean isPlaced(int lineNumber, Color color)
    {
        return wall.get(lineNumber)
                .stream()
                .filter(t -> t.getColor() == color)
                .findFirst().get().isPlaced();
    }

    public int getRowCount(int lineNumber, Color color){
        var line = wall.get(lineNumber);
        var tile = line.stream().filter(t -> t.getColor() == color).findFirst().get();
        var tileIndex = line.indexOf(tile);

        int result = 0;
        for(int i = tileIndex-1; i >= 0; i--) {
            if(wall.get(lineNumber).get(i).isPlaced() != true)
                break;
            result += 1;
        }
        for(int i = tileIndex+1; i < line.size(); i++) {
            if(wall.get(lineNumber).get(i).isPlaced() != true)
                break;
            result += 1;
        }

        if(result != 0)
            result += 1;

        return result;
    }

    public int getColumnCount(int lineNumber, Color color){
        var line = wall.get(lineNumber);
        var tile = line.stream().filter(t -> t.getColor() == color).findFirst().get();
        var tileIndex = line.indexOf(tile);

        int result = 0;
        for(int j = lineNumber -1 ; j >= 0; j --){
            if(wall.get(lineNumber).get(tileIndex).isPlaced() != true)
                break;
        }
        for(int j = lineNumber + 1; j < wall.size(); j ++){
            if(wall.get(lineNumber).get(tileIndex).isPlaced() != true)
                break;
        }

        if(result != 0)
            result += 1;

        return result;
    }

    public boolean isComplete(){
        return wall.stream() .anyMatch(line -> line.stream()
                        .filter(tile -> tile.isPlaced() == true)
                        .count() == 5);
    }

    public ArrayList<ArrayList<String>> getString(){
        return wall.stream().map((line) -> {
            ArrayList<String> tiles = new ArrayList<String>(Collections.nCopies(5,"empty"));
            for(var tile : line){
                if(tile.isPlaced()){
                   tiles.set(line.indexOf(tile), tile.getColor().toString());
                }
            }
            return tiles;
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public Wall clone(){
        var result = new Wall();
        result.wall = new ArrayList<ArrayList<TilePlace>>(wall);
        return result;
    }

    class TilePlace{
        private final Color color;
        private boolean placed;

        public TilePlace(Color color){
            this.color = color;
            placed = false;
        }

        public boolean isPlaced() {
            return placed;
        }

        public void setPlaced(Boolean placed){
           this.placed = placed;
        }

        public Color getColor() {
            return color;
        }
    }
}
