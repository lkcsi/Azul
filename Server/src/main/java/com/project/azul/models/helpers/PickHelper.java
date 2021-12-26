package com.project.azul.models.helpers;
import com.project.azul.dto.PickForm;
import com.project.azul.models.*;

import javax.naming.TimeLimitExceededException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PickHelper {

    private final Game game;

    public PickHelper(Game game){
        this.game = game;
    }

    public void pickFromFactory(PickForm pickForm) {
        checkState();
        var player = getPlayer(pickForm.getPlayerName());
        var color = getColor(pickForm.getColor());
        var factory = getFactory(pickForm.getFactoryId());
        var tiles = getTiles(factory, color);

        addToPlayer(player, tiles, pickForm);

        factory.removeTiles(color);
        game.getCenter().addTiles(factory.removeTiles());
    }

    public void pickFromCenter(PickForm pickForm){
        checkState();
        var player = getPlayer(pickForm.getPlayerName());
        var color = getColor(pickForm.getColor());
        var center = game.getCenter();
        var tiles = getTiles(center, color);

        var marker = game.getCenter().getTiles(TileColor.MARKER).stream().findAny().orElse(null);
        if(marker != null){
            player.addTilesToFloor(new ArrayList<>(Arrays.asList(marker)));
            center.removeTile(marker);
        }

        addToPlayer(player, tiles, pickForm);
        center.removeTiles(color);
    }

    private void addToPlayer(Player player, List<Tile> tiles, PickForm form){
        if(form.toFloor())
            player.addTilesToFloor(tiles);
        else player.addTilesToLine(tiles, form.getLineNumber());

        game.getDrop().addTiles(tiles);
    }

    private Player getPlayer(String playerName){
        var currentPlayer = game.getCurrentPlayer();
        var player = game.getPlayers().stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst();

        if(!player.isPresent())
            throw new RuntimeException("Player " + playerName + " does not exist in game");

        if(player.get() != currentPlayer)
            throw new RuntimeException("Wrong player order");

        return player.get();
    }

    private TileColor getColor(String colorName){
        try {
            var color = TileColor.valueOf(colorName);
            return color;
        }
        catch (Exception ex) {throw new RuntimeException("Color does not exist: " + colorName);}
    }

    private List<Tile> getTiles(TileCollection source, TileColor color){
        var tiles = source.getTiles(color);
        if(tiles.size() == 0)
            throw new RuntimeException("Color not found: " + color.toString());

        return tiles;
    }

    private Factory getFactory(int factoryId){
        var factory = game.getFactories().stream()
                .filter(f -> f.getId() == factoryId)
                .findFirst();

        if(!factory.isPresent())
            throw new RuntimeException("Factory does not exists");

        return factory.get();
    }

    private void checkState(){
        if(game.getState() != State.GAME_READY)
            throw new RuntimeException("Wrong state: " + game.getState().name());
    }
}
