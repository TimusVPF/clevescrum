package me.clevecord.scrum.helpers.auth;

import graphql.schema.DataFetchingEnvironment;
import graphql.servlet.context.GraphQLServletContext;
import lombok.RequiredArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GQLAuthenticationHelper {

    private final AuthenticationHelper helper;

    public User getAuthenticatedUser(DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        return helper.getAuthenticatedUser(context.getHttpServletRequest());
    }
}
