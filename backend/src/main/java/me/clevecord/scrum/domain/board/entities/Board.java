package me.clevecord.scrum.domain.board.entities;

import lombok.*;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.user.entities.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "boards")
public class Board implements Serializable {

    private static final long serialVersionUID = -6689843614411697493L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @Column(name = "board_name")
    private String name;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "board",
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<BoardCategory> categories;

    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "key.board",
        orphanRemoval = true
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<BoardPermission> permissions;

    @Override
    public String toString() {
        return "[Board " + id + "]";
    }
}
