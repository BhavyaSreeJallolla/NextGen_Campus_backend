package com.campusconnect.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.campusconnect.entity.Alumni;
import com.campusconnect.entity.Student;
import com.campusconnect.service.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin("*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // =========================
    // CREATE STUDENT
    // =========================
    @PostMapping
    public Student saveStudent(@RequestBody Student student) {
        return studentService.saveStudent(student);
    }

    // =========================
    // GET STUDENT BY ID
    // =========================
    @GetMapping("/{studentId}")
    public Student getStudentById(@PathVariable Long studentId) {
        return studentService.getStudentById(studentId);
    }

    // =========================
    // GET ALL STUDENTS
    // =========================
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // =========================
    // UPDATE STUDENT
    // =========================
    @PutMapping("/{studentId}")
    public Student updateStudent(
            @PathVariable Long studentId,
            @RequestBody Student student) {

        return studentService.updateStudent(studentId, student);
    }

    // =========================
    // DELETE STUDENT
    // =========================
    @DeleteMapping("/{studentId}")
    public String deleteStudent(@PathVariable Long studentId) {

        studentService.deleteStudent(studentId);

        return "Student deleted successfully";
    }

    // =========================
    // DELETE MY PROFILE
    // =========================
    @DeleteMapping("/profile")
    public String deleteMyProfile(Authentication authentication) {

        studentService.deleteMyProfile(authentication.getName());

        return "Student account deleted successfully.";
    }

    // =========================
    // VIEW ALL ALUMNI
    // =========================
    @GetMapping("/alumni")
    public List<Alumni> getAllAlumni() {
        return studentService.getAllAlumni();
    }

    // =========================
    // SEARCH ALUMNI
    // =========================
    @GetMapping("/search")
    public List<Alumni> searchAlumni(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false) String role) {

        return studentService.searchAlumni(company, skills, role);
    }

    // =========================
    // UPLOAD PROFILE IMAGE
    // =========================
    @PostMapping("/{studentId}/upload-profile")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file.");
            }

            String folder = "uploads/profile/";
            Files.createDirectories(Paths.get(folder));

            String fileName =
                    System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(folder, fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Student student = studentService.getStudentById(studentId);

            student.setProfileImage("/uploads/profile/" + fileName);

            studentService.updateStudent(studentId, student);

            return ResponseEntity.ok("/uploads/profile/" + fileName);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Upload Failed : " + e.getMessage());
        }
    }

    // =========================
    // UPLOAD ID CARD
    // =========================
    @PostMapping("/{studentId}/upload-idcard")
    public ResponseEntity<String> uploadIdCard(
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file.");
            }

            String folder = "uploads/idcard/";
            Files.createDirectories(Paths.get(folder));

            String fileName =
                    System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(folder, fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Student student = studentService.getStudentById(studentId);

            student.setCollegeIdCard("/uploads/idcard/" + fileName);

            studentService.updateStudent(studentId, student);

            return ResponseEntity.ok("/uploads/idcard/" + fileName);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Upload Failed : " + e.getMessage());
        }
    }

    // =========================
    // GET STUDENT BY USER ID
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<Student> getStudentByUserId(@PathVariable Long userId) {

        Student student = studentService.getStudentByUserId(userId);

        return ResponseEntity.ok(student);
    }

}