package com.project.azul.dto;

import com.project.azul.models.Player;

import java.util.ArrayList;

public class PlayerDto {
    private ArrayList<ArrayList<String>> wall;
    private ArrayList<ArrayList<String>> patternLines;
    private int score;
    private int id;
    private String name;

    public PlayerDto(Player player){
       id = player.getId();
       name = player.getName();
       score = player.getScore();
       wall = player.getWall().getString();
       patternLines = player.getPatternLines().getString();
    }

    public ArrayList<ArrayList<String>> getWall() {
        return wall;
    }

    public ArrayList<ArrayList<String>> getPatternLines() {
        return patternLines;
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
