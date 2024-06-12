package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.dentistry.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends JpaRepository<Service, String> {
}
