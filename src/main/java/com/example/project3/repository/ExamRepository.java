package com.example.project3.repository;


import com.example.project3.entity.Exam;
import com.example.project3.entity.Subject;
import com.example.project3.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    List<Exam> findBySubject(Subject subject);
    List<Exam> findByCreatedBy(User createdBy);
}