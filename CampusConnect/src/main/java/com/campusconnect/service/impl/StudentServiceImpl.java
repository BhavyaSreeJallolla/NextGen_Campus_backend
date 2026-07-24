package com.campusconnect.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.campusconnect.entity.Student;
import com.campusconnect.entity.User;
import com.campusconnect.entity.Alumni;

import com.campusconnect.repository.StudentRepository;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.repository.AlumniRepository;

import com.campusconnect.service.StudentService;



@Service
public class StudentServiceImpl implements StudentService {



    @Autowired
    private StudentRepository studentRepository;


    @Autowired
    private AlumniRepository alumniRepository;


    @Autowired
    private UserRepository userRepository;





    @Override
    public Student saveStudent(Student student) {

        User user = userRepository.findById(student.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        student.setUser(user);

        return studentRepository.save(student);
    }





    @Override
    public Student getStudentById(Long studentId){

        return studentRepository.findById(studentId)
                .orElseThrow(
                () -> new RuntimeException("Student not found"));
    }





    @Override
    public List<Student> getAllStudents(){

        return studentRepository.findAll();
    }





 // UPDATE STUDENT PROFILE

    @Override
    public Student updateStudent(Long studentId, Student student) {

        Student existing = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getRollNumber() != null)
            existing.setRollNumber(student.getRollNumber());

        if (student.getCollegeName() != null)
            existing.setCollegeName(student.getCollegeName());

        if (student.getBranch() != null)
            existing.setBranch(student.getBranch());

        if (student.getYear() != null)
            existing.setYear(student.getYear());

        if (student.getPhone() != null)
            existing.setPhone(student.getPhone());

        if (student.getBio() != null)
            existing.setBio(student.getBio());

        if (student.getSkills() != null)
            existing.setSkills(student.getSkills());

        if (student.getGithubUrl() != null)
            existing.setGithubUrl(student.getGithubUrl());

        if (student.getLinkedinUrl() != null)
            existing.setLinkedinUrl(student.getLinkedinUrl());

        if (student.getProfileImage() != null)
            existing.setProfileImage(student.getProfileImage());

        if (student.getCollegeIdCard() != null)
            existing.setCollegeIdCard(student.getCollegeIdCard());

        return studentRepository.save(existing);
    }



    @Override
    public void deleteStudent(Long studentId){

        studentRepository.deleteById(studentId);

    }







    // VIEW ALL ALUMNI

    @Override
    public List<Alumni> getAllAlumni(){

        return alumniRepository.findAll();

    }







    // SEARCH ALUMNI

    @Override
    public List<Alumni> searchAlumni(
            String company,
            String skills,
            String role){



        if(company != null && !company.isEmpty()){

            return alumniRepository
            .findByCompanyNameContainingIgnoreCase(company);

        }



        if(skills != null && !skills.isEmpty()){

            return alumniRepository
            .findByExpertiseContainingIgnoreCase(skills);

        }



        if(role != null && !role.isEmpty()){

            return alumniRepository
            .findByDesignationContainingIgnoreCase(role);

        }



        return alumniRepository.findAll();

    }







    // DELETE OWN PROFILE

    @Override
    public void deleteMyProfile(String email) {


        User user = userRepository.findByEmail(email)
                .orElseThrow(
                () -> new RuntimeException("User not found"));



        Student student = studentRepository.findByUser(user)
                .orElseThrow(
                () -> new RuntimeException("Student profile not found"));



        studentRepository.delete(student);


        userRepository.delete(user);

    }







    // UPDATE OWN PROFILE USING EMAIL

    @Override
    public Student updateMyProfile(
            String email,
            Student student) {



        User user = userRepository.findByEmail(email)
                .orElseThrow(
                () -> new RuntimeException("User not found"));



        Student existingStudent =
                studentRepository.findByUser(user)
                .orElseThrow(
                () -> new RuntimeException("Student profile not found"));





        if(student.getRollNumber()!=null)
            existingStudent.setRollNumber(
                    student.getRollNumber());



        if(student.getCollegeName()!=null)
            existingStudent.setCollegeName(
                    student.getCollegeName());



        if(student.getBranch()!=null)
            existingStudent.setBranch(
                    student.getBranch());



        if(student.getYear()!=null)
            existingStudent.setYear(
                    student.getYear());



        if(student.getPhone()!=null)
            existingStudent.setPhone(
                    student.getPhone());



        if(student.getBio()!=null)
            existingStudent.setBio(
                    student.getBio());



        if(student.getSkills()!=null)
            existingStudent.setSkills(
                    student.getSkills());



        if(student.getGithubUrl()!=null)
            existingStudent.setGithubUrl(
                    student.getGithubUrl());



        if(student.getLinkedinUrl()!=null)
            existingStudent.setLinkedinUrl(
                    student.getLinkedinUrl());



        if(student.getProfileImage()!=null)
            existingStudent.setProfileImage(
                    student.getProfileImage());



        if(student.getCollegeIdCard()!=null)
            existingStudent.setCollegeIdCard(
                    student.getCollegeIdCard());



        return studentRepository.save(existingStudent);

    }
    @Override
    public Student getStudentByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return studentRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student profile not found"));
    }
    


}