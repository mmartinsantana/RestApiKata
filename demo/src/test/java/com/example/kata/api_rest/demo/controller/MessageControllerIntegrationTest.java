package com.example.kata.api_rest.demo.controller;

import com.example.kata.api_rest.demo.message.Sender;
import com.example.kata.api_rest.demo.repository.PersonRepository;
import com.example.kata.api_rest.demo.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureJsonTesters
@WebMvcTest(MessageController.class)
@MockBean(Sender.class)
@MockBean(AccountService.class)
@MockBean(PersonRepository.class)
public class MessageControllerIntegrationTest {

    @MockBean
    private DataSource dataSource;

    @Autowired
    private MockMvc mockMvc;

    private final static long ACCOUNT_ID = 2;

    @Test
    @WithMockUser(roles = "REST")
    public void withdraw() throws Exception {
        // given
        String input = "test";

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(MessageController.PATH + MessageController.SUB_PATH_SEND)
                .accept(MediaType.APPLICATION_JSON)
                .content(input)
                .contentType(MediaType.APPLICATION_JSON);
        //.with(httpBasic("rest", "restPass"));


        MvcResult result = mockMvc
                .perform(requestBuilder)
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString())
                .isEqualTo(input);
    }

}
