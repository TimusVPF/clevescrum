package me.clevecord.scrum.domain.user.entities;

import lombok.*;
import me.clevecord.scrum.domain.board.entities.Board;
// import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "owner",
        cascade = CascadeType.ALL
    )
    private List<Board> boards;

    @CreatedDate
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<UserRole> roles;

    @ManyToMany
    @JoinTable(
        name = "card_participants",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "card_id", referencedColumnName = "id")
    )
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<TaskCard> tasksTaken;
}
