package com.example.project3.repository;


import com.example.project3.entity.QuestionBank;
import com.example.project3.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank, Integer> {
    List<QuestionBank> findBySubject(Subject subject);
    List<QuestionBank> findBySubjectId(Long subjectId);
    List<QuestionBank> findByQuestionTypeAndDifficulty(Integer questionType, Integer difficulty);
    List<QuestionBank> findBySubjectIdAndDifficulty(Long subjectId, Integer difficulty);

}