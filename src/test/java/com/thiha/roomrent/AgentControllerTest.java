package com.thiha.roomrent;

import org.junit.jupiter.api.BeforeEach;

import com.thiha.roomrent.controller.AgentController;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.thiha.roomrent.controller.AgentController;
import org.springframework.test.web.servlet.MockMvcBuilder;
import com.thiha.roomrent.dto.RoomPostRegisterDto;
import com.thiha.roomrent.service.AgentService;
import com.thiha.roomrent.service.RoomPostService;
import com.thiha.roomrent.validator.ObjectValidator;


@RunWith(MockitoJUnitRunner.class)
public class AgentControllerTest {
    @Mock
    private RoomPostService roomPostService;

    @Mock
    private AgentService agentService;

    @Mock
    private ObjectValidator<RoomPostRegisterDto> roomPostValidator;

    @InjectMocks
    private AgentController agentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRoomPost() {

    }
}
