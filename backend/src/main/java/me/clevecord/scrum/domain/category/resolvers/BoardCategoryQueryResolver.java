package me.clevecord.scrum.domain.category.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.board.exceptions.GQLInsufficientPermissionException;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.category.services.ReadBoardCategoryService;
import me.clevecord.scrum.domain.board.services.ReadBoardService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.AuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class BoardCategoryQueryResolver implements GraphQLQueryResolver {

    private final AuthenticationHelper helper;
    private final ReadBoardService readBoardService;
    private final ReadBoardCategoryService readBoardCategoryService;
    private final BoardPermissionHelper boardPermissionHelper;

    @RequiresAuth
    public CompletableFuture<List<BoardCategory>> getCategories(int boardId, DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        final User user = helper.getAuthenticatedUser(context.getHttpServletRequest());
        return CompletableFuture.supplyAsync(() -> {
            Optional<Board> boardOptional = readBoardService.loadById(boardId);
            if (boardOptional.isEmpty()) {
                return new ArrayList<>();
            }

            Board board = boardOptional.get();
            if (boardPermissionHelper.userHasPermission(user, board, BoardPermissions.BOARD_READ, false)) {
                return board.getCategories();
            }
            throw new GQLInsufficientPermissionException("You do not have permission to look at this board's categories.");
        });
    }

    @RequiresAuth
    public CompletableFuture<BoardCategory> getCategoryInfo(int categoryId, DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        final User user = helper.getAuthenticatedUser(context.getHttpServletRequest());
        return CompletableFuture.supplyAsync(() -> {
            Optional<BoardCategory> boardCategoryOptional = readBoardCategoryService.findById(categoryId);

            if (boardCategoryOptional.isEmpty()) {
                return null;
            }

            if (boardPermissionHelper.userHasPermission(user, boardCategoryOptional.get().getBoard(),
                BoardPermissions.BOARD_READ, false)) {
                return boardCategoryOptional.get();
            }
            throw new GQLInsufficientPermissionException("You do not have permission to get this category's information");
        });
    }
}
