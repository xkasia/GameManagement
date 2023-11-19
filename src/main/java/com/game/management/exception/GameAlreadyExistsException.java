package com.game.management.exception;

public class GameAlreadyExistsException extends RuntimeException  {

    public GameAlreadyExistsException(String message) {
        super(message);
    }
}