package me.clevecord.scrum.domain.category.entities;

import lombok.*;
import me.clevecord.scrum.domain.board.entities.Board;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board_categories")
public class BoardCategory implements Serializable {

    private static final long serialVersionUID = -6189501757512043922L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    private Board board;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "priority")
    private int priority;

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
        mappedBy = "category",
        orphanRemoval = true
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<TaskCard> tasks;

    @Override
    public String toString() {
        return "[BoardCategory " + id + "]";
    }
}
