package org.example.backend.model;

public class BookSummaryDTO {
    private String title;
    private String author;
    private String summary;

    public BookSummaryDTO() {}

    public BookSummaryDTO(String title, String author, String summary) {
        this.title   = title;
        this.author  = author;
        this.summary = summary;
    }

    // getters + setters
    public String getTitle()   { return title; }
    public void   setTitle(String title) { this.title = title; }
    public String getAuthor()  { return author; }
    public void   setAuthor(String author) { this.author = author; }
    public String getSummary() { return summary; }
    public void   setSummary(String summary) { this.summary = summary; }
}