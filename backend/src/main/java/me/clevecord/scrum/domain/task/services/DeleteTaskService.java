package me.clevecord.scrum.domain.task.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.task.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteTaskService {

    private final TaskRepository taskRepository;

    public boolean deleteTask(Task task) {
        try {
            taskRepository.deleteTaskById(task.getId());
            return true;
        } catch (Exception exc) {
            return false;
        }
    }
}
