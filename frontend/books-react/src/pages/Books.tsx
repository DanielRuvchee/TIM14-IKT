// import React from "react";
// import { useNavigate } from "react-router-dom";
// import Navbar from "@/components/Navbar";
// import BookCard from "@/components/BookCard";
//
// const Books = () => {
//     const navigate = useNavigate();
//
//     // Mock book data
//     const mockBooks = [
//         { id: 1, title: "Crime and Punishment", category: "Classic", coverUrl: "https://upload.wikimedia.org/wikipedia/en/4/4b/Crimeandpunishmentcover.png" },
//         { id: 2, title: "The Great Alone", category: "Fiction", coverUrl: "https://kristinhannah.com/wp-content/uploads/2017/05/The-Great-Alone-Book.jpg" },
//         { id: 3, title: "The House of Mirth", category: "Fiction", coverUrl: "https://us.macmillan.com/wp-content/uploads/2020/08/9781909621978.jpg" },
//         { id: 4, title: "The Little Liar",category: "Fiction", coverUrl: "https://www.mitchalbom.com/wp-content/uploads/2023/06/the-little-liar-cover.jpg" },
//         { id: 5, title: "The Reluctant Sheriff", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/51z1K6lF5sL.jpg" },
//         { id: 6, title: "The Secret Room", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },
//         { id: 7, title: "The Girl with the Dragon Tattoo", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/51z1K6lF5sL.jpg" },
//         { id: 8, title: "Gone Girl", category: "Mystery", coverUrl: "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },
//         { id: 9, title: "Sapiens: A Brief History of Humankind", category: "Non-Fiction", "coverUrl": "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },
//         { id: 10, title: "Educated", category: "Non-Fiction", coverUrl: "https://m.media-amazon.com/images/I/41NfGZ2J2XL.jpg" },
//
//     ];
//
//
//     const categorizedBooks = mockBooks.reduce((acc, book) => {
//         acc[book.category] = acc[book.category] || [];
//         acc[book.category].push(book);
//         return acc;
//     }, {});
//
//     const handleViewDetails = (id) => {
//         navigate(`/books/${id}`);
//     };
//
//     return (
//         <div className="min-h-screen bg-turquoise-light/30">
//             <Navbar />
//
//             <div className="max-w-4xl mx-auto px-6 py-8">
//                 <div className="bg-white p-8 rounded-lg shadow-lg max-w-7xl mx-auto">
//                     <h1 className="text-3xl font-bold text-center text-turquoise-light mb-8">
//                         Book Collection
//                     </h1>
//
//                     {Object.keys(categorizedBooks).length > 0 ? (
//                         Object.entries(categorizedBooks).map(([category, books]) => (
//                             <div key={category} className="mb-6">
//                                 <h3 className="text-xl font-semibold text-turquoise-light mb-3">{category}</h3>
//                                 <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
//                                     {books.map(book => (
//                                         <BookCard key={book.id} book={book} onViewDetails={handleViewDetails} />
//                                     ))}
//                                 </div>
//                             </div>
//                         ))
//                     ) : (
//                         <p className="text-center text-gray-600 text-lg">
//                             Your book collection will appear here after you upload books for summarization.
//                         </p>
//                     )}
//                 </div>
//             </div>
//         </div>
//     );
// };
//
// export default Books;


import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Navbar from "@/components/Navbar";

const Books = () => {
    const [books, setBooks] = useState([]);
    const [categorizedBooks, setCategorizedBooks] = useState({});
    const [error, setError] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get("http://localhost:8080/api/books")
            .then(response => {
                setBooks(response.data);
                categorizeBooks(response.data);
            })
            .catch(error => {
                console.error("Error fetching books:", error);
                setError(true);
            });
    }, []);

    const categorizeBooks = (books) => {
        const categories = {};
        books.forEach(book => {
            if (!categories[book.category]) {
                categories[book.category] = [];
            }
            categories[book.category].push(book);
        });
        setCategorizedBooks(categories);
    };

    const handleViewDetails = (id) => {
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
                    ) : Object.keys(categorizedBooks).length > 0 ? (
                        Object.entries(categorizedBooks).map(([category, books]) => (
                            <div key={category} className="mb-10">
                                <h3 className="text-2xl font-semibold text-gray-800 mb-4">{category}</h3>
                                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                                    {books.map(book => (
                                        <div key={book.id} className="bg-gray-100 p-4 rounded-lg shadow-lg">
                                            <img
                                                src={book.coverUrl}
                                                alt={book.title}
                                                className="w-full h-48 object-cover rounded-lg"
                                            />
                                            <h2 className="text-lg font-bold mt-3 text-gray-800">{book.title}</h2>
                                            <p className="text-sm text-gray-600">by {book.author}</p>
                                            <button
                                                onClick={() => handleViewDetails(book.id)}
                                                className="mt-4 w-full px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                                            >
                                                View Details
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        ))
                    ) : (
                        <p className="text-center text-gray-600">
                            Your book collection will appear here after you upload books for summarization.
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Books;
