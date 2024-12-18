package com.example.employee;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.nio.charset.Charset;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AppTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() throws Exception {
        Employee employee1 = new Employee();
        employee1.setFirstName("Harsha");
        employee1.setLastName("Sharma");
        employee1.setEmail("harshasharma@gmail.com");
        employeeRepository.save(employee1);

		Employee employee2 = new Employee();
        employee2.setFirstName("Viraj");
        employee2.setLastName("Jain");
        employee2.setEmail("jainviraj@gmail.com");
        employeeRepository.save(employee2);
    }

    @Test
    @DisplayName("test post Employee entity")
    @Order(1)
	public void testSaveEmployees() throws Exception {

        Employee employee3 = new Employee();
        employee3.setFirstName("Udesh");
        employee3.setLastName("Shah");
        employee3.setEmail("shahudesh@gmail.com");
        employeeRepository.save(employee3);

        Employee Employee = employeeService.saveEmployee(employee3);

        assertEquals(employee3.getId(), Employee.getId());
        assertEquals(employee3.getFirstName(), Employee.getFirstName());
        assertEquals(employee3.getLastName(), Employee.getLastName());
        assertEquals(employee3.getEmail(), Employee.getEmail());

    }


    @Test
    @DisplayName("test delete by Id")
    @Transactional
    @Order(3)
	public void testDeleteById() throws Exception{

        String delString = employeeService.deleteEmployeeById(2);

        assertEquals("The requested id got deleted", delString);

    }


    @Test
    @DisplayName("test post Employee entity created response")
    @Order(4)
	public void testSaveEmployeesCreatedResponse() throws Exception {

        Employee employee3 = new Employee();
        employee3.setFirstName("Udesh");
        employee3.setLastName("Shah");
        employee3.setEmail("shahudesh@gmail.com");
        employeeRepository.save(employee3);
      

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(employee3);

        mockMvc.perform(post("/employees").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.firstName", is("Udesh")))
                .andExpect(jsonPath("$.lastName", is("Shah")))
                .andExpect(jsonPath("$.email", is("shahudesh@gmail.com")));
    }

    @Test
    @DisplayName("test post Employee entity bad request for null")
    @Order(5)
	public void testSaveEmployeesBadRequestNull() throws Exception {

        Employee employee3 = new Employee();
        employee3.setFirstName(null);
        employee3.setLastName("Shah");
        employee3.setEmail("shahudesh@gmail.com");
        employeeRepository.save(employee3);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(employee3);

        mockMvc.perform(post("/employees").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("test post Employee entity bad request for empty data")
    @Order(6)
	public void testSaveEmployeesBadRequestEmpty() throws Exception {

        Employee employee3 = new Employee();
        employee3.setFirstName("Udesh");
        employee3.setLastName("Shah");
        employee3.setEmail("");
        employeeRepository.save(employee3);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(employee3);

        mockMvc.perform(post("/employees").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("check delete students data Ok response")
    @Order(7)
    public void testDeleteEmployeeByIdOk() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/employees/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("check delete students data NotFound response")
    @Order(8)
    public void testDeleteEmployeeByIdNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/employees/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
}
