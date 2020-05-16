package me.clevecord.scrum.domain.comments.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.comments.entities.CardComment;
import me.clevecord.scrum.domain.comments.exceptions.GQLCardCommentNotFoundException;
import me.clevecord.scrum.domain.comments.services.CreateCardCommentService;
import me.clevecord.scrum.domain.comments.services.ReadCardCommentService;
import me.clevecord.scrum.domain.comments.services.UpdateCardCommentService;
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
public class CardCommentMutationResolver implements GraphQLMutationResolver {

    private final GQLAuthenticationHelper authenticationHelper;
    private final BoardPermissionHelper permissionHelper;

    private final ReadTaskCardService readTaskCardService;
    private final ReadCardCommentService readCardCommentService;
    private final CreateCardCommentService createCardCommentService;
    private final UpdateCardCommentService updateCardCommentService;

    @RequiresAuth
    public CompletableFuture<CardComment> postComment(int cardId, String comment, DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);

        final Optional<TaskCard> taskCardOptional = readTaskCardService.findById(cardId);
        if (taskCardOptional.isEmpty()) {
            throw new GQLTaskCardNotFoundException("task card not found");
        }
        final TaskCard taskCard = taskCardOptional.get();
        permissionHelper.assertUserHasPermission(user, taskCard, BoardPermissions.COMMENTS_CREATE, false);

        return CompletableFuture.supplyAsync(() -> createCardCommentService.createComment(taskCard, comment));
    }

    @RequiresAuth
    public CompletableFuture<CardComment> updateComment(int commentId, String comment, DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);

        final Optional<CardComment> cardCommentOptional = readCardCommentService.findById(commentId);
        if (cardCommentOptional.isEmpty()) {
            throw new GQLCardCommentNotFoundException("comment not found");
        }
        final CardComment cardComment = cardCommentOptional.get();
        permissionHelper.assertUserHasPermission(user, cardComment, BoardPermissions.COMMENTS_UPDATE, false);

        return CompletableFuture.supplyAsync(() -> updateCardCommentService.updateComment(cardComment, comment));
    }

    @RequiresAuth
    public CompletableFuture<Boolean> deleteComment(int commentId, DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);

        final Optional<CardComment> cardCommentOptional = readCardCommentService.findById(commentId);
        if (cardCommentOptional.isEmpty()) {
            throw new GQLCardCommentNotFoundException("comment not found");
        }
        final CardComment cardComment = cardCommentOptional.get();
        permissionHelper.assertUserHasPermission(user, cardComment, BoardPermissions.COMMENTS_DELETE, false);

        return CompletableFuture.supplyAsync(() -> {
            try {
                updateCardCommentService.deleteComment(cardComment);
                return true;
            } catch (Exception exc) {
                return false;
            }
        });
    }
}
