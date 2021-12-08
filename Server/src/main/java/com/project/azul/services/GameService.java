package com.project.azul.services;

import com.project.azul.dto.GameDto;
import com.project.azul.dto.PickForm;
import com.project.azul.dto.PlayerDto;
import com.project.azul.models.Factory;
import com.project.azul.models.Game;
import com.project.azul.models.Player;
import com.project.azul.models.TileCollection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
@Scope("singleton")
public class GameService {
    private ArrayList<Game> games;

    public GameService(){
        games = new ArrayList<>();
    }

    public GameDto newGame(int players) {
        if(players > 4 || players < 2)
            throw new RuntimeException("Invalid number of players");

        var game = new Game(UUID.randomUUID(), players);
        games.add(game);

        return new GameDto(game);
    }

    public void pickFromFactory(UUID gameId, PickForm pickForm) throws Exception {
        var game = getGame(gameId);
        var backup = getGame(gameId).clone();
        try {
            game.pickFromFactory(pickForm.getPlayerName(),
                    pickForm.getFactoryId(),
                    pickForm.getLineNumber(),
                    pickForm.getColor(),
                    pickForm.isToFloor());
        } catch (Exception e) {
            setGame(gameId, backup);
            throw e;
        }
    }
    public void pickFromCenter(UUID gameId, PickForm pickForm) throws Exception {
        var game = getGame(gameId);
        var backup = game.clone();
        try {
            game.pickFromCenter(pickForm.getPlayerName(),
                    pickForm.getLineNumber(),
                    pickForm.getColor(),
                    pickForm.isToFloor());
        } catch (Exception e) {
            setGame(gameId, backup);
            throw e;
        }
    }
    public PlayerDto addPlayer(UUID gameId, String playerName) {
        var game = getGame(gameId);
        return new PlayerDto(game.registerPlayer(playerName));
    }

    public Game getGame(UUID gameId){
        var result = games.stream().filter(g -> g.getId().equals(gameId)).findFirst();
        if(!result.isPresent())
            throw new RuntimeException("Game not found");
        return result.get();
    }
    public void setGame(UUID gameId, Game game){
        var result = games.stream().filter(g -> g.getId().equals(gameId)).findFirst();
        games.set(games.indexOf(result.get()), game);
    }

    public Player getPlayer(UUID gameId, String playerName) throws Exception {
        var game = getGame(gameId);
        var player = game
                .getPlayers()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst();

        if(player.isPresent())
            throw new Exception("Player not found");

        return player.get();
    }

    public Collection<Player> getPlayers(UUID gameId){
        return getGame(gameId).getPlayers();
    }

    public Factory getCenter(UUID gameId) {
        return getGame(gameId).getCenter();
    }

    public Collection<Factory> getFactories(UUID gameId) {
        return getGame(gameId).getFactories();
    }

    public TileCollection getBag(UUID gameId) {
        return getGame(gameId).getBag();
    }

    public TileCollection getDrop(UUID gameId) {
        return getGame(gameId).getDrop();
    }
}
