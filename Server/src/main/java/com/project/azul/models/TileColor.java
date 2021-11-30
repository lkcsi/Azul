package com.project.azul.models;


import java.awt.*;

public enum TileColor {
    RED (Color.RED),
    GREEN (Color.GREEN),
    BLUE (Color.BLUE),
    WHITE (Color.WHITE),
    BLACK (Color.BLACK),
    YELLOW (Color.YELLOW);

    private Color color;

    private TileColor(Color color){
        this.color = color;
    }
}
