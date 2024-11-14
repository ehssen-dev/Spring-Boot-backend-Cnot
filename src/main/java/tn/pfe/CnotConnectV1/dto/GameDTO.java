package tn.pfe.CnotConnectV1.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Long gameId;
    private String name;
    private LocalDate date;
    private String location;
    private String description;
    private LocalDate startGame;
    private LocalDate endGame;
    private Long eventId;
}