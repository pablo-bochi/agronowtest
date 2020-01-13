package com.example.agronowtest.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.agronowtest.exception.BadResourceException;
import com.example.agronowtest.exception.ResourceAlreadyExistsException;
import com.example.agronowtest.exception.ResourceNotFoundException;
import com.example.agronowtest.model.Bar;
import com.example.agronowtest.service.BarService;

@RestController
public class BarController {

	@Autowired
    private BarService barService;
	
	private final int ROW_PER_PAGE = 5;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping(value = "/bars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Bar>> findAll(@RequestParam(value="page", defaultValue="1") int pageNumber) {
        return ResponseEntity.ok(barService.findAll(pageNumber, ROW_PER_PAGE));
    }
	
	@GetMapping(value = "/bars/{barId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bar> findContactById(@PathVariable long barId) {
        try {
            Bar bar = barService.findById(barId);
            return ResponseEntity.ok(bar);  //return 200
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //return 404
        }
    }
	
	@PostMapping(value = "/bars")
    public ResponseEntity<Bar> addBar(@Valid @RequestBody Bar bar) throws URISyntaxException {
        try {
            Bar newBar = barService.save(bar);
            return ResponseEntity.created(new URI("/bars/" + newBar.getId())).body(bar);
        } catch (ResourceAlreadyExistsException ex) {
            //log exception, return Conflict (409)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadResourceException ex) {
            //log exception, return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
	
	@PutMapping(value = "/bars/{barId}")
    public ResponseEntity<Bar> updateBar(@Valid @RequestBody Bar bar, @PathVariable long barId) {
        try {
            bar.setId(barId);
            barService.update(bar);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            //log exception, return Not Found (404)
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BadResourceException ex) {
            //log exception, return Bad Request (400)
            logger.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
	
	@DeleteMapping(path="/bars/{barId}")
    public ResponseEntity<Void> deleteBarById(@PathVariable long barId) {
        try {
            barService.deleteById(barId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
	
}
