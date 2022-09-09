package com.task.githubTask.internal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubBranchResponse implements Serializable {
    private String name;
    private Commit commit;
}
