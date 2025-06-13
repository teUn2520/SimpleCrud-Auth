package com.example.universitycrud.sevice.impl;

import com.example.universitycrud.model.Student;
import com.example.universitycrud.repository.StudentRepository;
import com.example.universitycrud.sevice.StudentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
@Primary
public class StudentServiceImpl implements StudentService {
    private final StudentRepository REPOSITORY;

    @Override
    public List<Student> findAllStudents() {
        return REPOSITORY.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return REPOSITORY.save(student);
    }

    @Override
    public Student findByEmail(String email) {
        return REPOSITORY.findByEmail(email);
    }

    @Override
    public Student updateStudent(Student student) {
        return REPOSITORY.save(student);
    }

    @Override
    @Transactional
    public void deleteStudent(String email) {
        REPOSITORY.deleteByEmail(email);
    }
}
