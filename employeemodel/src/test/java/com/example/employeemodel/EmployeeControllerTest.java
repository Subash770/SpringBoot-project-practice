package com.example.employeemodel;

import com.example.employeemodel.entity.Employee;
import com.example.employeemodel.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    // @Autowired
    // private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // employeeRepository.deleteAll();
    }

    @Test
    public void testAddEmployee() throws Exception {
        Employee employee = new Employee("John Doe", 300,"Not Achieved");
       // String employeeJson = objectMapper.writeValueAsString(employee);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievedTarget").value(300))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Not Achieved"));
    }

    @Test
    public void testAddEmployeeBadRequest() throws Exception {
        Employee employee = new Employee("John Doe", -2,"Not Achieved");
       // String employeeJson = objectMapper.writeValueAsString(employee);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testAddEmployeeNullBadRequest() throws Exception {
        Employee employee = new Employee("", 0,"Not Achieved");
       // String employeeJson = objectMapper.writeValueAsString(employee);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name shouldn't be null or empty"));;

    }

    @Test
    public void testAddEmployeeEmptyBadRequest() throws Exception {
        Employee employee = new Employee(null, null,"Done");
       // String employeeJson = objectMapper.writeValueAsString(employee);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name shouldn't be null or empty"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.achievedTarget").value("achievedTarget shouldn't be null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Invalid employee status: It should be either Achieved or Not Achieved"));

    }

    @Test
    public void testGetAllExpectedTarget() throws Exception {
        Employee employee1 = new Employee("John Doe", 600,"Achieved");
        Employee employee2 = new Employee("Melvin Doe", 300,"Not Achieved");
        Employee employee3 = new Employee("Yuvan K", 800,"Achieved");

        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));

        mockMvc.perform(get("/employees/targets/400")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].achievedTarget").value(600))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("Achieved"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Yuvan K"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].achievedTarget").value(800))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value("Achieved"));
    }

    @Test
    public void testGetAllExpectedTargetNotFound() throws Exception {
        Employee employee1 = new Employee("John Doe", 600,"Achieved");
        Employee employee2 = new Employee("Melvin Doe", 300,"Not Achieved");
        Employee employee3 = new Employee("Yuvan K", 800,"Achieved");

        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));

        mockMvc.perform(get("/employees/targets/900")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}
