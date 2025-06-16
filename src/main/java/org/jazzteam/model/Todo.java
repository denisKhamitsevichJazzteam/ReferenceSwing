package org.jazzteam.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "todos")
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    private LocalDate creationDate;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "sort_order")
    private Integer sortOrder;

}
