package me.clevecord.scrum.domain.taskcard.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.category.exceptions.GQLBoardCategoryNotFoundException;
import me.clevecord.scrum.domain.category.repositories.BoardCategoryRepository;
import me.clevecord.scrum.domain.category.services.ReadBoardCategoryService;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.repositories.TaskCardRepository;
import me.clevecord.scrum.domain.user.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTaskCardService {

    private final ReadBoardCategoryService readBoardCategoryService;
    private final TaskCardRepository repository;

    public TaskCard updateTaskCard(TaskCard taskCard, String title, String description) {
        taskCard.setTitle(title);
        taskCard.setDescription(description);
        return repository.save(taskCard);
    }

    public TaskCard moveCategory(TaskCard taskCard, int categoryId) {
        Optional<BoardCategory> boardCategoryOptional = readBoardCategoryService.findById(categoryId);
        if (boardCategoryOptional.isEmpty()) {
            throw new GQLBoardCategoryNotFoundException("board category not found");
        }
        BoardCategory boardCategory = boardCategoryOptional.get();
        taskCard.setCategory(boardCategory);
        return repository.save(taskCard);
    }

    public void joinCard(TaskCard taskCard, User user) {
        repository.joinCard(user.getId(), taskCard.getId());
    }
}
