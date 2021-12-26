package com.project.azul.models;

import com.project.azul.dto.PickForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PickTest {

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
    @DisplayName("Pick from factory")
    void pickFromFactory() {
        int factoryId = 0;
        var factory = game.getFactories().stream()
                .filter(f -> f.getId() == factoryId)
                .findFirst().get();

        var color = factory.getTiles().stream()
                .findAny().get()
                .getColor();

        var tilesToAdd = factory.getTiles().stream()
                .filter(t -> t.getColor() == color)
                .collect(Collectors.toList());

        var tilesToCenter = factory.getTiles().stream()
                .filter(t -> t.getColor() != color)
                .collect(Collectors.toList());


        tilesToCenter.addAll(game.getCenter().getTiles(TileColor.MARKER));

        var player = game.getCurrentPlayer();
        var line = player.getPatternLines().get(1);

        game.pickFromFactory(new PickForm(player.getName(), factoryId, color.name(), line.getLineNumber(), false));

        //Check line
        assertEquals(tilesToAdd.size() < line.capacity() ? tilesToAdd.size() : line.capacity(), line.size());
        assertEquals(color, line.getColor());

        //Check factory
        assertEquals(0, factory.size());
        assertEquals(tilesToCenter.size(), game.getCenter().size());

        //Check center
        assertEquals(true, listEqualsIgnoreOrder(game.getCenter().getTiles(), tilesToCenter));

        //Check floor
        assertEquals(line.capacity() < tilesToAdd.size() ? tilesToAdd.size() - line.capacity() : 0, player.getFloor().size());
    }


    @Test
    @DisplayName("Pick from center")
    void pickFromCenter(){
        var center = game.getCenter();
        center.addTile(new Tile(TileColor.YELLOW));
        center.addTile(new Tile(TileColor.YELLOW));
        center.addTile(new Tile(TileColor.YELLOW));
        center.addTile(new Tile(TileColor.BLUE));


        var color = TileColor.YELLOW;
        var tilesToAdd = center.getTiles();
        var player = game.getCurrentPlayer();
        var line = player.getPatternLines().get(0);

        var pickForm = new PickForm();

        game.pickFromCenter(new PickForm(player.getName(), 0, color.name(), line.getLineNumber(), false));

        //Check line
        assertEquals(1, line.size());
        assertEquals(line.capacity(), line.size());
        assertEquals(color, line.getColor());

        //Check center
        assertEquals(1, center.size());

        //Check floor
        assertEquals(3, player.getFloor().size());

        //Check marker
        assertEquals(true, player.getFloor().getTiles().stream()
                .anyMatch(t -> t.getColor() == TileColor.MARKER));
    }

    @Test
    @DisplayName("Add from factory to floor")
    void addFactoryToFloor(){
        var factory = game.getFactories().get(1);
        var color = factory.getTiles().stream().findAny().get().getColor();
        var player = game.getCurrentPlayer();

        var toFloor = factory.getTiles(color);

        var pickForm = new PickForm(player.getName(), factory.getId(), color.name(), 0, true);
        game.pickFromFactory(pickForm);

        //Check floor
        assertEquals(true, listEqualsIgnoreOrder(toFloor, player.getFloor().getTiles()));

        //Check marker
        assertEquals(true, player.getFloor().getTiles().stream()
                .noneMatch(t -> t.getColor() == TileColor.MARKER));
    }

    @Test
    @DisplayName("Add from center to floor")
    void addCenterToFloor() {
        var center = game.getCenter();
        center.addTile(new Tile(TileColor.YELLOW));
        center.addTile(new Tile(TileColor.YELLOW));
        center.addTile(new Tile(TileColor.YELLOW));
        center.addTile(new Tile(TileColor.BLUE));

        var color = TileColor.YELLOW;
        var player = game.getCurrentPlayer();
        var toFloor = center.getTiles(color);
        toFloor.addAll(game.getCenter().getTiles(TileColor.MARKER));

        var pickForm = new PickForm(player.getName(), 0, color.name(), 0, true);
        game.pickFromCenter(pickForm);

        //Check floor
        assertEquals(true, listEqualsIgnoreOrder(toFloor, player.getFloor().getTiles()));

        //Check center
        assertEquals(1, center.size());
        assertEquals(center.getTiles().get(0).getColor(), TileColor.BLUE);

        //Check marker
        assertEquals(true, player.getFloor().getTiles().stream()
                .anyMatch(t -> t.getColor() == TileColor.MARKER));
    }

    public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }
}
