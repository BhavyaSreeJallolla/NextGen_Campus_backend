package com.campusconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.campusconnect.dto.StatusUpdateRequest;
import com.campusconnect.dto.UserRequestDTO;

import com.campusconnect.entity.Alumni;
import com.campusconnect.entity.Student;
import com.campusconnect.entity.User;
import com.campusconnect.service.AdminService;
import com.campusconnect.service.AlumniService;
import com.campusconnect.service.StudentService;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AlumniService alumniService;

    @Autowired
    private StudentService studentService;


    @PutMapping("/users/{userId}/status")
    public User updateStatus(@PathVariable Long userId,
                             @RequestBody StatusUpdateRequest request) {

        return adminService.updateStatus(userId, request.getStatus());
    }


    // ===== User Management =====

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return adminService.getAllUsers();
    }

    @GetMapping("/pending-users")
    public List<UserRequestDTO> getPendingUsers() {

        return adminService.getPendingUsers();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {

        adminService.deleteUser(userId);
    }


    // ===== Alumni Management =====

    @GetMapping("/alumni")
    public List<Alumni> getAllAlumniForAdmin() {

        return alumniService.getAllAlumni();
    }

    @GetMapping("/alumni/{alumniId}")
    public Alumni getAlumniForAdmin(@PathVariable Long alumniId) {

        return alumniService.getAlumniById(alumniId);
    }

    @PutMapping("/alumni/{alumniId}")
    public Alumni updateAlumniForAdmin(@PathVariable Long alumniId,
                                        @RequestBody Alumni alumni) {

        return alumniService.updateAlumni(alumniId, alumni);
    }

    @DeleteMapping("/alumni/{alumniId}")
    public void deleteAlumniForAdmin(@PathVariable Long alumniId) {

        alumniService.deleteAlumni(alumniId);
    }


    // ===== Student Management =====

    @GetMapping("/students")
    public List<Student> getAllStudentsForAdmin() {

        return studentService.getAllStudents();
    }

    @GetMapping("/students/{studentId}")
    public Student getStudentForAdmin(@PathVariable Long studentId) {

        return studentService.getStudentById(studentId);
    }

    @PutMapping("/students/{studentId}")
    public Student updateStudentForAdmin(@PathVariable Long studentId,
                                          @RequestBody Student student) {

        return studentService.updateStudent(studentId, student);
    }

    @DeleteMapping("/students/{studentId}")
    public void deleteStudentForAdmin(@PathVariable Long studentId) {

        studentService.deleteStudent(studentId);
    }

}