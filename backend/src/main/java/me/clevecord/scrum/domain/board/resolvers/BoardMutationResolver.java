package me.clevecord.scrum.domain.board.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.board.entities.BoardPermission;
import me.clevecord.scrum.domain.board.entities.BoardPermissionKey;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.board.exceptions.GQLBoardNotFoundException;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.board.repositories.BoardPermissionRepository;
import me.clevecord.scrum.domain.board.repositories.BoardRepository;
import me.clevecord.scrum.domain.board.services.CreateBoardService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.domain.user.services.UserService;
import me.clevecord.scrum.errors.graphql.GQLGeneralError;
import me.clevecord.scrum.helpers.auth.GQLAuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class BoardMutationResolver implements GraphQLMutationResolver {

    private final BoardRepository boardRepository;
    private final BoardPermissionRepository boardPermissionRepository;
    private final BoardPermissionHelper boardPermissionHelper;
    private final CreateBoardService createBoardService;
    private final UserService userService;
    private final GQLAuthenticationHelper authenticationHelper;

    @RequiresAuth
    public CompletableFuture<Board> createBoard(String name, String description,
        DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        return CompletableFuture.supplyAsync(() -> createBoardService.createBoard(name, description, user));
    }

    @RequiresAuth
    public CompletableFuture<Board> updateBoard(int boardId, String name, String description,
        DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        final Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            throw new GQLBoardNotFoundException("board not found");
        }

        final Board board = boardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, board, BoardPermissions.BOARD_UPDATE, false);
        if (name != null) {
            board.setName(name);
        }
        if (description != null) {
            board.setDescription(description);
        }
        boardRepository.save(board);
        return CompletableFuture.supplyAsync(() -> board);
    }

    @RequiresAuth
    @Transactional
    public CompletableFuture<Boolean> addPermission(int boardId, int userId, String permission,
        DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        final Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            throw new GQLBoardNotFoundException("board not found");
        }
        final Optional<User> userOptional = userService.readById(userId);
        if (userOptional.isEmpty()) {
            throw new GQLGeneralError("user not found");
        }
        final User targetUser = userOptional.get();
        final Board board = boardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, board, BoardPermissions.BOARD_UPDATE, false);
        // TODO - Move this to a service, so Transactional belongs to a Service instead of Resolver
        try {
            boardPermissionRepository.createPermission(board.getId(), targetUser.getId(),
                BoardPermissions.valueOf(permission).toString());
        } catch (Exception e) {
            return CompletableFuture.supplyAsync(() -> false);
        }
        return CompletableFuture.supplyAsync(() -> true);
    }

    @RequiresAuth
    @Transactional
    public CompletableFuture<Boolean> removePermission(int boardId, int userId, String permission,
        DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        final Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            throw new GQLBoardNotFoundException("board not found");
        }
        final Optional<User> userOptional = userService.readById(userId);
        if (userOptional.isEmpty()) {
            throw new GQLGeneralError("user not found");
        }
        final User targetUser = userOptional.get();
        final Board board = boardOptional.get();
        boardPermissionHelper.assertUserHasPermission(user, board, BoardPermissions.BOARD_UPDATE, false);
        // TODO - Move this to a service, so Transactional belongs to a Service instead of Resolver
        try {
            boardPermissionRepository.deletePermission(board.getId(), targetUser.getId(),
                BoardPermissions.valueOf(permission).toString());
        } catch (Exception e) {
            return CompletableFuture.supplyAsync(() -> false);
        }
        return CompletableFuture.supplyAsync(() -> true);
    }
}
