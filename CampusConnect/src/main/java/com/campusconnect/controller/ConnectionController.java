
//
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//
//    @PutMapping("/{id}/accept")
//    public Connection acceptRequest(
//            @PathVariable Long id) {
//
//
//    @PutMapping("/{id}/reject")
//    public Connection rejectRequest(
//            @PathVariable Long id) {
//
//
//}

 package com.campusconnect.controller;                                                                                                                                                                                                       

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.campusconnect.dto.ConnectionRequestDTO;
import com.campusconnect.entity.Connection;
import com.campusconnect.service.ConnectionService;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;
//
    @PostMapping("/request/{alumniId}")
    public Connection sendRequest(
            @PathVariable Long alumniId,
            @RequestParam Long studentId,
            @RequestParam String message) {

        return connectionService.sendRequest(
                studentId,
                alumniId,
                message);
    }

    @GetMapping("/student/{id}")
    public List<Connection> getStudentConnections(
            @PathVariable Long id) {

        return connectionService.getStudentConnections(id);
    }

    @GetMapping("/alumni/{id}")
    public List<Connection> getAlumniConnections(
            @PathVariable Long id) {

        return connectionService.getAlumniConnections(id);
    }

//    @PutMapping("/{id}/accept")
//    public String acceptRequest(
//            @PathVariable Long id) {
//    	 System.out.println("Accept API Hit");
//
////        return connectionService.acceptRequest(id);
//    	 return "SUCCESS";
//    	 
//    }
//
//    @PutMapping("/{id}/reject")
//    public Connection rejectRequest(
//            @PathVariable Long id) {
//
//        return connectionService.rejectRequest(id);
//    }
    @PutMapping("/accept")
    public Connection acceptRequest(@RequestBody ConnectionRequestDTO request) {

        System.out.println("Accept API Hit");

        return connectionService.acceptRequest(request);
    }

    @PutMapping("/reject")
    public Connection rejectRequest(@RequestBody ConnectionRequestDTO request) {

        return connectionService.rejectRequest(request);
    }
   
}