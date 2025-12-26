package com.example.project3.service;

import com.example.project3.entity.*;
import com.example.project3.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    // ===== MSQ (submit 1 lần nhiều đáp án) =====
    public Answer createMultipleChoiceAnswer(
            Integer submissionId,
            Integer questionId,
            List<Integer> choiceIds,
            User user) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));

        if (!submission.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Không có quyền");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        Answer answer = answerRepository
                .findBySubmissionAndQuestion(submission, question)
                .orElseGet(() -> {
                    Answer a = new Answer();
                    a.setSubmission(submission);
                    a.setQuestion(question);
                    return a;
                });

        answer.setChosenChoice(null);
        answer.getSelectedAnswerChoices().clear();

        for (Integer cid : choiceIds) {
            Choice choice = choiceRepository.findById(cid)
                    .orElseThrow(() -> new EntityNotFoundException("Choice not found"));

            AnswerChoice ac = new AnswerChoice();
            ac.setAnswer(answer);
            ac.setChoice(choice);

            answer.getSelectedAnswerChoices().add(ac);
        }

        return answerRepository.save(answer);
    }

    // ===== MCQ / MSQ click từng đáp án =====
    public Answer createAnswer(Integer submissionId,
                               Integer questionId,
                               Integer choiceId,
                               User user) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new EntityNotFoundException("Submission not found"));

        if (!submission.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Không có quyền");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));

        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new EntityNotFoundException("Choice not found"));

        Answer answer = answerRepository
                .findBySubmissionAndQuestion(submission, question)
                .orElseGet(() -> {
                    Answer a = new Answer();
                    a.setSubmission(submission);
                    a.setQuestion(question);
                    return a;
                });

        int type = question.getQuestionBank().getQuestionType();

        switch (type) {
            case 1: // MCQ
            case 3: // True/False
                answer.setChosenChoice(choice);
                answer.getSelectedAnswerChoices().clear();
                break;

            case 2: // MSQ
                answer.setChosenChoice(null);

                Iterator<AnswerChoice> it = answer.getSelectedAnswerChoices().iterator();
                boolean removed = false;

                while (it.hasNext()) {
                    AnswerChoice ac = it.next();
                    if (ac.getChoice().getId().equals(choice.getId())) {
                        it.remove();
                        removed = true;
                        break;
                    }
                }

                if (!removed) {
                    AnswerChoice ac = new AnswerChoice();
                    ac.setAnswer(answer);
                    ac.setChoice(choice);
                    answer.getSelectedAnswerChoices().add(ac);
                }
                break;
        }

        return answerRepository.save(answer);
    }
}
