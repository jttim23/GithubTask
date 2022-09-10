package com.task.githubTask;

import com.task.githubTask.exceptions.ResponseNotCreatedException;
import com.task.githubTask.internal.models.Commit;
import com.task.githubTask.internal.models.GithubBranchResponse;
import com.task.githubTask.internal.models.GithubRepositoryResponse;
import com.task.githubTask.internal.models.UserReposDto;
import com.task.githubTask.internal.services.GithubConnectionService;
import com.task.githubTask.services.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GithubConnectionServiceTest {
    @Mock
    RestTemplate restTemplate;
    Map<String, String> map;
    GithubRepositoryResponse[] githubRepositoryResponses;
    GithubRepositoryResponse repositoryResponse;
    GithubBranchResponse[] githubBranchResponses;
    GithubBranchResponse branchResponse;

    @BeforeEach
    void setUp() {
        map = Map.of("login", "testUser");
        repositoryResponse = new GithubRepositoryResponse(
                "testUser", false, "\"https://api.github.com/repos/test/test/branches{/branch}\",", map);
        githubRepositoryResponses = new GithubRepositoryResponse[]{repositoryResponse};
        branchResponse = new GithubBranchResponse("master", new Commit("123"));
        githubBranchResponses = new GithubBranchResponse[]{branchResponse};
    }

    @Test
    void whenGivenUsername_whenRetrieveUserRepos_thenReturnsData() {
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.of(Optional.of(githubRepositoryResponses))
                , ResponseEntity.of(Optional.of(githubBranchResponses)));
        ConnectionService connectionService = new GithubConnectionService(restTemplate);
        List<UserReposDto> data = connectionService.retrieveUserData("wewdeeegrtdvd");

        assertNotNull(data);
        assertEquals("testUser", data.get(0).getOwnerLogin());
        assertEquals("master", data.get(0).getBranches().get(0).getName());

    }

    @Test
    void givenForkedRepo_whenRetrieveUserRepos_thenReturnsOnlyNotForked() {
        githubRepositoryResponses = new GithubRepositoryResponse[]{
                new GithubRepositoryResponse(
                        "testUser", true, "\"https://api.github.com/repos/test/test/branches{/branch}\",", map)
                , repositoryResponse};

        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.of(Optional.of(githubRepositoryResponses))
                , ResponseEntity.of(Optional.of(githubBranchResponses)));
        ConnectionService connectionService = new GithubConnectionService(restTemplate);
        List<UserReposDto> data = connectionService.retrieveUserData("wewdeeegrtdvd");

        assertNotNull(data);
        assertEquals(1, data.size());
    }

    @Test
    void givenWrongBranchResponse_whenGetBranches_thenResponseNotCreatedException() {
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.of(Optional.of(githubRepositoryResponses)),
                ResponseEntity.of(Optional.empty()));
        ConnectionService connectionService = new GithubConnectionService(restTemplate);

        assertThrows(ResponseNotCreatedException.class, () -> connectionService.retrieveUserData("testUser"));
    }

    @Test
    void givenWrongRepoResponse_whenGetRepos_thenResponseNotCreatedException() {
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.of(Optional.empty()));
        ConnectionService connectionService = new GithubConnectionService(restTemplate);

        assertThrows(ResponseNotCreatedException.class, () -> connectionService.retrieveUserData("testUser"));
    }


}
