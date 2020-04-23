package me.clevecord.scrum.domain.user.repositories;

import me.clevecord.scrum.domain.user.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    boolean existsByUsername(String username);
    User findByUsername(String username);
}
