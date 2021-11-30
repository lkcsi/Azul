package com.azul.client.models;


import java.awt.*;

public enum TileColor {
    RED (Color.RED),
    BLUE (Color.BLUE),
    WHITE (Color.WHITE),
    BLACK (Color.BLACK),
    YELLOW (Color.YELLOW);

    private Color color;

    private TileColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }
}
