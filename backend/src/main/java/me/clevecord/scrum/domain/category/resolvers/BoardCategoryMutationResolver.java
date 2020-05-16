package me.clevecord.scrum.domain.category.resolvers;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.category.exceptions.GQLBoardCategoryNotFoundException;
import me.clevecord.scrum.domain.board.exceptions.GQLBoardNotFoundException;
import me.clevecord.scrum.domain.board.helpers.BoardPermissionHelper;
import me.clevecord.scrum.domain.category.repositories.BoardCategoryRepository;
import me.clevecord.scrum.domain.board.repositories.BoardRepository;
import me.clevecord.scrum.domain.category.services.CreateCategoryService;
import me.clevecord.scrum.domain.category.services.ReadBoardCategoryService;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.GQLAuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class BoardCategoryMutationResolver implements GraphQLMutationResolver {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final ReadBoardCategoryService readBoardCategoryService;
    private final CreateCategoryService createCategoryService;
    private final GQLAuthenticationHelper authenticationHelper;
    private final BoardPermissionHelper permissionHelper;

    @RequiresAuth
    public CompletableFuture<BoardCategory> createCategory(int boardId, String name, String description, DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        final Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isEmpty()) {
            throw new GQLBoardNotFoundException("board not found");
        }
        final Board board = boardOptional.get();
        permissionHelper.assertUserHasPermission(user, board, BoardPermissions.CATEGORIES_CREATE, false);
        return CompletableFuture.supplyAsync(() -> createCategoryService.createCategory(board, name, description));
    }

    @RequiresAuth
    public CompletableFuture<BoardCategory> updateCategory(int categoryId, String name, String description, Integer priority,
        DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        final Optional<BoardCategory> categoryOptional = readBoardCategoryService.findById(categoryId);

        if (categoryOptional.isEmpty()) {
            throw new GQLBoardCategoryNotFoundException("category not found");
        }

        final BoardCategory category = categoryOptional.get();
        Hibernate.initialize(category.getBoard());
        permissionHelper.assertUserHasPermission(user, category, BoardPermissions.CATEGORIES_UPDATE, false);

        if (name != null) {
            category.setName(name);
        }
        if (description != null) {
            category.setDescription(description);
        }
        if (priority != null) {
            category.setPriority(priority);
        }
        boardCategoryRepository.save(category);

        return CompletableFuture.supplyAsync(() -> category);
    }

    @RequiresAuth
    public CompletableFuture<Boolean> deleteCategory(int categoryId, DataFetchingEnvironment env) {
        final User user = authenticationHelper.getAuthenticatedUser(env);
        final Optional<BoardCategory> categoryOptional = readBoardCategoryService.findById(categoryId);

        if (categoryOptional.isEmpty()) {
            throw new GQLBoardCategoryNotFoundException("category not found");
        }

        final BoardCategory category = categoryOptional.get();
        Hibernate.initialize(category.getBoard());
        permissionHelper.assertUserHasPermission(user, category, BoardPermissions.CATEGORIES_DELETE, false);

        return CompletableFuture.supplyAsync(() -> {
            try {
                boardCategoryRepository.delete(category);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
