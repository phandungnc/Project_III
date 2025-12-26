package com.example.project3.service;

import com.example.project3.entity.*;
import com.example.project3.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GradingService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private EssayAnswerRepository essayAnswerRepository;

    public Submission gradeSubmission(Integer submissionId, User user) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Submission not found"));

        if (!submission.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Bạn không có quyền xem bài này");
        }

        float score = 0.0f;

        List<Answer> answers =
                answerRepository.findBySubmission(submission);

        for (Answer answer : answers) {

            Question question = answer.getQuestion();
            int type = question.getQuestionBank().getQuestionType();

            switch (type) {

                case 1: // MCQ
                case 3: // TRUE / FALSE
                    if (answer.getChosenChoice() != null &&
                            Boolean.TRUE.equals(
                                    answer.getChosenChoice().getIsCorrect())) {
                        score += 1.0f;
                    }
                    break;

                case 2: // multi
                    Set<Integer> correctIds =
                            choiceRepository
                                    .findByQuestionBankAndIsCorrectTrue(
                                            question.getQuestionBank())
                                    .stream()
                                    .map(Choice::getId)
                                    .collect(Collectors.toSet());

                    Set<Integer> selectedIds =
                            answer.getSelectedAnswerChoices()
                                    .stream()
                                    .map(ac -> ac.getChoice().getId())
                                    .collect(Collectors.toSet());

                    if (!selectedIds.isEmpty()
                            && selectedIds.equals(correctIds)) {
                        score += 1.0f;
                    }
                    break;

                case 4:
                    // TỰ LUẬN – CHẤM TAY
                    break;

                default:
                    throw new IllegalStateException("Unknown question type");
            }
        }

        submission.setScore(score);
        return submissionRepository.save(submission);
    }

    // chấm tự luận
    public EssayAnswer gradeEssayAnswer(
            Integer essayAnswerId,
            Float score,
            String userRole) {

        if (!userRole.equals("teacher")
                && !userRole.equals("admin")) {
            throw new SecurityException("Không có quyền chấm");
        }

        EssayAnswer essayAnswer =
                essayAnswerRepository.findById(essayAnswerId)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Essay answer not found"));

        essayAnswer.setScore(score);
        return essayAnswerRepository.save(essayAnswer);
    }
}
