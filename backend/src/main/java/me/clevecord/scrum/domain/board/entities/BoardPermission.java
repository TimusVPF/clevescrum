package me.clevecord.scrum.domain.board.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_permissions")
public class BoardPermission implements Serializable {

    private static final long serialVersionUID = 7243309474104564070L;

    @EmbeddedId
    private BoardPermissionKey key;
}
