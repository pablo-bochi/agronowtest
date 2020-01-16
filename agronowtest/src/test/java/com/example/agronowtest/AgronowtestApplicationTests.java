package com.example.agronowtest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.example.agronowtest.exception.ResourceNotFoundException;
import com.example.agronowtest.model.Bar;
import com.example.agronowtest.service.BarService;

@RunWith(SpringRunner.class)
@SpringBootTest
class AgronowtestApplicationTests {

	@Autowired 
    private BarService barService;
	
	@Rule
    public ExpectedException exceptionRule = ExpectedException.none();
	
	@Test
	void contextLoads() throws Exception {
		Bar b = new Bar();
        b.setBarName("Fast Berlin");
        b.setBarAddress("Rua Mourato Coelho, 24, Pinheiros, São Paulo - SP");
        b.setBarCoordinates("-23.5762185, -46.684450");
        
        //test adding new bar
        barService.save(b);
        assertNotNull(b.getBarId());
        
        //test list
        Bar findBar = barService.findById(b.getBarId());
        assertEquals("Fast Berlin", findBar.getBarName());
        assertEquals("Rua Mourato Coelho, 24, Pinheiros, São Paulo - SP", findBar.getBarAddress());
        
        //update
        b.setBarAddress("R. Baltazar Carrasco, 187 - Pinheiros, São Paulo - SP");
        barService.update(b);
        
        //test update
        findBar = barService.findById(b.getBarId());
        assertEquals("R. Baltazar Carrasco, 187 - Pinheiros, São Paulo - SP", findBar.getBarAddress());
        
        //delete
        barService.deleteById(b.getBarId());
        
        //test delete
        exceptionRule.expect(ResourceNotFoundException.class);
        exceptionRule.expectMessage("Cannot find bar with id:" + b.getBarId());
        try {
            barService.findById(b.getBarId());
            Assert.fail();
		} catch (ResourceNotFoundException e) {
			System.out.println("Couldn't find bar.");
		}
	}

}
