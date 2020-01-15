package com.example.agronowtest;

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
        b.setName("Fast Berlin");
        b.setAddress("Rua Mourato Coelho, 24, Pinheiros, São Paulo - SP");
        b.setCoordinates("-23.566185, -46.685420");
        
        //test adding new bar
        barService.save(b);
        assertNotNull(b.getId());
        
        //test list
        Bar findBar = barService.findById(b.getId());
        assertEquals("Fast Berlin", findBar.getName());
        assertEquals("Rua Mourato Coelho, 24, Pinheiros, São Paulo - SP", findBar.getAddress());
        
        //update
        b.setAddress("R. Baltazar Carrasco, 187 - Pinheiros, São Paulo - SP");
        barService.update(b);
        
        //test update
        findBar = barService.findById(b.getId());
        assertEquals("R. Baltazar Carrasco, 187 - Pinheiros, São Paulo - SP", findBar.getAddress());
        
        //delete
        barService.deleteById(b.getId());
        
        //test delete
        exceptionRule.expect(ResourceNotFoundException.class);
        barService.findById(b.getId());
	}

}
