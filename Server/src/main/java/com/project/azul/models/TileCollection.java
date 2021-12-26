package com.project.azul.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileCollection {
    private List<Tile> tiles;
    private String name;
    private int capacity;

    public TileCollection(int capacity){
        this.capacity = capacity;
        this.name = "TileCollection";
        tiles = new ArrayList<>();
    }

    public TileCollection(String name, int capacity){
        this.capacity = capacity;
        this.name = name;
        tiles = new ArrayList<>();
    }

    public Tile addTile(Tile tile){
        if(size() == capacity)
            throw new RuntimeException(name + " is full");

        tiles.add(tile);
        return tile;
    }

    //returns added tiles
    public List<Tile> addTiles(List<Tile> tiles) {
        if(size() == capacity)
            throw new RuntimeException(name + " is full");

        var tilesToAdd = tiles.stream()
                .limit(capacity - size())
                .collect(Collectors.toList());

        this.tiles.addAll(tilesToAdd);
        tiles.removeAll(tilesToAdd);

        return tilesToAdd;
    }

    public List<Tile> getTiles(){
        return new ArrayList<>(tiles);
    }

    public List<Tile> getTiles(TileColor color){
        return tiles.stream()
                .filter(t -> t.getColor() == color)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Tile> removeTiles(TileColor color){
        var result = tiles.stream()
                .filter(t -> t.getColor() == color)
                .collect(Collectors.toCollection(ArrayList::new));

        tiles.removeAll(result);
        return result;
    }

    public List<Tile> removeTiles(){
        var result = new ArrayList<Tile>(tiles);
        tiles.clear();
        return result;
    }

    public boolean removeTile(Tile tile){
        return tiles.remove(tile);
    }

    public Tile removeTile(TileColor tileColor){
        var tile = tiles.stream().filter(t -> t.getColor() == tileColor).findAny().orElse(null);
        if(tile != null)
            tiles.remove(tile);

        return tile;
    }

    public Tile removeTile(int index){
        var tile = tiles.get(index);
        tiles.remove(tile);
        return tile;
    }

    public int size(){
        return tiles.size();
    }

    public void clear(){
        tiles.clear();
    }

    public boolean isFull(){
        return capacity - size() == 0;
    }

    public int capacity() {
        return capacity;
    }
}
