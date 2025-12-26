package com.example.project3.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "answers")
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // MCQ / True False
    @ManyToOne
    @JoinColumn(name = "chosen_choice_id")
    private Choice chosenChoice;

    // MSQ
    @OneToMany(
            mappedBy = "answer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<AnswerChoice> selectedAnswerChoices = new ArrayList<>();

    public List<Choice> getSelectedChoices() {
        return selectedAnswerChoices.stream()
                .map(AnswerChoice::getChoice)
                .collect(Collectors.toList());
    }
}
