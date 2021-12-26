package com.project.azul.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wall {
    private List<List<Tile>> tiles = new ArrayList<>();

    public Wall()
    {
        tiles.add(new ArrayList<>(Arrays.asList(new Tile(TileColor.BLUE), new Tile(TileColor.YELLOW), new Tile(TileColor.RED), new Tile(TileColor.BLACK), new Tile(TileColor.WHITE))));
        tiles.add(new ArrayList<>(Arrays.asList(new Tile(TileColor.WHITE), new Tile(TileColor.BLUE), new Tile(TileColor.YELLOW), new Tile(TileColor.RED), new Tile(TileColor.BLACK))));
        tiles.add(new ArrayList<>(Arrays.asList(new Tile(TileColor.BLACK), new Tile(TileColor.WHITE), new Tile(TileColor.BLUE), new Tile(TileColor.YELLOW), new Tile(TileColor.RED))));
        tiles.add(new ArrayList<>(Arrays.asList(new Tile(TileColor.RED), new Tile(TileColor.BLACK), new Tile(TileColor.WHITE), new Tile(TileColor.BLUE), new Tile(TileColor.YELLOW))));
        tiles.add(new ArrayList<>(Arrays.asList(new Tile(TileColor.YELLOW), new Tile(TileColor.RED), new Tile(TileColor.BLACK), new Tile(TileColor.WHITE), new Tile(TileColor.BLUE))));
    }

    public void setPlaced(PatternLine line, Tile tile) {
        setPlaced(line.getLineNumber(), tile.getColor());
    }
    public void setPlaced(int lineNumber, TileColor tileColor) {

        if(isPlaced(lineNumber, tileColor))
            throw new RuntimeException(("Color is already filled in wall"));

        tiles.get(lineNumber)
                .stream()
                .filter(t -> t.getColor() == tileColor)
                .findFirst().get().setPlaced(true);

    }
    public boolean isPlaced(PatternLine line, Tile tile) {
        return isPlaced(line.getLineNumber(), tile.getColor());
    }

    public boolean isPlaced(int lineNumber, TileColor color) {
        return tiles.get(lineNumber)
                .stream()
                .filter(t -> t.getColor() == color)
                .findFirst().get().isPlaced();
    }

    public int countScore(PatternLine line, Tile tile){
        int column = getColumnCount(line.getLineNumber(), tile.getColor());
        int row = getRowCount(line.getLineNumber(), tile.getColor());

        if(row == 0 && column == 0)
            return 1;
        else
            return (row + column);
    }


    public int countBonusScore(){
        int result = (getColorCompleted() * 10);
        result += (getLineCompleted() * 2);
        result += (getColumnCompleted() * 7);

        return result;
    }
    public int getRowCount(int lineNumber, TileColor color){
        var line = tiles.get(lineNumber);
        var tile = line.stream().filter(t -> t.getColor() == color).findFirst().get();
        var tileIndex = line.indexOf(tile);

        int result = 0;
        for(int i = tileIndex-1; i >= 0; i--) {
            if(!tiles.get(lineNumber).get(i).isPlaced())
                break;
            result += 1;
        }
        for(int i = tileIndex+1; i < line.size(); i++) {
            if(!tiles.get(lineNumber).get(i).isPlaced())
                break;
            result += 1;
        }

        if(result != 0)
            return result + 1;
        return 0;
    }

    public int getColumnCount(int lineNumber, TileColor color){
        var line = tiles.get(lineNumber);
        var tile = line.stream().filter(t -> t.getColor() == color).findFirst().get();
        var tileIndex = line.indexOf(tile);

        int result = 0;
        for(int j = lineNumber -1 ; j >= 0; j --){
            if(!tiles.get(j).get(tileIndex).isPlaced())
                break;
            result += 1;
        }
        for(int j = lineNumber + 1; j < tiles.size(); j ++){
            if(!tiles.get(j).get(tileIndex).isPlaced())
                break;
            result += 1;
        }

        if(result != 0)
            return result + 1;
        return 0;
    }

    public boolean isComplete(){
        return tiles.stream() .anyMatch(line -> line.stream()
                        .filter(tile -> tile.isPlaced() == true)
                        .count() == 5);
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
        for(var line : tiles){
            if(!line.stream().anyMatch(t -> t.isPlaced() && t.getColor() == color))
                return false;
        }
        return true;
    }

    public int getColumnCompleted() {
        int result = 0;
        for(int i = 0 ; i < 5; i++){
            int rowCounter = 0;
            for(int j = 0; j < 5; j++){
                if(!tiles.get(j).get(i).isPlaced())
                    break;
                rowCounter++;
            }
            if(rowCounter == 5)
                result++;
        }
        return result;
    }

    public int getLineCompleted() {
        return (int) tiles.stream()
                .filter(line -> line.stream().allMatch(t-> t.isPlaced()))
                .count();
    }

    public List<List<Tile>> getTiles(){
        return tiles;
    }
}
