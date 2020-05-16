package me.clevecord.scrum.domain.board.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.board.repositories.BoardRepository;
import me.clevecord.scrum.domain.user.entities.User;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class CreateBoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(final String name, final String description, final User owner) {
        ZonedDateTime now = ZonedDateTime.now();
        Board board = Board.builder()
            .owner(owner)
            .name(name)
            .description(description)
            .createdAt(now)
            .updatedAt(now)
            .build();
        boardRepository.save(board);
        return board;
    }
}
