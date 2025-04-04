import React from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "@/components/Navbar";
import BookCard from "@/components/BookCard";

const Books = () => {
    const navigate = useNavigate();

    // Mock book data
    const mockBooks = [
        { id: 1, title: "Crime and Punishment", category: "Classic", coverUrl: "https://upload.wikimedia.org/wikipedia/en/4/4b/Crimeandpunishmentcover.png" },
        { id: 2, title: "The Great Alone", category: "Fiction", coverUrl: "https://kristinhannah.com/wp-content/uploads/2017/05/The-Great-Alone-Book.jpg" },
        { id: 3, title: "The House of Mirth", category: "Fiction", coverUrl: "https://us.macmillan.com/wp-content/uploads/2020/08/9781909621978.jpg" },
        { id: 4, title: "The Little Liar",category: "Fiction", coverUrl: "https://www.mitchalbom.com/wp-content/uploads/2023/06/the-little-liar-cover.jpg" },
        { id: 5, title: "The Reluctant Sheriff", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/51z1K6lF5sL.jpg" },
        { id: 6, title: "The Secret Room", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },
        { id: 7, title: "The Girl with the Dragon Tattoo", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/51z1K6lF5sL.jpg" },
        { id: 8, title: "Gone Girl", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },
        { id: 9, title: "Sapiens: A Brief History of Humankind", category: "Non-Fiction", "coverUrl": "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },
        { id: 10, title: "Educated", category: "Non-Fiction", coverUrl: "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },

    ];


    const categorizedBooks = mockBooks.reduce((acc, book) => {
        acc[book.category] = acc[book.category] || [];
        acc[book.category].push(book);
        return acc;
    }, {});

    const handleViewDetails = (id) => {
        navigate(`/books/${id}`);
    };

    return (
        <div className="min-h-screen bg-turquoise-light/30">
            <Navbar />

            <div className="max-w-4xl mx-auto px-6 py-8">
                <div className="bg-white p-8 rounded-lg shadow-lg max-w-7xl mx-auto">
                    <h1 className="text-3xl font-bold text-center text-turquoise-light mb-8">
                        Book Collection
                    </h1>

                    {Object.keys(categorizedBooks).length > 0 ? (
                        Object.entries(categorizedBooks).map(([category, books]) => (
                            <div key={category} className="mb-6">
                                <h3 className="text-xl font-semibold text-turquoise-light mb-3">{category}</h3>
                                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
                                    {books.map(book => (
                                        <BookCard key={book.id} book={book} onViewDetails={handleViewDetails} />
                                    ))}
                                </div>
                            </div>
                        ))
                    ) : (
                        <p className="text-center text-gray-600 text-lg">
                            Your book collection will appear here after you upload books for summarization.
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Books;
