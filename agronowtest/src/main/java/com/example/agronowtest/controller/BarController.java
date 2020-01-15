package com.example.agronowtest.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

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
import com.example.agronowtest.model.Office;
import com.example.agronowtest.model.RaffleResult;
import com.example.agronowtest.service.BarService;
import com.example.agronowtest.service.OfficeService;
import com.uber.sdk.core.client.ServerTokenSession;
import com.uber.sdk.core.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.PriceEstimate;
import com.uber.sdk.rides.client.model.PriceEstimatesResponse;
import com.uber.sdk.rides.client.services.RidesService;

import io.swagger.annotations.ApiOperation;

@RestController
@ApiOperation(value = "Controls the endpoints to Bars CRUD operations.")
public class BarController {

	@Autowired
    private BarService barService;
	private OfficeService officeService;
	
	private final int ROW_PER_PAGE = 10;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@ApiOperation(value = "Returns a list of all bars on database.")
	@GetMapping(value = "/bars", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Bar>> findAll(@RequestParam(value="page", defaultValue="1") int pageNumber) {
        return ResponseEntity.ok(barService.findAll(pageNumber, ROW_PER_PAGE));
    }
	
	@ApiOperation(value = "Returns the bar with given id.")
	@GetMapping(value = "/bars/{barId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Bar> findBarById(@PathVariable long barId) {
        try {
            Bar bar = barService.findById(barId);
            System.out.println(barService.count());
            return ResponseEntity.ok(bar);  //return 200
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); //return 404
        }
    }
	
	@ApiOperation(value = "Creates a bar with the information provided.")
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
	
	@ApiOperation(value = "Updates a bar with given id on database.")
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
	
	@ApiOperation(value = "Delete bar with given id.")
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
	
	//raffle endpoint
	@ApiOperation(value = "Raffles some bar on database and provides Uber run information from the office to the pub.")
	@PostMapping(path="/raffle")
	public ResponseEntity<RaffleResult> raffleBar(@Valid @RequestBody long officeId){
		Office office = new Office();
		Bar bar = new Bar();
		RaffleResult raffleResult = new RaffleResult();
		Random rd = new Random();
		long barId;
		
		try {
			office = officeService.findById(officeId); //get the chosen office
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		raffleResult.setOfficeName(office.getName()); //set the office name for the response
		raffleResult.setOfficeAddress(office.getAddress()); //set the office address for the response
		
		barId = (long) rd.nextInt((int) barService.count()); //raffles the bar
		
		try {
			bar = barService.findById(barId); //get the raffled bar
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		
		raffleResult.setPubName(bar.getName());
		raffleResult.setPubAddress(bar.getAddress());
		
		List<PriceEstimate> priceEstimateList = getRideInfo(bar.getCoordinates(), office.getCoordinates()); //call the request to uber API to get ride info
		
		//Get estimate, distance and duration for uberx, uber select and uber black
		String productName;
		
		String uberxEstimate = "";
		String uberSelectEstimate = "";
		String uberBlackEstimate = "";
		
		float rideDuration = priceEstimateList.get(0).getDuration();
		float rideDistance = priceEstimateList.get(0).getDistance();
		
		for(int i=0; i<priceEstimateList.size(); i++) {
			productName = priceEstimateList.get(i).getDisplayName().toLowerCase();
			
			switch (productName) {
				case "uberx":
					uberxEstimate = priceEstimateList.get(i).getEstimate();
				break;
				
				case "uberselect":
					uberSelectEstimate = priceEstimateList.get(i).getEstimate();
				break;
				
				case "uberblack":
					uberBlackEstimate = priceEstimateList.get(i).getEstimate();
				break;

				default:
				break;
			}
		}
		
		raffleResult.setDistance(rideDistance);
		raffleResult.setDuration(rideDuration);
		raffleResult.setEstimateX(uberxEstimate);
		raffleResult.setEstimateSelect(uberSelectEstimate);
		raffleResult.setEstimateBlack(uberBlackEstimate);
		
		return ResponseEntity.status(HttpStatus.OK).body(raffleResult);
	}
	
	private static List<PriceEstimate> getRideInfo(String barCoordinates, String officeCoordinates) {
		//treating the coodinates
		String[] startCoord = officeCoordinates.split(",");
		String[] endCoord = barCoordinates.split(",");
		
		float startLat = Float.parseFloat(startCoord[0]);
		float startLong = Float.parseFloat(startCoord[1]);
		
		float endLat = Float.parseFloat(endCoord[0]);
		float endLong = Float.parseFloat(endCoord[1]);
		
		//initializing session in uber api
		SessionConfiguration config = new SessionConfiguration.Builder()
			    .setClientId("Dykpa6OMxqUEWON3IQjJSX3oO-jZVvkI")
			    .setServerToken("YOUR_SERVER_TOKEN")
			    .build();

		ServerTokenSession session = new ServerTokenSession(config);
		
		UberRidesApi uberRidesApi = UberRidesApi.with(session).build();
		
		RidesService service = uberRidesApi.createService();
		
		//Get prices for the ride with given location (pub and office)
		List<PriceEstimate> priceEstimateList = null;
		try {
			PriceEstimatesResponse priceEstimate = service.getPriceEstimates(startLat, startLong, endLat, endLong).execute().body();
			priceEstimateList = priceEstimate.getPrices();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return priceEstimateList;
	}
}
