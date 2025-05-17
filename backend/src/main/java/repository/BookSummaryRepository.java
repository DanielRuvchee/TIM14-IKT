package repository;

import model.BookSummary;

public interface BookSummaryRepository extends MongoRepository<BookSummary, String> {
}