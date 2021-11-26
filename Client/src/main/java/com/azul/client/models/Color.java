package com.azul.client.models;

public enum Color {
    RED(1),
    BLACK(2),
    WHITE(3),
    BLUE(4),
    YELLOW(5),
    NONE(0);

    private int id;
    private Color(int id){
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
