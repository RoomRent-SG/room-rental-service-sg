package com.thiha.roomrent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.thiha.roomrent.dto.AdminDto;
import com.thiha.roomrent.dto.AdminRegister;
import com.thiha.roomrent.enums.UserRole;
import com.thiha.roomrent.mapper.AdminMapper;
import com.thiha.roomrent.model.Admin;
import com.thiha.roomrent.repository.AdminRepository;
import com.thiha.roomrent.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AdminDto createAdmin(AdminRegister request) {
        Admin admin =  new Admin();
        admin.setUsername(request.getUsername());
        String hashPassword = passwordEncoder.encode(request.getPassword());
        admin.setPassword(hashPassword);
        admin.setRole(UserRole.ADMIN);
        Admin savedAdmin = adminRepository.save(admin);
        return AdminMapper.mapToAdminDtoFromAdmin(savedAdmin);
    }
    
}
