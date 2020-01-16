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
import com.example.agronowtest.model.Office;
import com.example.agronowtest.repository.OfficeRepository;

@Service
public class OfficeService {

	@Autowired
	private OfficeRepository officeRepository;
	
	private boolean existsById(Long id) {
        return officeRepository.existsById(id);
    }
	
	public Office findById(Long id) throws ResourceNotFoundException {
		Office office = officeRepository.findById(id).orElse(null);
        if (office==null) {
            throw new ResourceNotFoundException("Cannot find office with id: " + id);
        }
        else return office;
    }
	
	public List<Office> findAll(int pageNumber, int rowPerPage) {
        List<Office> offices = new ArrayList<>();
        officeRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPage)).forEach(offices::add);
        return offices;
    }
	
	public Office save(Office office) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(office.getOfficeName())) {
            if (office.getOfficeId() != null && existsById(office.getOfficeId())) { 
                throw new ResourceAlreadyExistsException("Office with id: " + office.getOfficeId() + " already exists");
            }
            return officeRepository.save(office);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save office");
            exc.addErrorMessage("office is null or empty");
            throw exc;
        }
    }
	
	public void update(Office office) throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(office.getOfficeName())) {
            if (!existsById(office.getOfficeId())) {
                throw new ResourceNotFoundException("Cannot find office with id: " + office.getOfficeId());
            }
            officeRepository.save(office);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save office");
            exc.addErrorMessage("Office is null or empty");
            throw exc;
        }
    }
	
	public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) { 
            throw new ResourceNotFoundException("Cannot find office with id: " + id);
        }
        else {
            officeRepository.deleteById(id);
        }
    }
	
}
