package me.clevecord.scrum.domain.user.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "roles_enum")
    private RoleEnum role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
