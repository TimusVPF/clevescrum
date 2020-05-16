package me.clevecord.scrum.domain.board.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import me.clevecord.scrum.domain.board.repositories.BoardPermissionRepository;
import me.clevecord.scrum.domain.board.repositories.BoardRepository;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.PermissionHelper;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadBoardService {

    private final BoardRepository boardRepository;
    private final BoardPermissionRepository boardPermissionRepository;
    private final PermissionHelper permissionHelper;

    public Optional<Board> loadById(int id) {
        Optional<Board> boardOptional = boardRepository.findById(id);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            Hibernate.initialize(board.getCategories());
        }
        return boardOptional;
    }

    public List<Board> loadAllReadableBoards(User user) {
        if (permissionHelper.isAdmin(user)) {
            List<Board> board = new ArrayList<>();
            boardRepository.findAll()
                .forEach(board::add);
            return board;
        }
        List<Board> boards = boardRepository.findByOwnerId(user.getId());
        boards.addAll(boardRepository.getAllBoardsByUserIdAndPermission(user.getId(),
            BoardPermissions.BOARD_READ.toString()));
        boards.sort(Comparator.comparingInt(Board::getId));
        return boards;
    }
}
