package com.game.management.controller;

import com.game.management.exception.ValidationException;
import com.game.management.model.Game;
import com.game.management.model.GameDTO;
import com.game.management.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class for handling HTTP requests related to games.
 * This class defines RESTful endpoints for retrieving, creating, updating, and deleting games.
 *
 * @author Katarzyna Lesniak
 * @version 1.0
 * @since 2023-11-18
 */

@RestController
@RequestMapping("/games")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping()
    public ResponseEntity<GameDTO> createGame(@RequestBody @Valid Game game, BindingResult result) {

        checkValidationErrors(result);
        logRequest("create game", game);

        Game createdGame = gameService.createGame(game);
        GameDTO createdGameDTO = convertToGameDTO(createdGame);
        return ResponseEntity.ok().body(createdGameDTO);
    }

    @GetMapping("/{name}")
    public ResponseEntity<GameDTO> getGameByName(@PathVariable String name) {
        logRequest("get game by name", name);

        Game retrievedGame = gameService.getGame(name);
        GameDTO retrievedGameDTO = convertToGameDTO(retrievedGame);
        return ResponseEntity.ok().body(retrievedGameDTO);
    }

    @GetMapping()
    public ResponseEntity<List<GameDTO>> getAllGames() {
        logRequest("get all games");
        List<Game> retrievedGames = gameService.getAllGames();
        List<GameDTO> retrievedGameDTOS = retrievedGames.stream().map(this::convertToGameDTO).collect(Collectors.toList());
        return ResponseEntity.ok().body(retrievedGameDTOS);
    }

    @PutMapping()
    public ResponseEntity<GameDTO> updateGame(@RequestBody @Valid Game game, BindingResult result) {

        checkValidationErrors(result);
        logRequest("update game", game);
        Game updatedGame = gameService.updateGame(game);
        GameDTO updatedGameDTO = convertToGameDTO(updatedGame);
        return ResponseEntity.ok().body(updatedGameDTO);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteGame(@PathVariable String name) {

        logRequest("delete game by name", name);
        gameService.deleteGame(name);
        return ResponseEntity.ok().build();
    }

    private void checkValidationErrors(BindingResult result) {
        if (result != null && result.hasErrors()) {
            throw new ValidationException(result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", ")));
        }
    }

    private String extractRequesterIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRemoteAddr();
        }
        return "Unknown";
    }

    private void logRequest(String action, Object... requestData) {
        String requesterIp = extractRequesterIp();
        String logMessage = String.format("Request from IP %s to %s", requesterIp, action);
        if (requestData.length > 0) {
            logMessage += String.format(": %s", requestData[0]);
        }
        logger.info(logMessage);
    }
    private GameDTO convertToGameDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        BeanUtils.copyProperties(game, gameDTO);
        return gameDTO;
    }
}