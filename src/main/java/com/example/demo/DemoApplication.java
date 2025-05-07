package com.example.demo;

import com.example.demo.dto.CreateBookRequestDto;
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
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
//                createBookRequestDto.setDescription("dbfhd");
//                createBookRequestDto.setTitle("dfkhfb");
//                createBookRequestDto.setPrice(BigDecimal.valueOf(5464L));
//                createBookRequestDto.setCoverImage("dnfbd");
//                createBookRequestDto.setAuthor("jdbfh");
//                bookService.createBook(createBookRequestDto);
            }
        };
    }
}
