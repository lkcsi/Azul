package com.project.azul.repositories;

import com.project.azul.models.Game;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class GameRepository {
    private List<Game> games;

    public GameRepository(){
        games = new ArrayList<>();
    }

    public Game getGameById(UUID id){
        return games.stream().filter(g -> g.getId() == id).findAny().orElse(null);
    }

    public void add(Game game) {
        games.add(game);
    }
}
