package com.task.githubTask.controllers;

import com.task.githubTask.services.ConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GithubAppMainController.BASE_URL)
public class GithubAppMainController {
    public static final String BASE_URL = "/api/v1/repositories";
    private final ConnectionService connectionService;

    public GithubAppMainController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/{username}")
    ResponseEntity<?> getReposForUser(@PathVariable String username) {
        return new ResponseEntity<>(connectionService.retrieveUserData(username), HttpStatus.OK);
    }


}
