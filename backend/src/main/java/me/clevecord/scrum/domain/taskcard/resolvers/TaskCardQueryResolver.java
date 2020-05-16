package me.clevecord.scrum.domain.taskcard.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.board.exceptions.GQLInsufficientPermissionException;
import me.clevecord.scrum.domain.taskcard.exceptions.GQLTaskCardNotFoundException;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.taskcard.services.ReadTaskCardService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.AuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TaskCardQueryResolver implements GraphQLQueryResolver {

    private final AuthenticationHelper helper;
    private final ReadTaskCardService readTaskCardService;
    private final BoardPermissionHelper boardPermissionHelper;

    @RequiresAuth
    public CompletableFuture<List<TaskCard>> getTaskCards(int categoryId, DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        final User user = helper.getAuthenticatedUser(context.getHttpServletRequest());
        return CompletableFuture.supplyAsync(() -> {
            List<TaskCard> taskCards = readTaskCardService.findByCategoryId(categoryId);
            if (taskCards.isEmpty()) {
                return taskCards;
            }
            if (boardPermissionHelper.userHasPermission(user, taskCards.get(0), BoardPermissions.BOARD_READ, false)) {
                return taskCards;
            }
            throw new GQLInsufficientPermissionException("insufficient permission");
        });
    }

    @RequiresAuth
    public CompletableFuture<TaskCard> getTaskCardInfo(int taskCardId, DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        final User user = helper.getAuthenticatedUser(context.getHttpServletRequest());
        return CompletableFuture.supplyAsync(() -> {
            Optional<TaskCard> taskCardOptional = readTaskCardService.findById(taskCardId);
            if (taskCardOptional.isEmpty()) {
                throw new GQLTaskCardNotFoundException("task card not found");
            }
            TaskCard taskCard = taskCardOptional.get();
            if (boardPermissionHelper.userHasPermission(user, taskCard, BoardPermissions.BOARD_READ, false)) {
                return taskCard;
            }
            throw new GQLInsufficientPermissionException("insufficient permission");
        });
    }

}
