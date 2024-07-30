package com.thiha.roomrent.service;

import com.thiha.roomrent.dto.AdminDto;
import com.thiha.roomrent.dto.AdminRegister;

public interface AdminService {
    AdminDto createAdmin(AdminRegister request);
}
