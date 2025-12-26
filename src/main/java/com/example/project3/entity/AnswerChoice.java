package com.example.project3.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "answer_choices")
@Data
public class AnswerChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    @JsonBackReference
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "choice_id", nullable = false)
    private Choice choice;
}
