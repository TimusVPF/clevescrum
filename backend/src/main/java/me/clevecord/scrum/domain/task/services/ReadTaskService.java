package me.clevecord.scrum.domain.task.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.exceptions.GQLTaskCardNotFoundException;
import me.clevecord.scrum.domain.taskcard.repositories.TaskCardRepository;
import me.clevecord.scrum.domain.task.repositories.TaskRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadTaskService {

    private final TaskCardRepository taskCardRepository;
    private final TaskRepository taskRepository;

    public List<Task> findByTaskCard(int taskCardId) {
        Optional<TaskCard> taskCardOptional = taskCardRepository.findById(taskCardId);
        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }

        TaskCard taskCard = taskCardOptional.get();
        Hibernate.initialize(taskCard.getCategory());
        Hibernate.initialize(taskCard.getCategory().getBoard());
        Hibernate.initialize(taskCard.getTasks());
        List<Task> tasks = taskCard.getTasks();
        tasks.sort(Comparator.comparingInt(Task::getId));
        return tasks;
    }

    public Optional<Task> findById(int taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();

            Hibernate.initialize(task.getCard());
            Hibernate.initialize(task.getCard().getCategory());
            Hibernate.initialize(task.getCard().getCategory().getBoard());
        }

        return taskOptional;
    }
}
