/**
 * 
 */
package com.example.agronowtest.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.agronowtest.model.Bar;

/**
 * @author Pablo
 *
 */
public interface BarRepository extends PagingAndSortingRepository<Bar, Long> {

}
