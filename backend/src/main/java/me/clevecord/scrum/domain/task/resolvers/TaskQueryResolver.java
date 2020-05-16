package me.clevecord.scrum.domain.task.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.board.exceptions.GQLInsufficientPermissionException;
import me.clevecord.scrum.domain.task.exceptions.GQLTaskNotFoundException;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.task.services.ReadTaskService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.AuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TaskQueryResolver implements GraphQLQueryResolver {

    private final AuthenticationHelper helper;
    private final BoardPermissionHelper boardPermissionHelper;
    private final ReadTaskService readTaskService;

    @RequiresAuth
    public CompletableFuture<List<Task>> getTasks(int taskCardId, DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        final User user = helper.getAuthenticatedUser(context.getHttpServletRequest());
        return CompletableFuture.supplyAsync(() -> {
            List<Task> tasks = readTaskService.findByTaskCard(taskCardId);
            if (tasks.isEmpty()) {
                return tasks;
            }
            if (boardPermissionHelper.userHasPermission(user, tasks.get(0), BoardPermissions.BOARD_READ, false)) {
                return tasks;
            }
            throw new GQLInsufficientPermissionException("insufficient permission");
        });
    }

    @RequiresAuth
    public CompletableFuture<Task> getTaskInfo(int taskId, DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        final User user = helper.getAuthenticatedUser(context.getHttpServletRequest());
        return CompletableFuture.supplyAsync(() -> {
            Optional<Task> taskOptional = readTaskService.findById(taskId);
            if (taskOptional.isEmpty()) {
                throw new GQLTaskNotFoundException("task not found");
            }
            Task task = taskOptional.get();
            if (boardPermissionHelper.userHasPermission(user, task, BoardPermissions.BOARD_READ, false)) {
                return task;
            }
            throw new GQLInsufficientPermissionException("insufficient permission");
        });
    }
}
