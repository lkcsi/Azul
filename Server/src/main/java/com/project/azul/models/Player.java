package com.project.azul.models;

import com.project.azul.api.Code;

import java.util.ArrayList;

public class Player {

    private Wall wall = new Wall();
    private Floor floor = new Floor();
    private PatternLines patternLines = new PatternLines();


    private int score = 0;
    private final int id;
    private final String name;

    public Player(int id, String name) {
       this.id = id;
        this.name = name;
    }

    public Code addTilesToLine(ArrayList<Tile> tiles, int lineNumber) {
        var result = patternLines.addTilesToLine(tiles, lineNumber);
        floor.addTiles(tiles);
        return result;
    }

    public ArrayList<Tile> wallTiling(){
       ArrayList<Tile> remainder = new ArrayList<Tile>();
       for(int i = 0 ; i < 5 ; i++){
          var line = patternLines.get(i);
          if(line.size() == line.capacity()){
              var tile = line.removeTile(0);
              remainder.addAll(line.removeTiles());
              wall.add(line, tile);
              countScore(line, tile);
          }
       }
       return remainder;
    }

    private void countScore(PatternLine line, Tile tile){
       int column = wall.getColumnCount(line.getLineNumber(), tile.getColor());
       int row = wall.getRowCount(line.getLineNumber(), tile.getColor());

       if(row == 0 && column == 0)
           score += 1;
       else
           score += (row + column);

       line.clear();
    }

    public PatternLines getPatternLines(){
        return patternLines;
    }


    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public Wall getWall() {
        return wall;
    }
    public String getName() {
        return name;
    }

    public void countBonus() {
    }

    public Player clone(){
        Player player = new Player(id, name);
        player.patternLines = patternLines.clone();
        player.wall = wall.clone();
        player.floor = floor.clone();
        player.score = score;
        return player;
    }
}
