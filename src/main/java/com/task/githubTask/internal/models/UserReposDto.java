package com.task.githubTask.internal.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserReposDto {
    private String name;
    private Boolean fork;
    private List<GithubBranch> branches;
    private String ownerLogin;
}
