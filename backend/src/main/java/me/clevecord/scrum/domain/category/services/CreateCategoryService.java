package me.clevecord.scrum.domain.category.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.category.repositories.BoardCategoryRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class CreateCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    public BoardCategory createCategory(final Board board, final String name, final String description) {
        ZonedDateTime now = ZonedDateTime.now();

        BoardCategory category = BoardCategory.builder()
            .board(board)
            .name(name)
            .description(description)
            .createdAt(now)
            .updatedAt(now)
            .build();
        boardCategoryRepository.save(category);
        return category;
    }
}
