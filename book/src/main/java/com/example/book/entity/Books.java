package com.example.book.entity;

import javax.persistence.Column;  
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;  
import javax.persistence.Table;

import com.sun.istack.NotNull;  

@Entity  
@Table(name = "books")
public class Books  
{  

	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@NotNull
	private int bookId; 

	@Column
	@NotNull
	private String bookName;


	@Column
	@NotNull
	private String author;  

	@Column
	@NotNull
	private double bookCostPrice;

	public Books() {
	}

    public Books(String bookName, String author, double bookCostPrice) {
        this.bookName = bookName;
        this.author = author;
        this.bookCostPrice = bookCostPrice;
    }

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getBookCostPrice() {
		return bookCostPrice;
	}

	public void setBookCostPrice(double bookCostPrice) {
		this.bookCostPrice = bookCostPrice;
	}

	@Override
	public String toString() {
		return "Books [bookId=" + bookId + ", bookName=" + bookName + ", author=" + author + ", bookCostPrice="
				+ bookCostPrice + "]";
	}  

	
}  