package com.dropandgo.backend.services;

import com.dropandgo.backend.entity.Admin;

public interface AdminService {
    Boolean isAdmin(String adminId, String adminPassword);
}
