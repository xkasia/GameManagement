package com.game.management.service;

import com.game.management.exception.GameAlreadyExistsException;
import com.game.management.exception.ResourceNotFoundException;
import com.game.management.exception.ValidationException;
import com.game.management.model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the {@link GameService} interface providing CRUD operations for games.
 * This service manages games using an in-memory cache backed by a ConcurrentHashMap.
 *
 * @author Katarzyna Lesniak
 * @version 1.0
 * @since 2023-11-18
 */

@Service
public class GameServiceImpl implements GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);

    /**
     * In-memory cache to store games with their names as keys.
     */
    private final ConcurrentHashMap<String, Game> gameCache;

    public GameServiceImpl() {
        this.gameCache = new ConcurrentHashMap<>();
    }

    // Constructor for testing with a custom cache
    public GameServiceImpl(ConcurrentHashMap<String, Game> gameCache) {
        this.gameCache = gameCache;
    }

    @Override
    public Game createGame(Game game) {

        String gameName = game.getName();

        // we have validation annotations for it, but just in case
        validateGameName(gameName);
        game.setCreationDate(LocalDateTime.now());

        // Atomically check if the key is present and put the value if absent
        Game newGame = gameCache.putIfAbsent(gameName, game);

        if (newGame == null) {
            // The key was not present, the game was added
            logger.info("Created game: {}", game);
            return game;
        } else {
            // The key was present, indicating that the game already exists
            throw new GameAlreadyExistsException("Game with name: " + gameName + " already exists.");
        }
    }

    @Override
    public Game getGame(String name) {

        Game game = gameCache.get(name);

        if (game == null) {
            throwResourceNotFoundException(name);
        }

        logger.info("Retrieved game: {}", game);

        return game;
    }

    @Override
    public List<Game> getAllGames() {

        List<Game> retrievedGames = new ArrayList<>(gameCache.values());

        logger.info("Retrieved game: {}", retrievedGames);

        return retrievedGames;
    }

    @Override
    public Game updateGame(Game game) {

        String currentName = game.getName();

        // Use compute to ensure atomicity
        return gameCache.compute(currentName, (key, existingGame) -> {

            if(!gameCache.containsKey(currentName)) {
                throwResourceNotFoundException(currentName);
            }

            // Check if an update is needed
            if (shouldSkipUpdate(game, existingGame)) {
                logger.info("Nothing to update. Game with the same data already exists: {}", existingGame);
                return existingGame;
            }

            // Update the game details
            updateGameDetails(game, existingGame);

            logger.info("Updated game: {}", existingGame);
            return existingGame;
        });
    }

    @Override
    public void deleteGame(String name) {

        Game existingGame = gameCache.remove(name);
        if (existingGame == null) {
            throwResourceNotFoundException(name);
        }

        logger.info("Deleted game: {}", existingGame);
    }

    private void throwResourceNotFoundException(String name) {
        logger.error("Game with name: {} not found.", name);
        throw new ResourceNotFoundException("Game with name: " + name + " not found.");
    }

    private boolean shouldSkipUpdate(Game game, Game gameToUpdate) {
        boolean isActiveUnchanged = Objects.equals(game.getIsActive(), gameToUpdate.getIsActive());

        boolean newNameIsNull = game.getNewName() == null;
        boolean newNameUnchanged = Objects.equals(game.getNewName(), gameToUpdate.getName());

        return isActiveUnchanged && (newNameIsNull || newNameUnchanged);
    }

    private void updateGameDetails(Game game, Game gameToUpdate) {

        gameToUpdate.setIsActive(game.getIsActive());
        gameToUpdate.setUpdateDate(LocalDateTime.now());

        String newName = game.getNewName();

        if (newName != null && !newName.equals(game.getName())) {
            if (newName.isEmpty()) {
                throw new ValidationException("New game name cannot be null or empty.");
            }
            handleNewNameUpdate(gameToUpdate, newName);
        }
    }

    private void validateGameName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Game name cannot be null or empty.");
        }
    }

    private void handleNewNameUpdate(Game gameToUpdate, String newName) {
        if (gameCache.containsKey(newName)) {
            throw new GameAlreadyExistsException("Game with name: " + newName + " already exists.");
        }
        gameCache.remove(gameToUpdate.getName());
        gameToUpdate.setName(newName);
        gameCache.put(newName, gameToUpdate);
    }
}