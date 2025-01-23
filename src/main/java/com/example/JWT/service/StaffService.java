package com.example.JWT.service;

import com.example.JWT.entity.Staff;
import com.example.JWT.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Staff saveUser(Staff staff) {
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        staff.setRoles("USER");
        return staffRepository.save(staff);
    }

    public Optional<Staff> findByUsername(String username) {
        return staffRepository.findByUsername(username);
    }
}
