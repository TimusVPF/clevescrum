package me.clevecord.scrum.domain.taskcard.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.repositories.TaskCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteTaskCardService {

    private final TaskCardRepository taskCardRepository;

    public boolean deleteById(int taskCardId) {
        Optional<TaskCard> taskCardOptional = taskCardRepository.findById(taskCardId);
        if (taskCardOptional.isEmpty()) {
            return false;
        }
        taskCardRepository.delete(taskCardOptional.get().getId());
        return true;
    }
}
