package me.clevecord.scrum.domain.task.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.task.repositories.TaskRepository;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.exceptions.GQLTaskCardNotFoundException;
import me.clevecord.scrum.domain.taskcard.services.ReadTaskCardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTaskService {

    private final ReadTaskCardService readTaskCardService;
    private final TaskRepository taskRepository;

    public Task createTask(TaskCard taskCard, String taskString, @Nullable Boolean finished) {
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime finishedAt = finished != null && finished ? now : null;
        final Task task = Task.builder()
            .card(taskCard)
            .task(taskString)
            .finishedAt(finishedAt)
            .createdAt(now)
            .updatedAt(now)
            .build();
        taskRepository.save(task);
        return task;
    }
}
