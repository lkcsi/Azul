package com.project.azul.controllers;

import com.project.azul.dto.*;
import com.project.azul.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path = "/game")
    public GameDto newGame(@RequestParam("players") int players) throws Exception {
        return gameService.newGame(players);
    }

    @PostMapping(path = "/factory-pick")
    public void pickFromFactory(@RequestParam UUID gameId, @RequestBody PickForm pickForm) throws Exception {
        gameService.pickFromFactory(gameId, pickForm);
    }

    @PostMapping(path = "/center-pick")
    public void pickFromCenter(@RequestParam UUID gameId, @RequestBody PickForm pickForm) throws Exception {
        gameService.pickFromCenter(gameId, pickForm);
    }


    @GetMapping(path = "/game")
    public GameDto getGameDto(@RequestParam("gameId") UUID gameId){
        return new GameDto(gameService.getGameById(gameId));
    }

    @GetMapping(path = "/bag")
    public TileCollectionDto getBag(@RequestParam("gameId") UUID gameId){
        return new TileCollectionDto(gameService.getBag(gameId));
    }
    @GetMapping(path = "/drop")
    public TileCollectionDto getDrop(@RequestParam("gameId") UUID gameId){
        return new TileCollectionDto(gameService.getDrop(gameId));
    }

    @GetMapping(path = "/factories")
    public Collection<FactoryDto> getFactories(@RequestParam UUID gameId){
        return gameService.getFactories(gameId)
                .stream()
                .map(p -> new FactoryDto(p))
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/center")
    public TileCollectionDto getCenter(@RequestParam("gameId") UUID gameId){
        return new TileCollectionDto(gameService.getCenter(gameId));
    }

    @PostMapping(path = "/player")
    public PlayerDto addPlayer(@RequestParam("gameId") UUID gameId,
                               @RequestParam("name") String playerName) throws Exception {
        return gameService.addPlayer(gameId, playerName);
    }
    @GetMapping(path = "/player")
    public PlayerDto getPlayer(@RequestParam("gameId") UUID gameId,
                               @RequestParam("name") String playerName) throws Exception {
        var player = gameService
                .getPlayer(gameId, playerName);

        return new PlayerDto(player);
    }
    @GetMapping(path = "/players")
    public Collection<PlayerDto> getPlayers(@RequestParam("gameId") UUID gameId){
        return gameService.getPlayers(gameId)
                .stream()
                .map(p -> new PlayerDto(p))
                .collect(Collectors.toList());
    }
}
