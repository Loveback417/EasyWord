package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "total_word")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String word;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String meaning;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sentence;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sentenceMeaning;

    @Column(nullable = false, length = 20)
    private String kind;

    private Double difficulty;
}