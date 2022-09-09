package com.task.githubTask.internal.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserReposDto {

    private List<UserRepos> userReposResponses;
}
