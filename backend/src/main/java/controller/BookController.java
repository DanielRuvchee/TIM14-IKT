
package controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookSummaryRepository summaryRepository;

    @GetMapping("/all")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/summaries")
    public List<BookSummary> getAllSummaries() {
        return summaryRepository.findAll();
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