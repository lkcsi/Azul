package com.project.azul.models;
import com.project.azul.dto.PickForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PickExceptionTest {

    private Game game;

    @BeforeEach
    void setUp(){
        this.game = new Game(UUID.randomUUID(), 4);
        game.registerPlayer("Dave");
        game.registerPlayer("Mike");
        game.registerPlayer("Lana");
        game.registerPlayer("Angelina");
    }
    @Test
    @DisplayName("Wrong state")
    void wrongState(){
        var factory = game.getFactories().get(0);
        var color = factory.getTiles().get(0).getColor();
        var player = game.getCurrentPlayer();

        var pickForm = new PickForm(player.getName(), factory.getId(), color.name(), 2, false);

        game.setState(State.GAME_OVER);
        var ex = assertThrows(RuntimeException.class, () -> game.pickFromFactory(pickForm));
        assertEquals(true, ex.getMessage().equalsIgnoreCase("Wrong state: " + State.GAME_OVER));

        game.setState(State.WAITING_FOR_PLAYERS);
        ex = assertThrows(RuntimeException.class, () -> game.pickFromFactory(pickForm));
        assertEquals(true, ex.getMessage().equalsIgnoreCase("Wrong state: " + State.WAITING_FOR_PLAYERS));
    }

    @Test
    @DisplayName("Wrong player")
    void wrongPlayer(){
        var player = game.getPlayers().get(1);
        var factory = game.getFactories().get(0);
        var color = factory.getTiles().get(0).getColor();

        var pickForm = new PickForm(player.getName(), factory.getId(), color.name(), 1, false);
        var ex = assertThrows(RuntimeException.class, () -> game.pickFromFactory(pickForm));
        assertEquals(true, ex.getMessage().equalsIgnoreCase("Wrong player order"));

        pickForm.setPlayerName("Shark");
        ex = assertThrows(RuntimeException.class, () -> game.pickFromFactory(pickForm));
        assertEquals(true, ex.getMessage().equalsIgnoreCase("Player Shark does not exist in game"));
    }


    @Test
    @DisplayName("Wrong color test")
    void wrongColor(){
        var player = game.getCurrentPlayer();
        var factory = game.getFactories().get(0);
        game.getBag().addTiles(factory.removeTiles());
        factory.addTiles(getTilesFromBag(TileColor.WHITE, 4));

        var pickForm = new PickForm(player.getName(), factory.getId(), TileColor.BLUE.name(), 1, false);
        var ex = assertThrows(RuntimeException.class, () -> game.pickFromFactory(pickForm));
        assertEquals("Color not found: BLUE", ex.getMessage());

        pickForm.setColor("KHAKI");
        ex = assertThrows(RuntimeException.class, () -> game.pickFromFactory(pickForm));
        assertEquals("Color does not exist: KHAKI", ex.getMessage());
    }

    private List<Tile> getTilesFromBag(TileColor color, int count){
        var tiles = game.getBag().getTiles(color).stream().limit(count).collect(Collectors.toList());
        game.getBag().getTiles().removeAll(tiles);
        return tiles;
    }
}
