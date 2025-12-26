package com.example.project3.repository;


import com.example.project3.entity.Choice;
import com.example.project3.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChoiceRepository extends JpaRepository<Choice, Integer> {
    List<Choice> findByQuestionBank(QuestionBank questionBank);

    List<Choice> findByQuestionBankAndIsCorrectTrue(QuestionBank questionBank);
    long countByQuestionBankAndIsCorrectTrue(QuestionBank questionBank);

}