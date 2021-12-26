package com.project.azul.dto;

public class PickForm {
    private String playerName;
    private int factoryId;
    private String color;
    private int lineNumber;
    private boolean toFloor;

    public PickForm(){}
    public PickForm(String playerName, int factoryId, String colorName, int lineNumber, boolean toFloor){
        this.playerName = playerName;
        this.factoryId = factoryId;
        this.color = colorName;
        this.lineNumber = lineNumber;
        this.toFloor = toFloor;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(int factoryId) {
        this.factoryId = factoryId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean toFloor() {
        return toFloor;
    }

    public void setToFloor(boolean toFloor) {
        this.toFloor = toFloor;
    }
}
