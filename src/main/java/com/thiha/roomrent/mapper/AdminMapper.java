package com.thiha.roomrent.mapper;

import com.thiha.roomrent.dto.AdminDto;
import com.thiha.roomrent.model.Admin;

public class AdminMapper {
    public static AdminDto mapToAdminDtoFromAdmin(Admin admin){
        AdminDto adminDto = new AdminDto();
        adminDto.setId(admin.getId());
        adminDto.setUsername(admin.getUsername());
        adminDto.setRole(admin.getRole().toString());
        return adminDto;
    }
}
