package com.example.project3.repository;

import com.example.project3.entity.Answer;
import com.example.project3.entity.Question;
import com.example.project3.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findBySubmission(Submission submission);

    List<Answer> findByQuestion(Question question);

    // dùng cho update đáp án
    Optional<Answer> findBySubmissionAndQuestion(
            Submission submission,
            Question question
    );
}
