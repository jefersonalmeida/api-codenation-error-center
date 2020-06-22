package com.error.center.entity;

import com.error.center.util.enums.Level;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Event extends Auditable<String> implements Serializable {

    private static final long serialVersionUID = -9065556819769322761L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 20)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Level level;

    @Column(nullable = false, length = 100)
    @NotNull()
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull()
    private String log;

    @Column(nullable = false, length = 100)
    @NotNull()
    private String origin;

    @Column(nullable = false)
    @NotNull()
    private Date date;

    @Column(nullable = false)
    @NotNull()
    private Integer quantity;

    public void increment() {
        this.quantity++;
    }
}
