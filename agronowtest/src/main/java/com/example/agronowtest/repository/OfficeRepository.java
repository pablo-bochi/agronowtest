package com.example.agronowtest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.agronowtest.model.Office;

public interface OfficeRepository extends PagingAndSortingRepository<Office, Long> {}
