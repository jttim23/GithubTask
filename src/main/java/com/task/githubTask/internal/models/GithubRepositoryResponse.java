package com.task.githubTask.internal.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepositoryResponse implements Serializable {
    private String name;
    private Boolean fork;
    @JsonProperty("branches_url")
    private String branchesUrl;
    private Map<String, String> owner;

    public Boolean isForked() {
        return this.fork;
    }
}
