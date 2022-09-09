package com.task.githubTask.internal.services;

import com.task.githubTask.services.ConnectionService;
import com.task.githubTask.exceptions.ResponseNotCreatedException;
import com.task.githubTask.internal.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GithubConnectionService implements ConnectionService {
    public static final String GITHUB_BASE_API_URL = "https://api.github.com";
    public static final String LOGIN_KEY = "login";
    public static final String BRANCH_PATH = "{/branch}";
    private final RestTemplate restTemplate;

    public GithubConnectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static List<GithubBranch> mapBranchResponsesToBranches(GithubBranchResponse[] branchResponses) {
        return Arrays.stream(branchResponses)
                .map(br -> new GithubBranch(br.getName(), br.getCommit().getSha())).collect(Collectors.toList());
    }

    private static String createBranchesUrlFrom(String branchesUrl) {
        return branchesUrl.replace(BRANCH_PATH, "");
    }

    @Override
    public UserReposDto retrieveUserData(String username) {
        GithubRepositoryResponse[] githubResponses = getRepositoryResponsesFor(username);
        return new UserReposDto(getUserReposFrom(githubResponses));
    }

    private List<UserRepos> getUserReposFrom(GithubRepositoryResponse[] githubResponses) {
        return Arrays.stream(githubResponses)
                .filter(githubRepositoryResponse -> !githubRepositoryResponse.isForked())
                .map(this::createUserRepos)
                .toList();
    }

    private UserRepos createUserRepos(GithubRepositoryResponse response) {
        String branchesUrl = createBranchesUrlFrom(response.getBranchesUrl());

        GithubBranchResponse[] branchResponses = getAllBranchResponsesFrom(branchesUrl);

        List<GithubBranch> branches = mapBranchResponsesToBranches(branchResponses);

        return new UserRepos(response.getName(), response.getFork(), branches, response.getOwner().get(LOGIN_KEY));
    }

    private GithubBranchResponse[] getAllBranchResponsesFrom(String branchesUrl) {
        Optional<GithubBranchResponse[]> branchResponses = Optional.ofNullable(restTemplate
                .getForEntity(branchesUrl, GithubBranchResponse[].class)
                .getBody());
        return branchResponses.orElseThrow(ResponseNotCreatedException::new);
    }

    private GithubRepositoryResponse[] getRepositoryResponsesFor(String username) {
        Optional<GithubRepositoryResponse[]> githubRepositoryResponses = Optional.ofNullable(restTemplate
                .getForEntity(GITHUB_BASE_API_URL + "/users/" + username + "/repos", GithubRepositoryResponse[].class)
                .getBody());
        return githubRepositoryResponses.orElseThrow(ResponseNotCreatedException::new);
    }

}
