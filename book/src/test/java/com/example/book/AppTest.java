package com.example.book;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.book.entity.Books;
import com.example.book.repository.BooksRepository;
import com.example.book.service.BooksService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;


@SpringBootTest()
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AppTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired 
	BooksService booksService;

    @Autowired
    BooksRepository booksRepository;
	
    @BeforeEach
    public void setUp() throws Exception {
        Books books1 = new Books();
        books1.setBookId(1);
        books1.setBookName("Merchant Of Venice");
        books1.setAuthor("Shakespeare");
        books1.setBookCostPrice(50.87);
        booksRepository.save(books1);

        Books books2 = new Books();
        books2.setBookId(2);
        books2.setBookName("Lost City");
        books2.setAuthor("Shawn Medes");
        books2.setBookCostPrice(97.99);
        booksRepository.save(books2);

    }


	@Test
    @Transactional
    @DisplayName("save Books details")
    public void SaveBooks() throws Exception {
        Books books3 = new Books();
        books3.setBookId(3);
        books3.setBookName("Smart City");
        books3.setAuthor("Sameer Joshi");
        books3.setBookCostPrice(80.99);
        booksRepository.save(books3);
       

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(books3);
        
        mockMvc.perform(put("/books").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId", is(3)))
                .andExpect(jsonPath("$.bookName", is("Smart City")))
                .andExpect(jsonPath("$.author", is("Sameer Joshi")))
                .andExpect(jsonPath("$.bookCostPrice", is(80.99)));
    }

    @Test
    @DisplayName("save Books details for Bad Request")
    public void SaveBooksBadRequest() throws Exception {
        Books books3 = new Books();
        books3.setBookId(3);
        books3.setBookName("");
        books3.setAuthor("Sameer Joshi");
        books3.setBookCostPrice(80.99);
        booksRepository.save(books3);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(books3);
        
        mockMvc.perform(put("/books").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
            
    }


    @Test
    @DisplayName("Get Bad Request response for ivalid cost price")
    public void SaveBooksBadRequest_2() throws Exception {
        Books books3 = new Books();
        books3.setBookId(3);
        books3.setBookName("Smart City");
        books3.setAuthor("Sameer Joshi");
        books3.setBookCostPrice(-4);
        booksRepository.save(books3);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(books3);
        
        mockMvc.perform(put("/books").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
            
    }

    @Test
    @Transactional
    @DisplayName("Update the book details")
    public void UpdateBooks() throws Exception {
        Books books3 = booksRepository.getById(2);
        books3.setBookName("Lost City");
        books3.setAuthor("Shawn Medes");
        books3.setBookCostPrice(20.99);
        booksRepository.save(books3);


        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(books3);
        
        mockMvc.perform(put("/books").contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bookId", is(2)))
                .andExpect(jsonPath("$.bookName", is("Lost City")))
                .andExpect(jsonPath("$.author", is("Shawn Medes")))
                .andExpect(jsonPath("$.bookCostPrice", is(20.99)));
            
    }

    @Test
    @DisplayName("Get Books details")
    public void GetBooksOK() throws Exception {
        
        mockMvc.perform(get("/books").contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId", is(1)))
                .andExpect(jsonPath("$[0].bookName", is("Merchant Of Venice")))
                .andExpect(jsonPath("$[0].author", is("Shakespeare")))
                .andExpect(jsonPath("$[0].bookCostPrice", is(50.87)))
                .andExpect(jsonPath("$[1].bookId", is(2)))
                .andExpect(jsonPath("$[1].bookName", is("Lost City")))
                .andExpect(jsonPath("$[1].author", is("Shawn Medes")))
                .andExpect(jsonPath("$[1].bookCostPrice", is(97.99)));
            
    }


    @Test
    @DisplayName("Get Books details Not Found")
    public void GetBooksNF() throws Exception {

        booksRepository.delete(booksRepository.getById(1));
        booksRepository.delete(booksRepository.getById(2));
    
        mockMvc.perform(get("/books").contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete Books details")
    public void DeleteBooksOK() throws Exception {
    
        mockMvc.perform(delete("/books").contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Books details Not Found")
    public void DeleteBooksNF() throws Exception {

    
        booksRepository.delete(booksRepository.getById(1));
        booksRepository.delete(booksRepository.getById(2));

    
        mockMvc.perform(delete("/books").contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNotFound());
                
    }
}
