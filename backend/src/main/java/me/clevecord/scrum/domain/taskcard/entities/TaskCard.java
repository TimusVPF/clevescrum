package me.clevecord.scrum.domain.taskcard.entities;

import lombok.*;
import me.clevecord.scrum.domain.category.entities.BoardCategory;
import me.clevecord.scrum.domain.comments.entities.CardComment;
import me.clevecord.scrum.domain.task.entities.Task;
import me.clevecord.scrum.domain.user.entities.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "task_cards")
public class TaskCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private BoardCategory category;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "card",
        orphanRemoval = true
    )
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<Task> tasks;

    @OneToMany(
        cascade = CascadeType.ALL,
        mappedBy = "card",
        orphanRemoval = true
    )
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<CardComment> comments;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @ManyToMany(mappedBy = "tasksTaken")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<User> participants;
}
