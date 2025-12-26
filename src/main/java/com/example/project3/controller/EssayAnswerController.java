package com.example.project3.controller;

import com.example.project3.entity.EssayAnswer;
import com.example.project3.service.EssayAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/essay-answers")
public class EssayAnswerController {

    @Autowired
    private EssayAnswerService essayAnswerService;

    @PostMapping("/submission/{submissionId}/question/{questionId}")
    public ResponseEntity<EssayAnswer> createEssayAnswer(@PathVariable Integer submissionId,
                                                         @PathVariable Integer questionId,
                                                         @RequestBody String answerText,
                                                         @RequestHeader("X-User-Role") String userRole) {
        EssayAnswer essayAnswer = essayAnswerService.createEssayAnswer(submissionId, questionId, answerText, userRole);
        return ResponseEntity.status(201).body(essayAnswer);
    }
}