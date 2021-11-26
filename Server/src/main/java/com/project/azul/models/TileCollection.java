package com.project.azul.models;

import com.project.azul.api.Code;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class TileCollection {
    private ArrayList<Tile> tiles;

    public TileCollection(){
        tiles = new ArrayList<Tile>();
    }

    public Code addTile(Tile tile){
        tiles.add(tile);
        return Code.SUCCESS;
    }

    public Code addTiles(ArrayList<Tile> tilesToAdd){
        tiles.addAll(tilesToAdd);
        tilesToAdd.clear();
        return Code.SUCCESS;
    }

    public ArrayList<Tile> getTiles(){
        return new ArrayList<Tile>(tiles);
    }

    public ArrayList<Tile> getTiles(Color color){
        return tiles.stream().filter(t -> t.getColor() == color).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Tile> removeTiles(Color color){
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
        result.tiles = new ArrayList<Tile>(tiles);
        return result;
    }
}
