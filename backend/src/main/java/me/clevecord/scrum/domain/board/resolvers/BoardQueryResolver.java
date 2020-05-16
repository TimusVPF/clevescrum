package me.clevecord.scrum.domain.board.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.board.entities.BoardPermission;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.board.exceptions.GQLBoardNotFoundException;
import me.clevecord.scrum.domain.board.exceptions.GQLInsufficientPermissionException;
import me.clevecord.scrum.domain.board.services.ReadBoardService;
import me.clevecord.scrum.domain.user.entities.RoleEnum;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.entities.UserRole;
import me.clevecord.scrum.helpers.auth.AuthenticationHelper;
import me.clevecord.scrum.helpers.auth.PermissionHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class BoardQueryResolver implements GraphQLQueryResolver {

    private final AuthenticationHelper helper;
    private final ReadBoardService readBoardService;
    private final PermissionHelper permissionHelper;

    @RequiresAuth
    public CompletableFuture<List<Board>> getBoards(DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        User user = helper.getAuthenticatedUser(context.getHttpServletRequest());

        return CompletableFuture.supplyAsync(() -> readBoardService.loadAllReadableBoards(user));
    }

    @RequiresAuth
    public CompletableFuture<Board> getBoardInfo(int id, DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        User user = helper.getAuthenticatedUser(context.getHttpServletRequest());

        Optional<Board> boardOptional = readBoardService.loadById(id);
        if (boardOptional.isEmpty()) {
            throw new GQLBoardNotFoundException("Board not found.");
        }

        Board board = boardOptional.get();
        return CompletableFuture.supplyAsync(() -> {
            if (permissionHelper.isAdmin(user) || board.getOwner().getId() == user.getId()) {
                return board;
            }
            List<BoardPermission> boardPermissions = board.getPermissions();
            BoardPermissions requiredPermission = BoardPermissions.BOARD_READ;
            for (BoardPermission permission : boardPermissions) {
                if (permission.getKey()
                    .getUser()
                    .getId() == user.getId() &&
                    permission.getKey().getPermission() == requiredPermission) {
                    return board;
                }
            }
            throw new GQLInsufficientPermissionException("You do not have permission to access the requested board.");
        });
    }
}
