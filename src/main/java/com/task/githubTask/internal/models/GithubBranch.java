package com.task.githubTask.internal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubBranch implements Serializable {
    private String name;
    private String lastCommitSha;
}
