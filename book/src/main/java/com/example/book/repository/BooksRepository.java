package com.example.book.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.book.entity.Books;

//repository that extends JpaRepository  
public interface BooksRepository extends JpaRepository<Books, Integer>  
{  
    
}  