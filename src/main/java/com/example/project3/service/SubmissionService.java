package com.example.project3.service;

import com.example.project3.entity.*;
import com.example.project3.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubjectStudentRepository subjectStudentRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private GradingService gradingService;

    public Submission createSubmission(Integer examId, User user) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Exam not found with id: " + examId));

        Subject subject = exam.getSubject();

        boolean isStudentEnrolled =
                subjectStudentRepository
                        .findBySubjectIdAndStudentId(subject.getId(), user.getId())
                        .isPresent();

        if (!isStudentEnrolled) {
            throw new SecurityException("Bạn không có quyền làm bài môn này");
        }

        Submission submission = new Submission();
        submission.setExam(exam);
        submission.setUser(user);
        submission.setStatus(0); // chưa nộp

        return submissionRepository.save(submission);
    }

    // nộp va chấm
    public Submission submitSubmission(Integer submissionId, User user) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài nộp"));

        if (!submission.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền nộp bài này");
        }

        if (submission.getStatus() != null && submission.getStatus() == 1) {
            throw new RuntimeException("Bài thi đã được nộp trước đó");
        }

        submission.setSubmitTime(LocalDateTime.now());
        submission.setStatus(1);
        submissionRepository.save(submission);

        // chấm ngay
        return gradingService.gradeSubmission(submissionId, user);
    }

    public Submission getSubmissionById(Integer submissionId) {
        return submissionRepository.findById(submissionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Submission not found with id: " + submissionId));
    }

    public List<Submission> getSubmissionsByExam(Integer examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Exam not found with id: " + examId));
        return submissionRepository.findByExam(exam);
    }

    public List<Submission> getSubmissionsByUser(User user) {
        return submissionRepository.findByUser(user);
    }
}
