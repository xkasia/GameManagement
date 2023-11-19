package com.game.management.controller;

import com.game.management.model.Game;
import com.game.management.model.GameDTO;
import com.game.management.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @Test
    void testGetGameByName() {
        String gameName = "TestGame";
        Game game = new Game(gameName, true);
        when(gameService.getGame(gameName)).thenReturn(game);

        ResponseEntity<GameDTO> responseEntity = gameController.getGameByName(gameName);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(gameName, responseEntity.getBody().getName());

        verify(gameService, times(1)).getGame(gameName);
    }

    @Test
    void testGetAllGames() {
        when(gameService.getAllGames()).thenReturn(Arrays.asList(new Game("Game1", true),
                new Game("Game2", false)));

        ResponseEntity<List<GameDTO>> responseEntity = gameController.getAllGames();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());

        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void testCreateGame() {
        Game game = new Game("TestGame", true);
        when(gameService.createGame(game)).thenReturn(new Game("TestGame", true));

        ResponseEntity<GameDTO> responseEntity = gameController.createGame(game, null);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("TestGame", responseEntity.getBody().getName());

        verify(gameService, times(1)).createGame(game);
    }

    @Test
    void testUpdateGame() {
        Game game = new Game("TestGame", true);
        when(gameService.updateGame(game)).thenReturn(new Game("TestGame", true));

        ResponseEntity<GameDTO> responseEntity = gameController.updateGame(game, null);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals("TestGame", responseEntity.getBody().getName());

        verify(gameService, times(1)).updateGame(game);
    }

    @Test
    void testDeleteGame() {
        String gameName = "TestGame";

        ResponseEntity<Void> responseEntity = gameController.deleteGame(gameName);

        assertEquals(200, responseEntity.getStatusCodeValue());

        verify(gameService, times(1)).deleteGame(gameName);
    }
}