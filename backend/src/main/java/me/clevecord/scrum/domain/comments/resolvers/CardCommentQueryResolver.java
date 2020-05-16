package me.clevecord.scrum.domain.comments.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.comments.entities.CardComment;
import me.clevecord.scrum.domain.comments.services.ReadCardCommentService;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.exceptions.GQLTaskCardNotFoundException;
import me.clevecord.scrum.domain.taskcard.services.ReadTaskCardService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.GQLAuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class CardCommentQueryResolver implements GraphQLQueryResolver {

    private final GQLAuthenticationHelper authenticationHelper;
    private final BoardPermissionHelper permissionHelper;

    private final ReadCardCommentService readCardCommentService;
    private final ReadTaskCardService readTaskCardService;

    @RequiresAuth
    public CompletableFuture<List<CardComment>> getComments(int taskCardId, DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        final Optional<TaskCard> taskCardOptional = readTaskCardService.findById(taskCardId);
        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }
        final TaskCard taskCard = taskCardOptional.get();
        permissionHelper.assertUserHasPermission(user, taskCard, BoardPermissions.BOARD_READ, false);
        return CompletableFuture.supplyAsync(() -> readCardCommentService.getByTaskCardId(taskCardId));
    }
}
