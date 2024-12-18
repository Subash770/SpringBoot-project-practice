package com.example.shipment_model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.shipment_model.entity.Shipment;
import com.example.shipment_model.repository.ShipmentRepository;
import com.example.shipment_model.service.ShipmentService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AppTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Autowired
    private MockMvc mockMvc;

	@Autowired
	ShipmentService shipmentService;

    @Autowired
    ShipmentRepository shipmentRepository;

    @BeforeEach
    public void setUp() throws Exception {
        Shipment shipment1 = new Shipment();
        shipment1.setShipId(1);
        shipment1.setTrackNo("TRK101");
        shipment1.setOrigin("Japan");
        shipment1.setDestination("Singapore");
		shipment1.setStatus("Delivered");
        shipmentRepository.save(shipment1);

		Shipment shipment2 = new Shipment();
        shipment2.setShipId(2);
        shipment2.setTrackNo("TRK201");
        shipment2.setOrigin("China");
        shipment2.setDestination("Singapore");
		shipment2.setStatus("Pending");
        shipmentRepository.save(shipment2);
    }


    @Test
    @DisplayName("test get shipment entity by track no.")
	public void testGetShipmentEntityByTrackNo() throws Exception{
        Shipment getData = shipmentService.getShipmentByTrackNo("TRK101");
        List<Shipment> getAll = shipmentService.getAllShipment();
        Shipment expectedData = getAll.get(0);
        assertEquals(expectedData.getShipId(), getData.getShipId());
        assertEquals(expectedData.getTrackNo(), getData.getTrackNo());
        assertEquals(expectedData.getOrigin(), getData.getOrigin());
        assertEquals(expectedData.getDestination(), getData.getDestination());
        assertEquals(expectedData.getStatus(), getData.getStatus());
    }


    @Test
    @DisplayName("test delete by Id")
	public void testDeleteById() throws Exception{
        String getData = shipmentService.deleteById(2);
        assertEquals("The requested shipId got deleted", getData);
    }

    @Test
    @DisplayName("check response for shipment by track No")
    public void testGetShipmentByTrackNoResponse() throws Exception{
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/shipments/track-ship/TRK201")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(jsonPath("$.shipId", is(2)))
                .andExpect(jsonPath("$.trackNo", is("TRK201")))
                .andExpect(jsonPath("$.origin", is("China")))
                .andExpect(jsonPath("$.destination", is("Singapore")))
                .andExpect(jsonPath("$.status", is("Pending")));
                
    }

    @Test
    @DisplayName("check get Shipment data OK resposne ")
    public void testGetShipmentByTrackNoOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/shipments/track-ship/TRK101")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("check get Shipment data NotFound resposne ")
    public void testGetShipmentByTrackNoNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/shipments/track-ship/TRK102")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("check delete shipment by shipId")
	public void testDeleteShipmentByShipId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/shipments/1")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The requested shipId got deleted")));


	}

    @Test
    @DisplayName("check delete Shipment data Ok response")
    public void testDeleteShipmentByIdOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/shipments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("check delete Shipment data NotFound response")
    public void testDeleteShipmentByIdNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/shipments/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}