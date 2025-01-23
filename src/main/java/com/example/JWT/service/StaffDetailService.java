package com.example.JWT.service;


import com.example.JWT.entity.Staff;
import com.example.JWT.repository.StaffRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffDetailService implements UserDetailsService {

    private final StaffRepository staffRepository;

    public StaffDetailService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Staff> myUserOptional = staffRepository.findByUsername(username);
        if (myUserOptional.isPresent()) {
            Staff staff = myUserOptional.get();
            return User.builder()
                    .username(staff.getUsername())
                    .password(staff.getPassword())
                    .roles(staff.getRoles().split(","))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
