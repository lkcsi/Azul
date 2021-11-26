package com.azul.client.models;

import java.util.ArrayList;
import java.util.Arrays;

public class Wall {
    private ArrayList<ArrayList<Color>> tiles;

    public Wall(){
        tiles = new ArrayList<ArrayList<Color>>();
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.WHITE, Color.BLUE, Color.YELLOW, Color.RED, Color.BLACK)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.BLACK, Color.WHITE, Color.BLUE, Color.YELLOW, Color.RED)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.RED, Color.BLACK, Color.WHITE, Color.BLUE, Color.YELLOW)));
        tiles.add(new ArrayList<Color>(Arrays.asList(Color.YELLOW, Color.RED, Color.BLACK, Color.WHITE, Color.BLUE)));
    }
}
