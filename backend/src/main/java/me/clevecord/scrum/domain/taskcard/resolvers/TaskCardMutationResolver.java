package me.clevecord.scrum.domain.taskcard.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.category.exceptions.GQLBoardCategoryNotFoundException;
import me.clevecord.scrum.domain.taskcard.exceptions.GQLTaskCardNotFoundException;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.taskcard.services.CreateTaskCardService;
import me.clevecord.scrum.domain.category.services.ReadBoardCategoryService;
import me.clevecord.scrum.domain.taskcard.services.DeleteTaskCardService;
import me.clevecord.scrum.domain.taskcard.services.ReadTaskCardService;
import me.clevecord.scrum.domain.taskcard.services.UpdateTaskCardService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.GQLAuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TaskCardMutationResolver implements GraphQLMutationResolver {

    private final BoardPermissionHelper boardPermissionHelper;
    private final GQLAuthenticationHelper gqlAuthenticationHelper;
    private final ReadBoardCategoryService readBoardCategoryService;

    private final CreateTaskCardService createTaskCardService;
    private final ReadTaskCardService readTaskCardService;
    private final UpdateTaskCardService updateTaskCardService;
    private final DeleteTaskCardService deleteTaskCardService;

    @RequiresAuth
    public CompletableFuture<TaskCard> createTaskCard(int categoryId, String title, String description,
        DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<BoardCategory> boardCategoryOptional = readBoardCategoryService.findById(categoryId);
        if (boardCategoryOptional.isEmpty()) {
            throw new GQLBoardCategoryNotFoundException("board category not found");
        }

        BoardCategory category = boardCategoryOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, category, BoardPermissions.CARD_CREATE, false);

        return CompletableFuture.supplyAsync(() -> createTaskCardService.createTaskCard(category, title, description));
    }

    @RequiresAuth
    public CompletableFuture<TaskCard> updateTaskCard(int taskCardId, String title, String description,
        DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<TaskCard> taskCardOptional = readTaskCardService.findById(taskCardId);
        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }

        TaskCard taskCard = taskCardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, taskCard, BoardPermissions.CARD_UPDATE, false);

        return CompletableFuture.supplyAsync(() -> updateTaskCardService.updateTaskCard(taskCard, title, description));
    }

    @RequiresAuth
    public CompletableFuture<Boolean> deleteTaskCard(int taskCardId, DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<TaskCard> taskCardOptional = readTaskCardService.findById(taskCardId);
        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }

        TaskCard taskCard = taskCardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, taskCard, BoardPermissions.CARD_DELETE, false);

        return CompletableFuture.supplyAsync(() -> deleteTaskCardService.deleteById(taskCardId));
    }

    @RequiresAuth
    public CompletableFuture<Boolean> joinCard(int taskCardId, DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<TaskCard> taskCardOptional = readTaskCardService.findById(taskCardId);
        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }

        TaskCard taskCard = taskCardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, taskCard, BoardPermissions.CARD_JOIN, false);

        return CompletableFuture.supplyAsync(() -> {
            updateTaskCardService.joinCard(taskCard, user);
            return true;
        });
    }

    @RequiresAuth
    public CompletableFuture<TaskCard> moveCardCategory(int taskCardId, int newCategoryId, DataFetchingEnvironment env) {
        final User user = gqlAuthenticationHelper.getAuthenticatedUser(env);
        final Optional<TaskCard> taskCardOptional = readTaskCardService.findById(taskCardId);

        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }

        TaskCard taskCard = taskCardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, taskCard, BoardPermissions.CARD_UPDATE, false);

        return CompletableFuture.supplyAsync(() -> updateTaskCardService.moveCategory(taskCard, newCategoryId));
    }
}
