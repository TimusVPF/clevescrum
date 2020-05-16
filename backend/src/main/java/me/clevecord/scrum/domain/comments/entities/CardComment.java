package me.clevecord.scrum.domain.comments.entities;

import lombok.*;
import me.clevecord.scrum.domain.taskcard.entities.TaskCard;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "card_comments")
public class CardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private TaskCard card;

    @Column(name = "comment")
    private String comment;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
}
