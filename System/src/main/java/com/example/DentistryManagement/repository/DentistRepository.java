package com.example.DentistryManagement.repository;

import com.example.DentistryManagement.core.user.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DentistRepository extends JpaRepository<Dentist, String> {

//    List<Dentist> findAllBySta
}
