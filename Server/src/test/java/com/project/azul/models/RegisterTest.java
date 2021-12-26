package com.project.azul.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {

    @Test
    @DisplayName("Add players")
    void addPlayers(){
        Game game = new Game(UUID.randomUUID(), 4);

        assertEquals(State.WAITING_FOR_PLAYERS, game.getState());

        assertDoesNotThrow(() -> game.registerPlayer("Peter"));
        assertDoesNotThrow(() -> game.registerPlayer("Sarah"));
        assertDoesNotThrow(() -> game.registerPlayer("Donald"));
        assertDoesNotThrow(() -> game.registerPlayer("Sophie"));

        assertEquals(State.GAME_READY, game.getState());

        assertThrows(Exception.class, () -> game.registerPlayer("Pal"));

        assertEquals(State.GAME_READY, game.getState());
    }
}
