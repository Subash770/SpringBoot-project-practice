package com.example.sensor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.sensor.entity.Sensor;
import com.example.sensor.service.SensorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AppTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean 
    SensorService sensorService;

    @BeforeEach
    public void setUp() throws Exception {
    	
    }   

    @Test
    @DisplayName("check get sensor by id")
    public void getSensorById() throws Exception{
        
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1);
        sensor1.setSensorName("Humidity Sensor 2");
        sensor1.setSensorTypeName("Humidity");
        sensor1.setUnitsOfMeasure("Percentage");
        sensor1.setLocationName("Room H");
        
        Optional<Sensor> sensorData = Optional.of(sensor1);

        Mockito.when( sensorService.getSensorById(1)).thenReturn(sensorData);
        
        mockMvc.perform(MockMvcRequestBuilders
                .get("/sensors/1")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(jsonPath("$.sensorId", is(1)))
                .andExpect(jsonPath("$.sensorName", is("Humidity Sensor 2")))
                .andExpect(jsonPath("$.sensorTypeName", is("Humidity")))
                .andExpect(jsonPath("$.unitsOfMeasure", is("Percentage")))
                .andExpect(jsonPath("$.locationName", is("Room H")));
                
    }

    @Test
    @DisplayName("check get sensor data OK resposne ")
    public void testGetSensorByIdOk() throws Exception {
        
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1);
        sensor1.setSensorName("Humidity Sensor 2");
        sensor1.setSensorTypeName("Humidity");
        sensor1.setUnitsOfMeasure("Percentage");
        sensor1.setLocationName("Room H");

        Optional<Sensor> sensorData = Optional.of(sensor1);

        Mockito.when( sensorService.getSensorById(1)).thenReturn(sensorData);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/sensors/1")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("check get sensor data NotFound resposne ")
    public void testGetSensorByIdNotFound() throws Exception {
        
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1);
        sensor1.setSensorName("Humidity Sensor 2");
        sensor1.setSensorTypeName("Humidity");
        sensor1.setUnitsOfMeasure("Percentage");
        sensor1.setLocationName("Room H");

        Optional<Sensor> sensorData = Optional.of(sensor1);

        Mockito.when( sensorService.getSensorById(1)).thenReturn(sensorData);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/sensors/2")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("check get all sensor data ")
	public void testGetAllSensorData() throws Exception {
		
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1);
        sensor1.setSensorName("Humidity Sensor 2");
        sensor1.setSensorTypeName("Humidity");
        sensor1.setUnitsOfMeasure("Percentage");
        sensor1.setLocationName("Room H");

        Sensor sensor2 = new Sensor();
        sensor2.setSensorId(1);
        sensor2.setSensorName("Pressure Sensor 2");
        sensor2.setSensorTypeName("Pressure");
        sensor2.setUnitsOfMeasure("Pascal");
        sensor2.setLocationName("Room F");
			
		List<Sensor> sensorData = new ArrayList<>();
		sensorData.add(sensor1);
		sensorData.add(sensor2);
		
		Mockito.when( sensorService.getAllSensors() ).thenReturn(Arrays.asList(sensor1,sensor2));
		
		String URI = "/sensors";
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				URI).accept(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		String expectedJson = this.mapToJson(sensorData);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
		String outputInJson = result.getResponse().getContentAsString();
		assertThat(outputInJson).isEqualTo(expectedJson);
	}

    @Test
    @DisplayName("check get all sensor data for OK resposne ")
	public void testGetAllSensorDataOK() throws Exception {
		
        Sensor sensor1 = new Sensor();
        sensor1.setSensorId(1);
        sensor1.setSensorName("Humidity Sensor 2");
        sensor1.setSensorTypeName("Humidity");
        sensor1.setUnitsOfMeasure("Percentage");
        sensor1.setLocationName("Room H");

        Sensor sensor2 = new Sensor();
        sensor2.setSensorId(1);
        sensor2.setSensorName("Pressure Sensor 2");
        sensor2.setSensorTypeName("Pressure");
        sensor2.setUnitsOfMeasure("Pascal");
        sensor2.setLocationName("Room F");
		
		Mockito.when( sensorService.getAllSensors() ).thenReturn(Arrays.asList(sensor1,sensor2));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/sensors")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isOk());
	}

    @Test
    @DisplayName("check get all sensor data for NotFound resposne ")
	public void testGetAllSensorDataNotFound() throws Exception {

		
		Mockito.when( sensorService.getAllSensors() ).thenReturn(Arrays.asList());

        mockMvc.perform(MockMvcRequestBuilders
                .get("/sensors")
                .contentType(MediaType.APPLICATION_JSON)) 
                .andExpect(status().isNotFound());
	}

     private String mapToJson(List<Sensor> sensors) throws JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(sensors);
	}
    
}