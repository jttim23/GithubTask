package com.task.githubTask.services;

import com.task.githubTask.internal.models.UserReposDto;

import java.util.List;

public interface ConnectionService {

    List<UserReposDto> retrieveUserData(String username);

}
