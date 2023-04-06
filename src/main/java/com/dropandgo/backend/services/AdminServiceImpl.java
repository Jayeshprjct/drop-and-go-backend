package com.dropandgo.backend.services;

import com.dropandgo.backend.entity.Admin;
import com.dropandgo.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository repository;

    @Override
    public Boolean isAdmin(String adminId, String adminPassword) {
        Optional<Admin> mayBeAdmin = repository.findByAdminId(adminId);
        if (mayBeAdmin.isPresent()) {
            Admin admin = mayBeAdmin.get();
            return admin.getAdminPassword().equals(adminPassword);
        }
        return false;
    }
}
