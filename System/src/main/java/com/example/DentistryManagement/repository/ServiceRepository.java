package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Services, String> {

    List<Services> findAllByServiceIDNotNull();

    Services findByServiceID(String serviceID);



}