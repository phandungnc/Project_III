package com.example.project3.repository;

import com.example.project3.entity.EssayAnswer;
import com.example.project3.entity.Question;
import com.example.project3.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EssayAnswerRepository extends JpaRepository<EssayAnswer, Integer> {
    List<EssayAnswer> findBySubmission(Submission submission);
    List<EssayAnswer> findByQuestion(Question question);
}