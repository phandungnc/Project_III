package com.example.project3.controller;

import com.example.project3.entity.Answer;
import com.example.project3.entity.User;
import com.example.project3.security.CustomUserDetails;
import com.example.project3.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/student/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;
    // gửi đáp án cho từng câu hỏi
    @PostMapping("/submission/{submissionId}/question/{questionId}/choice/{choiceId}")
    public ResponseEntity<Answer> createAnswer(@PathVariable Integer submissionId,
                                               @PathVariable Integer questionId,
                                               @PathVariable Integer choiceId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Answer answer = answerService.createAnswer(submissionId, questionId, choiceId, user);
        return ResponseEntity.status(201).body(answer);
    }
    @PostMapping("/submission/{submissionId}/question/{questionId}/choices")
    public ResponseEntity<Answer> submitMultipleChoices(
            @PathVariable Integer submissionId,
            @PathVariable Integer questionId,
            @RequestBody List<Integer> choiceIds) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        Answer answer = answerService.createMultipleChoiceAnswer(
                submissionId, questionId, choiceIds, user);

        return ResponseEntity.ok(answer);
    }


}