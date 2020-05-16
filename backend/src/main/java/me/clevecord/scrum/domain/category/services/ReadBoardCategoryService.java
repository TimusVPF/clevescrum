package me.clevecord.scrum.domain.category.services;

import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.category.exceptions.GQLBoardCategoryNotFoundException;
import me.clevecord.scrum.domain.category.repositories.BoardCategoryRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadBoardCategoryService {

    private final BoardCategoryRepository boardCategoryRepository;

    public Optional<BoardCategory> findById(int id) {
        Optional<BoardCategory> boardCategoryOptional = boardCategoryRepository.findById(id);
        if (boardCategoryOptional.isEmpty()) {
            throw new GQLBoardCategoryNotFoundException("Board category with id " + id + " not found.");
        }
        BoardCategory boardCategory = boardCategoryOptional.get();
        Hibernate.initialize(boardCategory.getBoard());
        return boardCategoryOptional;
    }
}
