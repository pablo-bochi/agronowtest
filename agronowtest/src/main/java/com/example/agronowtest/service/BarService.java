package com.example.agronowtest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.agronowtest.exception.BadResourceException;
import com.example.agronowtest.exception.ResourceAlreadyExistsException;
import com.example.agronowtest.exception.ResourceNotFoundException;
import com.example.agronowtest.model.Bar;
import com.example.agronowtest.repository.BarRepository;

@Service
public class BarService {

	@Autowired
	private BarRepository barRepository;
	
	private boolean existsById(Long id) {
        return barRepository.existsById(id);
    }
	
	public Bar findById(Long id) throws ResourceNotFoundException {
        Bar bar = barRepository.findById(id).orElse(null);
        if (bar==null) {
            throw new ResourceNotFoundException("Cannot find bar with id: " + id);
        }
        else return bar;
    }
	
	public List<Bar> findAll(int pageNumber, int rowPerPage) {
        List<Bar> bars = new ArrayList<>();
        barRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPage)).forEach(bars::add);
        return bars;
    }
	
	public Bar save(Bar bar) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(bar.getName())) {
            if (bar.getId() != null && existsById(bar.getId())) { 
                throw new ResourceAlreadyExistsException("Bar with id: " + bar.getId() +
                        " already exists");
            }
            return barRepository.save(bar);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save bar");
            exc.addErrorMessage("bar is null or empty");
            throw exc;
        }
    }
	
	public void update(Bar bar) throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(bar.getName())) {
            if (!existsById(bar.getId())) {
                throw new ResourceNotFoundException("Cannot find bar with id: " + bar.getId());
            }
            barRepository.save(bar);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save bar");
            exc.addErrorMessage("Bar is null or empty");
            throw exc;
        }
    }
	
	public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Cannot find bar with id: " + id);
        }
        else {
            barRepository.deleteById(id);
        }
    }
	
}
