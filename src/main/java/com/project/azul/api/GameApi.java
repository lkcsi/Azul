package com.project.azul.api;


import java.util.Collection;

public interface GameApi {
    void Step(int playerId, int factoryId, int colorCode, int lineNumber);
    String GetPlayers();
}
