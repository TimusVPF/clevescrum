package me.clevecord.scrum.domain.task.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.task.exceptions.GQLTaskNotFoundException;
import me.clevecord.scrum.domain.task.services.CreateTaskService;
import me.clevecord.scrum.domain.task.services.DeleteTaskService;
import me.clevecord.scrum.domain.task.services.ReadTaskService;
import me.clevecord.scrum.domain.task.services.UpdateTaskService;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.exceptions.GQLTaskCardNotFoundException;
import me.clevecord.scrum.domain.taskcard.services.ReadTaskCardService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.GQLAuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TaskMutationResolver implements GraphQLMutationResolver {

    private final GQLAuthenticationHelper gqlAuthenticationHelper;
    private final BoardPermissionHelper boardPermissionHelper;

    private final ReadTaskCardService readTaskCardService;
    private final CreateTaskService createTaskService;
    private final DeleteTaskService deleteTaskService;
    private final ReadTaskService readTaskService;
    private final UpdateTaskService updateTaskService;

    @RequiresAuth
    public CompletableFuture<Task> createTask(int taskCardId, String taskString, Boolean finished,
        DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<TaskCard> taskCardOptional = readTaskCardService.findById(taskCardId);
        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }
        final TaskCard taskCard = taskCardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, taskCard, BoardPermissions.CARD_UPDATE, false);
        return CompletableFuture.supplyAsync(() -> createTaskService.createTask(taskCard, taskString, finished));
    }

    @RequiresAuth
    public CompletableFuture<Task> updateTask(int taskId, String taskString, Boolean finished,
        DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<Task> taskOptional = readTaskService.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }
        final Task task = taskOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, task, BoardPermissions.CARD_UPDATE, false);
        return CompletableFuture.supplyAsync(() -> updateTaskService.updateTask(task, taskString, finished));
    }

    @RequiresAuth
    public CompletableFuture<Boolean> deleteTask(int taskId, DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<Task> taskOptional = readTaskService.findById(taskId);
        if (taskOptional.isEmpty()) {
            throw new GQLTaskNotFoundException("task not found");
        }
        final Task task = taskOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, task, BoardPermissions.CARD_UPDATE, false);
        return CompletableFuture.supplyAsync(() -> deleteTaskService.deleteTask(task));
    }
}
