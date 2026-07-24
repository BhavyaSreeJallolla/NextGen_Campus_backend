
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.campusconnect.entity.Connection;
//import com.campusconnect.enums.ConnectionStatus;
//
//@Repository
//public interface ConnectionRepository extends JpaRepository<Connection, Long> {
//
//    // Student connections
//    List<Connection> findByStudentId(Long studentId);
//
//    List<Connection> findByStudentIdAndStatus(
//            Long studentId,
//            ConnectionStatus status
//    );
//
//    // Alumni connections
//    List<Connection> findByAlumniId(Long alumniId);
//
//    List<Connection> findByAlumniIdAndStatus(
//            Long alumniId,
//            ConnectionStatus status
//    );
//
//    // Count connections
//    int countByStudentId(Long studentId);
//
//    int countByAlumniId(Long alumniId);
//
//    // Check accepted connection
//    boolean existsByStudentIdAndAlumniIdAndStatus(
//            Long studentId,
//            Long alumniId,
//            ConnectionStatus status
//    );
//}

package com.campusconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campusconnect.entity.Alumni;
import com.campusconnect.entity.Connection;
import com.campusconnect.entity.Student;
import com.campusconnect.enums.ConnectionStatus;

public interface ConnectionRepository
        extends JpaRepository<Connection, Long> {

    List<Connection> findByStudent(Student student);

    List<Connection> findByAlumni(Alumni alumni);
    boolean existsByStudentStudentIdAndAlumniAlumniIdAndStatus(
            Long studentId,
            Long alumniId,
            ConnectionStatus status
    );
}