package com.azul.client.dtos;

public class Code {

    public static Code SUCCESS = new Code(0, "Success");
    public static Code FACTORY_DOES_NOT_EXIST = new Code(1, "Factory does not exist");
    public static Code FACTORY_IS_EMPTY = new Code(2, "Factory is empty");
    public static Code COLOR_DOES_NOT_EXIST = new Code(3, "Color does not exist");
    public static Code PATTERN_LINE_IS_FULL = new Code(4, "Pattern line is full");
    public static Code COLOR_NOT_FOUND_IN_FACTORY = new Code(5, "Color not found in factory");
    public static Code PATTERN_LINE_DOES_NOT_EXIST = new Code(6, "Pattern line does not exist");
    public static Code PLAYER_DOES_NOT_EXIST = new Code(7, "Player does not exist");
    public static Code PATTERN_LINE_COLOR_MISMATCH = new Code(8, "Pattern line color mismatch");
    public static Code WRONG_PLAYER_ORDER = new Code(9, "Wrong player order");
    public static Code GAME_IS_FULL = new Code (10, "Game is full");
    public static Code PLAYER_NAME_EXISTS = new Code(11, "Player name exists");
    public static Code WAITING_FOR_PLAYERS = new Code(12, "Waiting for players");
    public static Code GAME_NOT_STARTED = new Code(13, "Game not started");
    public static Code CENTER_IS_EMPTY = new Code(14, "Center is empty");
    public static Code INVALID_NUMBER_OF_PLAYERS = new Code(15, "Invalid number of players");

    private int code;
    private String description;

    private Code(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
}
