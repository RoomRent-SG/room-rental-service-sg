package com.thiha.roomrent;

import org.junit.jupiter.api.BeforeEach;

import com.thiha.roomrent.controller.AgentController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
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
