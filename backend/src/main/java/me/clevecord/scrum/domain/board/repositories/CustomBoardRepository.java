package me.clevecord.scrum.domain.board.repositories;

import me.clevecord.scrum.domain.board.entities.Board;

import java.util.List;

public interface CustomBoardRepository {

    List<Board> getAllBoardsByUserIdAndPermission(int userId, String permission);
}
