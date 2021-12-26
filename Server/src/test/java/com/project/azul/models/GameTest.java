package com.project.azul.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    @DisplayName("Create game")
    void createGame(){
        assertThrows(Exception.class, () -> new Game(UUID.randomUUID(), 1));
        assertDoesNotThrow(() -> new Game(UUID.randomUUID(),2));
        assertDoesNotThrow(() -> new Game(UUID.randomUUID(), 3));
        assertDoesNotThrow(() -> new Game(UUID.randomUUID(), 4));
        assertThrows(Exception.class, () -> new Game(UUID.randomUUID(), 5));
    }


    @Test
    @DisplayName("Check factories")
    void numberOfFactories(){
        Game game = new Game(UUID.randomUUID(), 2);
        assertEquals(5, game.getFactories().size());

        game = new Game(UUID.randomUUID(), 3);
        assertEquals(7, game.getFactories().size());

        game = new Game(UUID.randomUUID(), 4);
        assertEquals(9, game.getFactories().size());
    }

}
