package com.game.management.service;

import com.game.management.exception.GameAlreadyExistsException;
import com.game.management.exception.ResourceNotFoundException;
import com.game.management.model.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private ConcurrentHashMap<String, Game> gameCache;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void testCreateGame() {
        Game newGame = new Game("NewGame", true);

        Game createdGame = gameService.createGame(newGame);

        assertEquals("NewGame", createdGame.getName());
        assertTrue(createdGame.getIsActive());
    }

    @Test
    public void testGetGameWhenGameExists() {

        String gameName = "ExistingGame";
        Game existingGame = new Game(gameName, true);

        when(gameCache.get(gameName)).thenReturn(existingGame);

        Game retrievedGame = gameService.getGame(gameName);

        assertEquals(gameName, retrievedGame.getName());
        assertTrue(retrievedGame.getIsActive());
    }

    @Test
    public void testGetGameWhenGameDoesNotExist() {
        String nonExistingGameName = "NonExistingGame";

        assertThrows(ResourceNotFoundException.class, () -> {
            gameService.getGame(nonExistingGameName);
        });
    }

    @Test
    public void testGetAllGames() {
        List<Game> games = new ArrayList<>();
        games.add(new Game("Game1", true));
        games.add(new Game("Game2", false));

        when(gameCache.values()).thenReturn(games);

        List<Game> retrievedGames = gameService.getAllGames();

        assertEquals(2, retrievedGames.size());
        assertEquals("Game1", retrievedGames.get(0).getName());
        assertTrue(retrievedGames.get(0).getIsActive());
        assertEquals("Game2", retrievedGames.get(1).getName());
        assertFalse(retrievedGames.get(1).getIsActive());
    }

    @Test
    public void testUpdateGameStatus() {
        String existingGameName = "ExistingGame";
        Game existingGame = new Game(existingGameName, true);

        when(gameCache.containsKey(existingGameName)).thenReturn(true);

        when(gameCache.compute(eq(existingGameName), any(BiFunction.class)))
                .thenAnswer(invocation -> {
                    BiFunction<String, Game, Game> biFunction = invocation.getArgument(1);
                    return biFunction.apply(existingGameName, existingGame);
                });

        // Update the status of the existingGame to false
        existingGame.setIsActive(false);

        Game updatedGame = gameService.updateGame(existingGame);

        assertEquals(existingGameName, updatedGame.getName());
        assertFalse(updatedGame.getIsActive());
    }

    @Test
    public void testUpdateGameName() {
        String existingGameName = "ExistingGame";
        Game existingGame = new Game(existingGameName, true);

        when(gameCache.containsKey(existingGameName)).thenReturn(true);

        // Mock the behavior for computeIfPresent
        when(gameCache.compute(eq(existingGameName), any(BiFunction.class)))
                .thenAnswer(invocation -> {
                    BiFunction<String, Game, Game> biFunction = invocation.getArgument(1);
                    return biFunction.apply(existingGameName, existingGame);
                });

        // Update the status of the existingGame to false
        String newName = "ExistingGame2";
        existingGame.setNewName(newName);

        Game updatedGame = gameService.updateGame(existingGame);

        assertEquals(newName, updatedGame.getName());
    }

    @Test
    public void testDeleteGame() {
        String gameName = "GameToDelete";
        Game gameToDelete = new Game(gameName, true);

        when(gameCache.remove(gameName)).thenReturn(gameToDelete);

        gameService.deleteGame(gameName);

        verify(gameCache, times(1)).remove(gameName);
    }

    @Test
    public void testGameAlreadyExistsException() {
        String existingGameName = "ExistingGame";

        when(gameCache.putIfAbsent(eq(existingGameName), any(Game.class))).thenReturn(new Game(existingGameName, true));

        assertThrows(GameAlreadyExistsException.class, () -> {
            gameService.createGame(new Game(existingGameName, true));
        });
    }
}