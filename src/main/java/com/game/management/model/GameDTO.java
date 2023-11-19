package com.game.management.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a game.
 * This class is used to transfer game data between layers of the application.
 * It is used to represent custom views of the game data.
 */

@Getter
@Setter
public class GameDTO {
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    private Boolean isActive;

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