package com.task.githubTask.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.githubTask.internal.models.GithubBranch;
import com.task.githubTask.internal.models.UserReposDto;
import com.task.githubTask.services.ConnectionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GithubAppMainController.class)
class GithubAppMainControllerTest {
    @MockBean
    private ConnectionService connectionService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    List<UserReposDto> userReposDtos;

    @BeforeEach
    void setUp() {
        UserReposDto userReposDto = new UserReposDto("test", false, Collections.singletonList(new GithubBranch("testBranch", "123")), "owner");
        userReposDtos = Collections.singletonList(userReposDto);
    }

    @Test
    void givenCorrectUsername_whenGetUserRepos_theReturnsDataAndOK() throws Exception {
        when(connectionService.retrieveUserData(any())).thenReturn(userReposDtos);
        mockMvc.perform(get(GithubAppMainController.BASE_URL + "/owner").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }
    @Test
    void givenNotExistingUsername_whenGetUserRepos_theReturns404() throws Exception {
        when(connectionService.retrieveUserData(any())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND,"User not found"));

        mockMvc.perform(get(GithubAppMainController.BASE_URL + "/owner").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$", Matchers.aMapWithSize(3)));
    }

}