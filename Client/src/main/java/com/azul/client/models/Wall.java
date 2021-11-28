package com.azul.client.models;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Wall {
    private ArrayList<ArrayList<Color>> tiles;
    private static Wall instance;

    public static Wall getInstance(){
       if(instance == null) {
           instance = new Wall();
       }
       return instance;
    }

    public Wall(){
        tiles = new ArrayList<ArrayList<Color>>();
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.WHITE, Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.BLACK, Color.WHITE, Color.BLUE, Color.YELLOW, Color.RED)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.RED, Color.BLACK, Color.WHITE, Color.BLUE, Color.YELLOW)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE, Color.BLUE)));
    }

    public ArrayList<Color> get(int index){
        return tiles.get(index);
    }
}
