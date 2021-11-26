package com.project.azul.controllers;

import com.project.azul.api.Code;
import com.project.azul.dto.*;
import com.project.azul.models.*;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class GameController {
    private Game game;

    public GameController()
    {
    }

    @PostMapping(path = "/game/new/{number}")
    public Code newGame(@PathVariable("number") int numberOfPlayers){
        if(numberOfPlayers < 2 || numberOfPlayers > 4)
            return Code.INVALID_NUMBER_OF_PLAYERS;
        game = new Game(numberOfPlayers);
        return Code.SUCCESS;
    }

    @PostMapping(path = "/factories/pick")
    public Code pickFromFactory(@RequestBody PickForm pickForm){
       var backup = game.clone();
       var result = game.pickFromFactory(pickForm.getPlayerName(),
               pickForm.getFactoryId(),
               pickForm.getLineNumber(),
               pickForm.getColor());

       if(result!=Code.SUCCESS)
           game = backup;

       return result;
    }

    @PostMapping(path = "/center/pick")
    public Code pickFromCenter(@RequestBody PickForm pickForm){
        var backup = game.clone();
        var result = game.pickFromCenter(pickForm.getPlayerName(),
                pickForm.getLineNumber(),
                pickForm.getColor());

        if(result!=Code.SUCCESS)
            game = backup;

        return result;
    }

    @PostMapping(path = "/players/new/{name}")
    public Code addPlayer(@PathVariable("name") String playerName){
        return game.registerPlayer(playerName);
    }

    @GetMapping(path = "/game")
    public GameDto getGame(){
        return new GameDto(game);
    }

    @GetMapping(path = "/players")
    public Collection<PlayerDto> getPlayers(){
        return game.getPlayers().stream().map(p -> new PlayerDto(p)).collect(Collectors.toList());
    }
    @PostMapping(path = "/players")
    public Code addPlayers(@RequestBody Collection<String> names){
        for(var name : names){
            var result = game.registerPlayer(name);
            if(result != Code.SUCCESS)
                return result;
        }
        return Code.SUCCESS;
    }

    @GetMapping(path = "/factories")
    public Collection<FactoryDto> getFactories(){
        return game.getFactories().stream().map(p -> new FactoryDto(p)).collect(Collectors.toList());
    }

    @GetMapping(path = "/center")
    public FactoryDto getCenter(){
        return new FactoryDto(game.getCenter());
    }
}
