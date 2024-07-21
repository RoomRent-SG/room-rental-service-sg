/*
package com.thiha.roomrent;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.thiha.roomrent.controller.AgentController;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;

import jakarta.transaction.Transactional;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
public class AgentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup(){

    }

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
*/
