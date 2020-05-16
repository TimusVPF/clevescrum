package me.clevecord.scrum.domain.taskcard.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.repositories.TaskCardRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadTaskCardService {

    private final TaskCardRepository taskCardRepository;

    public List<TaskCard> findByCategoryId(int categoryId) {
        List<TaskCard> tasks = taskCardRepository.findByCategoryId(categoryId);
        if (!tasks.isEmpty()) {
            TaskCard card = tasks.get(0);
            Hibernate.initialize(card.getCategory());

            BoardCategory category = card.getCategory();
            Hibernate.initialize(category.getBoard());
        }
        return tasks;
    }

    public Optional<TaskCard> findById(int taskCardId) {
        Optional<TaskCard> taskCardOptional = taskCardRepository.findById(taskCardId);
        if (taskCardOptional.isPresent()) {
            TaskCard taskCard = taskCardOptional.get();
            Hibernate.initialize(taskCard.getCategory());
            Hibernate.initialize(taskCard.getCategory().getBoard());
        }
        return taskCardOptional;
    }
}
