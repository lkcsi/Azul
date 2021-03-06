package com.azul.client.dtos;

import java.util.ArrayList;

public class PlayerDto {
    private ArrayList<ArrayList<String>> wall;
    private ArrayList<ArrayList<String>> patternLines;
    private ArrayList<String> floor;
    private int score;
    private int id;
    private String name;

    public ArrayList<ArrayList<String>> getWall() {
        return wall;
    }

    public ArrayList<ArrayList<String>> getPatternLines() {
        return patternLines;
    }

    public ArrayList<String> getFloor() {
        return floor;
    }
    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
