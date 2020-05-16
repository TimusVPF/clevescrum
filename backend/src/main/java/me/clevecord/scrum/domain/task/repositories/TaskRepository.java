package me.clevecord.scrum.domain.task.repositories;

import me.clevecord.scrum.domain.task.entities.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Modifying
    @Query(value = "DELETE FROM tasks WHERE id=:id", nativeQuery = true)
    void deleteTaskById(@Param("id") int taskId);
}
