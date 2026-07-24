package com.campusconnect.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusconnect.entity.ProjectRequest;

@Repository
public interface ProjectRequestRepository 
        extends JpaRepository<ProjectRequest, Long> {

    List<ProjectRequest> findByProjectId(Long projectId);
    Optional<ProjectRequest> findByProjectIdAndStudentId(Long projectId, Long studentId);
}