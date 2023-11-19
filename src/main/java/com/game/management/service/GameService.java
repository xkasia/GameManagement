package com.game.management.service;

import com.game.management.model.Game;

import java.util.List;

public interface GameService {

    Game createGame(Game game);

    Game getGame(String name);

    List<Game> getAllGames();

    Game updateGame(Game game);

    void deleteGame(String name);
}