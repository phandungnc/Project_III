package com.example.project3.repository;


import com.example.project3.entity.Exam;
import com.example.project3.entity.Question;
import com.example.project3.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByExam(Exam exam);
    List<Question> findByQuestionBank(QuestionBank questionBank);
    Optional<Question> findByExamIdAndQuestionBankId(Long examId, Long questionBankId);
    @Query("SELECT COUNT(q) > 0 FROM Question q WHERE q.id = :questionId AND q.exam.id = :examId")
    boolean existsByIdAndExamId(@Param("questionId") Integer questionId, @Param("examId") Long examId);


}