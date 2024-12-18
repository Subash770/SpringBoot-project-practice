package com.example.book.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;  
import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.PostMapping;  
import org.springframework.web.bind.annotation.PutMapping;  
import org.springframework.web.bind.annotation.RequestBody;  
import org.springframework.web.bind.annotation.RestController;

import com.example.book.entity.Books;
import com.example.book.service.BooksService;


@RestController  
public class BooksController   
{  
    @Autowired  
    BooksService booksService;  
    
    @CrossOrigin
    @GetMapping("/books")  
    private ResponseEntity<List<Books>> getAllBooks()   
    {  
         List<Books> books=booksService.getAllBooks();
         if(books.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
         return new ResponseEntity<>(books,HttpStatus.OK);

    }  
    
    @CrossOrigin
    @DeleteMapping("/books")  
    private ResponseEntity<String> deleteBook()   
    {  
        List<Books> books=booksService.getAllBooks();
        if(books.isEmpty()){
            return new ResponseEntity<>("No books found to delete",HttpStatus.NOT_FOUND);
        }
        booksService.deleteAll();
         return new ResponseEntity<>("All books are deleted successfully",HttpStatus.OK);
					
    }  
    
    @CrossOrigin
    @PutMapping("/books")  
    private ResponseEntity<Books> update(@RequestBody Books books)   
    {  
        if(books.getBookName()==null||books.getBookName().isEmpty() ||books.getAuthor().isEmpty() || books.getBookCostPrice()<=0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Books updatedBook=booksService.saveOrUpdate(books);
        return new ResponseEntity<>(updatedBook,HttpStatus.CREATED);

    }   
}  
