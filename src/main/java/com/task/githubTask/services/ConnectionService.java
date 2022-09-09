package com.task.githubTask.services;

import com.task.githubTask.internal.models.UserReposDto;

public interface ConnectionService {

    UserReposDto retrieveUserData(String username);


}
