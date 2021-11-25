package com.project.azul.api;

import com.google.gson.Gson;
import com.project.azul.models.Game;

import java.util.stream.Collectors;

public class GameApiVer1 implements GameApi{

    private Game game;

    public GameApiVer1(Game game){

        this.game = game;
    }

    @Override
    public void Step(int playerId, int factoryId, int colorCode, int lineNumber) {

    }

    @Override
    public String GetPlayers() {
        return new Gson().toJson(game.getPlayers());
    }
}
