package com.example.universitycrud.controller;

import com.example.universitycrud.model.Student;
import com.example.universitycrud.sevice.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/secured")
public class StudentController {
    private final StudentService SERVICE;


    @GetMapping("show_all")
    public List<Student> findAllStudents(Principal principal) {
        if (principal == null)
            return null;
        return SERVICE.findAllStudents();
    }

    @PostMapping("save_student")
    public Student saveStudent (@RequestBody Student student, Principal principal) {
        if (principal == null)
            return null;
        return SERVICE.saveStudent(student);
    }

    @GetMapping("student/{email}")
    public Student findByEmail (@PathVariable String email, Principal principal) {
        if (principal == null)
            return null;
        return SERVICE.findByEmail(email);
    }

    @PutMapping("update_student")
    public Student updateStudent (@RequestBody Student student, Principal principal) {
        if (principal == null)
            return null;
        return SERVICE.updateStudent(student);
    }

    @DeleteMapping("delete_student/{email}")
    public void deleteStudent (@PathVariable String email) {
        SERVICE.deleteStudent(email);
    }
}
