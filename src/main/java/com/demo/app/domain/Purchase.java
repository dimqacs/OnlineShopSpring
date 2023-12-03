package com.demo.app.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "DEFAULT 'Not Delivered'")
    private String status;

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 0.0")
    private Float total;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Purchase() {
        this.status = "Not Delivered";
        this.total = 0.0f;
    }
}
