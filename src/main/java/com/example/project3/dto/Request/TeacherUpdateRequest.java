package com.example.project3.dto.Request;

import lombok.Data;

@Data
public class TeacherUpdateRequest {
    private String username;
    private String password;
    private String fullname;
    private String email;
}