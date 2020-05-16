package me.clevecord.scrum.domain.board.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the enumeration that consists of the available
 * permissions on the database enum. As this is defined through
 * the database, using any other value outside it is going to
 * cause an error in the query as the value is enumerated (see
 * V4__create_boards_table.sql on CREATE TYPE board_permissions
 * to be sure). This class is to interface the values.
 */
@Getter
@AllArgsConstructor
public enum BoardPermissions {
    CARD_CREATE("CARD_CREATE", "Create a card", "Create a new card in a board."),
    CARD_UPDATE("CARD_UPDATE", "Update a card", "Update an existing card in a board."),
    CARD_DELETE("CARD_DELETE", "Delete a card", "Delete an existing card in a board."),
    CARD_JOIN("CARD_JOIN", "Join a card", "Join a task card in a board."),

    COMMENTS_CREATE("COMMENTS_CREATE", "Comment a card", "Post a comment on a task card."),
    COMMENTS_UPDATE("COMMENTS_UPDATE", "Update a comment", "Update own posted comments."),
    COMMENTS_DELETE("COMMENTS_DELETE", "Delete a comment", "Delete own posted comments."),

    CATEGORIES_CREATE("CATEGORIES_CREATE", "Create a category", "Creates a category in a board."),
    CATEGORIES_UPDATE("CATEGORIES_UPDATE", "Update a category", "Updates a category in a board."),
    CATEGORIES_DELETE("CATEGORIES_DELETE", "Delete a category", "Deletes a category in a board."),

    BOARD_READ("BOARD_READ", "Read a board", "Read board contents."),
    BOARD_UPDATE("BOARD_UPDATE", "Update board", "Update board root information.");

    private final String permissionName;
    private final String canonicalName;
    private final String description;

    @Override
    public String toString() {
        return permissionName;
    }
}
