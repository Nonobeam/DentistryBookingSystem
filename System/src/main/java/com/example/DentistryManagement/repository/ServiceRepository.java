package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Services, String> {

    Services findByServiceID(String serviceID);

    @Query("SELECT s FROM Services s")
    List<Services> getAll();
}