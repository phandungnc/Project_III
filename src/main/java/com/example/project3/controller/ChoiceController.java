package com.example.project3.controller;
import com.example.project3.dto.Request.ChoiceRequest;
import com.example.project3.dto.Response.ChoiceResponse;
import com.example.project3.entity.Choice;
import com.example.project3.entity.User;
import com.example.project3.security.CustomUserDetails;
import com.example.project3.service.ChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ChoiceController {

    @Autowired
    private ChoiceService choiceService;

    // ===== STUDENT ENDPOINTS =====
    @GetMapping("/student/choices/question/{questionBankId}")
    public ResponseEntity<List<Choice>> getChoicesByQuestionForStudent(@PathVariable Integer questionBankId) {
        List<Choice> choices = choiceService.getChoicesByQuestion(questionBankId);
        return ResponseEntity.ok(choices);
    }

    // ===== TEACHER ENDPOINTS =====
    @PostMapping("/teacher/choices/question/{questionBankId}")
    public ResponseEntity<ChoiceResponse> createChoice(@PathVariable Integer questionBankId,
                                                       @RequestBody ChoiceRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Choice choice = new Choice();
        choice.setChoiceText(request.getChoiceText());
        choice.setIsCorrect(request.getIsCorrect());
        Choice created = choiceService.createChoice(questionBankId, choice, user);
        return ResponseEntity.status(201).body(new ChoiceResponse(created));
    }

    @PutMapping("/teacher/choices/{id}")
    public ResponseEntity<Choice> updateChoice(@PathVariable Integer id,
                                               @RequestBody Choice choice) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Choice updatedChoice = choiceService.updateChoice(id, choice, user);
        return ResponseEntity.ok(updatedChoice);
    }

    @DeleteMapping("/teacher/choices/{id}")
    public ResponseEntity<Void> deleteChoice(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        choiceService.deleteChoice(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teacher/choices/question/{questionBankId}")
    public ResponseEntity<List<Choice>> getChoicesByQuestion(@PathVariable Integer questionBankId) {
        List<Choice> choices = choiceService.getChoicesByQuestion(questionBankId);
        return ResponseEntity.ok(choices);
    }
}