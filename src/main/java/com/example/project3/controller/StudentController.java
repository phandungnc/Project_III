package com.example.project3.controller;
import com.example.project3.dto.Request.StudentUpdateRequest;
import com.example.project3.dto.Response.ApiResponse;
import com.example.project3.entity.Exam;
import com.example.project3.entity.SubjectStudent;
import com.example.project3.entity.User;
import com.example.project3.repository.ExamRepository;
import com.example.project3.security.CustomUserDetails;
import com.example.project3.service.ExamService;
import com.example.project3.service.SubjectService;
import com.example.project3.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final UserService userService;
    private final ExamService examService;
    private final SubjectService subjectService;

    @PutMapping("/update-info")
    public ResponseEntity<ApiResponse<String>> updateStudentInfo(@RequestBody @Valid StudentUpdateRequest request, Principal principal) {
        String username = principal.getName();
        userService.updateStudentInfo(username, request);
        ApiResponse<String> response = new ApiResponse<>(true, "Thông tin sinh viên đã được cập nhật.", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/subject/{subjectId}/exams")
    public ResponseEntity<ApiResponse<List<Exam>>> getExams(@PathVariable Long subjectId) {

        // Lấy thông tin user hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User student = userDetails.getUser();

        // Kiểm tra sinh viên có đăng ký môn này hay chưa
        SubjectStudent isEnrolled = subjectService.isStudentEnrolled(subjectId, student.getId());

        // Lấy danh sách bài thi
        List<Exam> exams = examService.getExamsBySubject(subjectId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy danh sách bài thi thành công.", exams)
        );
    }
}