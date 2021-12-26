package com.project.azul.models.helpers;

import com.project.azul.models.Game;
import com.project.azul.models.State;
import com.project.azul.models.Tile;
import com.project.azul.models.TileColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StateHandler {

    private final Game game;
    private int refillLimit = 2;
    private int refillCounter = 0;

    public StateHandler(Game game){
        this.game = game;
        fillFactories();
    }

    public void setGameState() {
        if(isRoundOver()){
            wallTiling();
            resetCurrentPlayer();
            if(isGameOver()){
                calculateBonus();
                game.setState(State.GAME_OVER);
            }
            else{
                fillFactories();
            }
            return;
        }
        nextPlayer();
    }

    private Tile getMarker(){
        for(var p : game.getPlayers()){
            var tiles = p.getFloor().getTiles(TileColor.MARKER);
            if(tiles.size() >= 1)
                return tiles.get(0);
        }
        return null;
    }

    private void resetCurrentPlayer() {
        game.setCurrentPlayer(game.getPlayers().get(0));
    }

    private void calculateBonus() {
        game.getPlayers().stream().forEach(p -> {
            p.calculateBonus();
        });
    }

    private void wallTiling() {
        game.getPlayers().stream().forEach((p) -> {
            var remainingTiles = p.wallTiling();
            game.getDrop().addTiles(remainingTiles);

            p.discount();
            var marker = getMarker();
            if(marker != null)
                game.getCenter().addTile(marker);

            game.getDrop().addTiles(p.getFloor().removeTiles());
        });
    }

    public void fillFactories() {
        var factories = game.getFactories();
        for(var factory : factories){
            factory.addTiles(getFourTileFromBag());
        }
    }

    private void refillBag() {
        var drop = game.getDrop();
        var bag = game.getBag();
        bag.addTiles(drop.removeTiles());
    }

    private List<Tile> getFourTileFromBag()
    {
        var bag = game.getBag();
        var result = new ArrayList<Tile>();

        if(bag.size() < 4 && refillLimit == refillCounter){
            refillBag();
        }

        int max = bag.size();
        for(int i = 0; i < 4 && i < max; i++){
            var random = new Random();
            int index = random.nextInt(max);
            result.add(bag.removeTile(index));
            max = bag.size();
        }
        return result;
    }

    private void nextPlayer()
    {
        var players = game.getPlayers();
        int index = players.indexOf(game.getCurrentPlayer());
        index = index + 1;

        if(index >= players.size())
            index = 0;

        game.setCurrentPlayer(players.get(index));
    }

    private boolean isRoundOver() {
        return game.getFactories().stream().noneMatch(f->f.size() > 0)
                && game.getCenter().size() == 0;
    }

    private boolean isGameOver(){
        return game.getPlayers().stream().anyMatch(p -> p.getWall().isComplete());
    }
}
