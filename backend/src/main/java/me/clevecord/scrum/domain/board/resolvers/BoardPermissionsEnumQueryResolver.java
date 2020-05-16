package me.clevecord.scrum.domain.board.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import me.clevecord.scrum.domain.board.entities.BoardPermissions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BoardPermissionsEnumQueryResolver implements GraphQLQueryResolver {

    public List<BoardPermissions> getPermissionNames() {
        return Arrays.asList(BoardPermissions.values());
    }
}
