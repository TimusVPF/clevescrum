package me.clevecord.scrum.domain.taskcard.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.repositories.TaskCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTaskCardService {

    private final TaskCardRepository taskCardRepository;

    public TaskCard createTaskCard(BoardCategory category, String title, String description) {
        ZonedDateTime now = ZonedDateTime.now();
        TaskCard card = TaskCard.builder()
            .category(category)
            .title(title)
            .description(description)
            .createdAt(now)
            .updatedAt(now)
            .build();

        taskCardRepository.save(card);
        return card;
    }
}
