package com.azul.client.models;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Wall {
    private ArrayList<ArrayList<TileColor>> tiles;
    private static Wall instance;

    public static Wall getInstance(){
       if(instance == null) {
           instance = new Wall();
       }
       return instance;
    }

    public Wall(){
        tiles = new ArrayList<>();
        tiles.add(new ArrayList<>(Arrays.asList(TileColor.BLUE, TileColor.YELLOW, TileColor.RED, TileColor.BLACK, TileColor.WHITE)));
        tiles.add(new ArrayList<>(Arrays.asList(TileColor.WHITE, TileColor.BLUE, TileColor.YELLOW, TileColor.RED, TileColor.BLACK)));
        tiles.add(new ArrayList<>(Arrays.asList(TileColor.BLACK, TileColor.WHITE, TileColor.BLUE, TileColor.YELLOW, TileColor.RED)));
        tiles.add(new ArrayList<>(Arrays.asList(TileColor.RED, TileColor.BLACK, TileColor.WHITE, TileColor.BLUE, TileColor.YELLOW)));
        tiles.add(new ArrayList<>(Arrays.asList(TileColor.YELLOW, TileColor.RED, TileColor.BLACK, TileColor.WHITE, TileColor.BLUE)));
    }

    public ArrayList<TileColor> get(int index){
        return tiles.get(index);
    }
}
