package com.project.azul.services;

import com.project.azul.dto.CheckDto;
import com.project.azul.dto.GameDto;
import com.project.azul.dto.PickForm;
import com.project.azul.dto.PlayerDto;
import com.project.azul.models.helpers.PickHelper;
import com.project.azul.models.*;
import com.project.azul.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class GameService {

    GameRepository gameRepository;
    PickHelper pickService;

    @Autowired
    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public GameDto newGame(int players) {
        if(players > 4 || players < 2)
            throw new RuntimeException("Invalid number of players");

        var game = new Game(UUID.randomUUID(), players);
        gameRepository.add(game);
        return new GameDto(game);
    }

    public CheckDto pickFromFactory(UUID gameId, PickForm pickForm) throws Exception {
        var game = getGameById(gameId);
        try {
            new PickHelper(game).pickFromFactory(pickForm);
        } catch (Exception e) {
            return new CheckDto(false, e.getMessage());
        }
        return new CheckDto(true, "Success");
    }
    public CheckDto pickFromCenter(UUID gameId, PickForm pickForm) throws Exception {
        pickForm.setFactoryId(10);
        return pickFromFactory(gameId, pickForm);
    }

    public PlayerDto addPlayer(UUID gameId, String playerName) {
        var game = getGameById(gameId);
        return new PlayerDto(game.registerPlayer(playerName));
    }

    public Game getGameById(UUID gameId){
        var result = gameRepository.getGameById(gameId);
        if(result == null)
            throw new RuntimeException("Game not found");
        return result;
    }

    public Player getPlayer(UUID gameId, String playerName) throws Exception {
        var game = getGameById(gameId);
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
        return getGameById(gameId).getPlayers();
    }

    public TileCollection getCenter(UUID gameId) {
        return getGameById(gameId).getCenter();
    }

    public Collection<Factory> getFactories(UUID gameId) {
        return getGameById(gameId).getFactories();
    }

    public TileCollection getBag(UUID gameId) {
        return getGameById(gameId).getBag();
    }

    public TileCollection getDrop(UUID gameId) {
        return getGameById(gameId).getDrop();
    }
}
