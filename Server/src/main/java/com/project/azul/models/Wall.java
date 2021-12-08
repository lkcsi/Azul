package com.project.azul.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class Wall {
    private ArrayList<ArrayList<TilePlace>> wall = new ArrayList<ArrayList<TilePlace>>();

    public Wall()
    {
        wall.add(new ArrayList<>(Arrays.asList(new TilePlace(TileColor.BLUE), new TilePlace(TileColor.YELLOW), new TilePlace(TileColor.RED), new TilePlace(TileColor.BLACK), new TilePlace(TileColor.WHITE))));
        wall.add(new ArrayList<>(Arrays.asList(new TilePlace(TileColor.WHITE), new TilePlace(TileColor.BLUE), new TilePlace(TileColor.YELLOW), new TilePlace(TileColor.RED), new TilePlace(TileColor.BLACK))));
        wall.add(new ArrayList<>(Arrays.asList(new TilePlace(TileColor.BLACK), new TilePlace(TileColor.WHITE), new TilePlace(TileColor.BLUE), new TilePlace(TileColor.YELLOW), new TilePlace(TileColor.RED))));
        wall.add(new ArrayList<>(Arrays.asList(new TilePlace(TileColor.RED), new TilePlace(TileColor.BLACK), new TilePlace(TileColor.WHITE), new TilePlace(TileColor.BLUE), new TilePlace(TileColor.YELLOW))));
        wall.add(new ArrayList<>(Arrays.asList(new TilePlace(TileColor.YELLOW), new TilePlace(TileColor.RED), new TilePlace(TileColor.BLACK), new TilePlace(TileColor.WHITE), new TilePlace(TileColor.BLUE))));
    }

    public void add(PatternLine line, Tile tile)
    {
        wall.get(line.getLineNumber())
                .stream()
                .filter(t -> t.getColor() == tile.getColor())
                .findFirst().get().setPlaced(true);

    }
    public boolean isPlaced(int lineNumber, TileColor color)
    {
        return wall.get(lineNumber)
                .stream()
                .filter(t -> t.getColor() == color)
                .findFirst().get().isPlaced();
    }

    public int getRowCount(int lineNumber, TileColor color){
        var line = wall.get(lineNumber);
        var tile = line.stream().filter(t -> t.getColor() == color).findFirst().get();
        var tileIndex = line.indexOf(tile);

        int result = 0;
        for(int i = tileIndex-1; i >= 0; i--) {
            if(!wall.get(lineNumber).get(i).isPlaced())
                break;
            result += 1;
        }
        for(int i = tileIndex+1; i < line.size(); i++) {
            if(!wall.get(lineNumber).get(i).isPlaced())
                break;
            result += 1;
        }

        if(result != 0)
            return result + 1;
        return 0;
    }

    public int getColumnCount(int lineNumber, TileColor color){
        var line = wall.get(lineNumber);
        var tile = line.stream().filter(t -> t.getColor() == color).findFirst().get();
        var tileIndex = line.indexOf(tile);

        int result = 0;
        for(int j = lineNumber -1 ; j >= 0; j --){
            if(!wall.get(j).get(tileIndex).isPlaced())
                break;
            result += 1;
        }
        for(int j = lineNumber + 1; j < wall.size(); j ++){
            if(!wall.get(j).get(tileIndex).isPlaced())
                break;
            result += 1;
        }

        if(result != 0)
            return result + 1;
        return 0;
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
        wall.forEach(line -> line.stream().filter(tile -> tile.isPlaced()).forEach(
                (tile -> {
                    int columnIndex = line.indexOf(tile);
                    int rowIndex = wall.indexOf(line);
                    wall.get(rowIndex).get(columnIndex).setPlaced(true);
                })
        ));
        return result;
    }

    public int getColorCompleted() {
        int result = isColorCompleted(TileColor.WHITE) ? 1 : 0;
        result += isColorCompleted(TileColor.BLACK) ? 1 : 0;
        result += isColorCompleted(TileColor.RED) ? 1 : 0;
        result += isColorCompleted(TileColor.BLUE) ? 1 : 0;
        result += isColorCompleted(TileColor.YELLOW) ? 1 : 0;
        return result;
    }

    private boolean isColorCompleted(TileColor color){
        for(var line : wall){
            if(!line.stream().anyMatch(t -> t.isPlaced() && t.getColor() == color))
                return false;
        }
        return true;
    }

    public int getColumnCompleted() {
        int result = 0;
        for(int i = 0 ; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(!wall.get(j).get(i).isPlaced())
                    break;
            }
            result++;
        }
        return result;
    }

    public int getLineCompleted() {
        return (int) wall.stream()
                .filter(line -> line.stream().allMatch(t-> t.isPlaced()))
                .count();
    }

    class TilePlace{
        private final TileColor color;
        private boolean placed;

        public TilePlace(TileColor color){
            this.color = color;
            placed = false;
        }

        public boolean isPlaced() {
            return placed;
        }

        public void setPlaced(Boolean placed){
           this.placed = placed;
        }

        public TileColor getColor() {
            return color;
        }
    }
}
