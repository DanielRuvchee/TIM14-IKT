import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";

interface Book {
    id: string;
    title: string;
    content: string;
}

const Books: React.FC = () => {
    const [books, setBooks] = useState<Book[]>([]);
    const [error, setError] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get("http://localhost:8080/api/books/all")
            .then(response => {
                setBooks(response.data);
            })
            .catch(error => {
                console.error("Error fetching books:", error);
                setError(true);
            });
    }, []);

    const handleViewDetails = (id: string) => {
        navigate(`/books/${id}`);
    };
    return (
        <div className="min-h-screen bg-turquoise-light/30">
            <Navbar />

            <div className="max-w-6xl mx-auto px-4 py-10">
                <h1 className="text-3xl font-bold text-center text-white mb-8">
                    Book Collection
                </h1>

                <div className="bg-white rounded-lg p-8 shadow-sm">
                    {error ? (
                        <p className="text-center text-red-500">
                            Failed to fetch books. Please check if the backend is running.
                        </p>
                    ) : books.length > 0 ? (
                        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                            {books.map(book => (
                                <div key={book.id} className="bg-gray-100 p-4 rounded-lg shadow-lg">
                                    <h2 className="text-lg font-bold mt-3 text-gray-800">{book.title}</h2>
                                    <button
                                        onClick={() => handleViewDetails(book.id)}
                                        className="mt-4 w-full px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                                    >
                                        View Details
                                    </button>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p className="text-center text-gray-600">
                            Your book collection will appear here after you upload books.
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Books;