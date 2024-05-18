package com.thiha.roomrent;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import jakarta.transaction.Transactional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource("classpath:application-test.properties")
public class AgentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @Transactional
    void shouldCreateANewAgent() throws Exception{
        String requestJson = "{\"email\": \"tester@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Cena\", \"phoneNumber\": \"093324342\", \"password\": \"default\", \"profilePhoto\": \"www.freephoto.gl\", \"createdAt\": \"11/11/2011\"}";

        ResultActions result = mockMvc.perform(post("/api/agent")
                                .contentType("application/json")
                                .content(requestJson));
        
        result.andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void shouldNotCreateANewAgent() throws Exception{
        String requestJson = "{\"email\": \"testMail@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Cena\", \"phoneNumber\": \"093324342\", \"password\": \"default\", \"profilePhoto\": \"www.freephoto.gl\", \"createdAt\": \"11/11/2011\"}";

        ResultActions resultOne = mockMvc.perform(post("/api/agent")
                                        .contentType("application/json")
                                        .content(requestJson));
        
        resultOne.andExpect(status().isCreated());

        ResultActions resultTwo = mockMvc.perform(post("/api/agent")
                                            .contentType("application/json")
                                            .content(requestJson));

        resultTwo.andExpect(status().isBadRequest());
                                        
    }
}
