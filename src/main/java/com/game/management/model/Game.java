package com.game.management.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a game entity.
 * This class is used to model game data within the application.
 */

@Getter
@Setter
public class Game {

    // unique value
    @NotNull(message = "Game name must be defined.")
    @NotBlank(message = "Game name cannot be empty.")
    private String name;
    private String newName; // New name for updates
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    @NotNull(message = "Game isActive field must be defined.")
    private Boolean isActive;

    public Game(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", isActive=" + isActive +
                '}';
    }
}