package me.clevecord.scrum.domain.board.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.clevecord.scrum.domain.user.entities.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardPermissionKey implements Serializable {

    private static final long serialVersionUID = -4795677288652369456L;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "board_id", referencedColumnName = "id")
    protected Board board;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    protected User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", columnDefinition = "board_permissions_enum")
    private BoardPermissions permission;
}
