package com.example.nikfrvSpring.config;

import com.example.nikfrvSpring.entity.Role;
import com.example.nikfrvSpring.repository.RoleRepository;
import com.example.nikfrvSpring.util.RoleConstants;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer {
    @Autowired
    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles(){
        Role userRole = new Role();
        userRole.setRoleName(RoleConstants.ROLE_USER);

        Role adminRole = new Role();
        adminRole.setRoleName(RoleConstants.ROLE_ADMIN);

        if (roleRepository.findByRoleName(RoleConstants.ROLE_USER).isEmpty()){
            roleRepository.save(userRole);
        }

        if (roleRepository.findByRoleName(RoleConstants.ROLE_ADMIN).isEmpty()){
            roleRepository.save(adminRole);
        }

        System.out.println("Default roles initialized");
    }
}
