package com.error.center.entity;

import com.error.center.util.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {

    private static final long serialVersionUID = -9065556819769322761L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(nullable = false)
    @NotNull()
    private String description;

    @Column(nullable = false)
    @NotNull()
    private String log;

    @Column(nullable = false)
    @NotNull()
    private String origin;

    @Column(nullable = false)
    @NotNull()
    private LocalDateTime date;

    @Column(nullable = false)
    @NotNull()
    private Integer quantity = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updated;
}
