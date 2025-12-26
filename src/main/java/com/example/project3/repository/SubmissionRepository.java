package com.example.project3.repository;

import com.example.project3.entity.Exam;
import com.example.project3.entity.Submission;
import com.example.project3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    List<Submission> findByExam(Exam exam);
    List<Submission> findByUser(User user);
    List<Submission> findByExamAndUser(Exam exam, User user);
}