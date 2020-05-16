package me.clevecord.scrum.domain.task.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.task.exceptions.GQLTaskNotFoundException;
import me.clevecord.scrum.domain.task.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTaskService {

    private final TaskRepository taskRepository;

    public Task updateTask(@NonNull Task task, @Nullable String taskString, @Nullable Boolean finished) {
        ZonedDateTime finishedAt = task.getFinishedAt();
        if (finished != null) {
            /*
             * There are two conditions that need to be met for this:
             * 1. If finished at was never filled before, or is set to null,
             *    it means the task isn't finished, or was, for any reason
             *    set to `null`. This means that we should ONLY set it to now()
             *    when finished = true. If finished = false, no reason to set
             *    the finishedAt value, is there?
             * 2. Conversely, if it has been filled before, and if you receive
             *    finished = true, that just means nothing happens. Don't set
             *    the finishedAt with a new value! But, if it has been filled,
             *    and the finished = false, that means something went wrong, or
             *    the task isn't actually finished, so set it back to null.
             */
            if (finishedAt != null && !finished) {
                // If finished at was filled once, and you're setting
                // finished = false, then that means it's no longer finished.
                // Set it back to null.
                finishedAt = null;
            } else if (finishedAt == null && finished) {
                // If finished at has never been filled before or is
                // set to null (see previous condition), it means that
                // the task is declared "not finished", so now that it's
                // finished, set it back to now.
                finishedAt = ZonedDateTime.now();
            }
        }

        if (taskString != null) {
            task.setTask(taskString);
        }
        task.setFinishedAt(finishedAt);
        taskRepository.save(task);
        return task;
    }
}
