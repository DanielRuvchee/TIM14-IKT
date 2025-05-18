
package controller;


import model.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.BookRepository;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;


    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }


    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable String id) {
        return bookRepository.findById(id);
    }

    // Save a new summary from AI
    //@PostMapping("/summary")
    //public BookSummary saveSummary(@RequestBody BookSummary summary) {
    //    return summaryRepository.save(summary);}
}