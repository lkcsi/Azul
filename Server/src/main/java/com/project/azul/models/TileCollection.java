package com.project.azul.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileCollection {
    private ArrayList<Tile> tiles;

    public TileCollection(){
        tiles = new ArrayList<Tile>();
    }

    public void addTile(Tile tile){
        tiles.add(tile);
    }

    public void addTiles(List<Tile> tilesToAdd) {
        tiles.addAll(tilesToAdd);
    }

    public ArrayList<Tile> getTiles(){
        return new ArrayList<Tile>(tiles);
    }

    public ArrayList<Tile> getTiles(TileColor color){
        return tiles.stream().filter(t -> t.getColor() == color).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Tile> removeTiles(TileColor color){
        var result = tiles.stream().filter(t -> t.getColor() == color).collect(Collectors.toCollection(ArrayList::new));
        tiles.removeAll(result);
        return result;
    }

    public ArrayList<Tile> removeTiles(){
        var result = new ArrayList<Tile>(tiles);
        tiles.clear();
        return result;
    }

    public boolean removeTile(Tile tile){
       return tiles.remove(tile);
    }

    public Tile removeTile(int index){
        return tiles.remove(index);
    }

    public int size(){
        return tiles.size();
    }

    public void clear(){
        tiles.clear();
    }

    public TileCollection clone(){
        var result = new TileCollection();
        tiles.forEach(t -> result.addTile(new Tile(t.getColor())));
        return result;
    }
}
