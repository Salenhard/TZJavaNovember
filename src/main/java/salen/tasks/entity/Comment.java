package salen.tasks.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "comments")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String value;

    @OneToOne
    private User author;

    @ManyToOne
    private Task task;
}
