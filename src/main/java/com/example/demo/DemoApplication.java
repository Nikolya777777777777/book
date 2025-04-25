package com.example.demo;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				Book book = new Book();
				book.setAuthor("Shevchenko");
				book.setPrice(BigDecimal.valueOf(100));
				book.setDescription("fnhfb");
				book.setIsbn("fjbff");
				book.setTitle("fkjbfhjfb");
				book.setCoverImage("fkjbfhj");

				bookService.save(book);
				System.out.println(bookService.findAll());
			}
		};
	}
}
