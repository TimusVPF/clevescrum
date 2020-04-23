package me.clevecord.scrum.domain.user.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;
import me.clevecord.scrum.helpers.auth.AuthenticationHelper;
import me.clevecord.scrum.helpers.auth.annotations.RequiresAuth;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserAuthQueryResolver implements GraphQLQueryResolver {

    private final AuthenticationHelper helper;

    @RequiresAuth
    public CompletableFuture<User> getCurrentActiveUser(DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        return CompletableFuture.supplyAsync(() -> helper.getAuthenticatedUser(context.getHttpServletRequest()));
    }
}
