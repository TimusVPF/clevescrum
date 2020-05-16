package me.clevecord.scrum.domain.category.repositories;

import me.clevecord.scrum.domain.category.entities.BoardCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCategoryRepository extends CrudRepository<BoardCategory, Integer> {

}
