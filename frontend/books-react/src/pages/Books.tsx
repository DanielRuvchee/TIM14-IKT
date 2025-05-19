// import React, { useEffect, useState } from "react";
// import axios from "axios";
// import { useNavigate } from "react-router-dom";
// import Navbar from "../components/Navbar";
//
// interface Book {
//     id: string;
//     title: string;
//     content: string;
// }
//
// const Books: React.FC = () => {
//     const [books, setBooks] = useState<Book[]>([]);
//     const [error, setError] = useState(false);
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         axios.get("http://localhost:8081/api/books/all")
//             .then(response => {
//                 console.log("API Response:", response.data);
//                 setBooks(response.data); // Just use the response directly
//             })
//             .catch(error => {
//                 console.error("Error fetching books:", error);
//                 setError(true);
//             });
//     }, []);
//
//     const handleViewDetails = (id: string) => {
//         navigate(`/books/${id}`);
//     };
//
//     return (
//         <div className="min-h-screen bg-turquoise-light/30">
//             <Navbar />
//
//             <div className="max-w-6xl mx-auto px-4 py-10">
//                 <h1 className="text-3xl font-bold text-center text-black mb-8">
//                     Book Collection
//                 </h1>
//
//                 <div className="bg-white rounded-lg p-8 shadow-sm">
//                     {error ? (
//                         <p className="text-center text-red-500">
//                             Failed to fetch books. Please check if the backend is running.
//                         </p>
//                     ) : books && books.length > 0 ? (
//                         <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
//                             {Array.isArray(books) ? books.map(book => (
//                                 <div key={book.id} className="bg-gray-100 p-4 rounded-lg shadow-lg">
//                                     <h2 className="text-lg font-bold mt-3 text-gray-800">{book.title}</h2>
//                                     <button
//                                         onClick={() => handleViewDetails(book.id)}
//                                         className="mt-4 w-full px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
//                                     >
//                                         View Details
//                                     </button>
//                                 </div>
//                             )) : <p>Error: Expected books to be an array</p>}
//                         </div>
//                     ) : (
//                         <p className="text-center text-gray-600">
//                             Your book collection will appear here after you upload books.
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
import Navbar from "../components/Navbar";

interface Book {
    id: string;
    title: string;
    author: string;
    category: string;
    coverUrl?: string;
    content: string;
}

const Books: React.FC = () => {
    const [books, setBooks] = useState<Book[]>([]);
    const [error, setError] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedCategory, setSelectedCategory] = useState("All");
    const [selectedAuthor, setSelectedAuthor] = useState("All");

    const navigate = useNavigate();

    useEffect(() => {
        axios.get("http://localhost:8081/api/books/all")
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

    const categories = ["All", ...Array.from(new Set(books.map(book => book.category)))];
    const authors = ["All", ...Array.from(new Set(books.map(book => book.author)))];

    const filteredBooks = books.filter(book => {
        const matchesSearch = book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
            book.author?.toLowerCase().includes(searchTerm.toLowerCase());

        const matchesCategory = selectedCategory === "All" || book.category === selectedCategory;
        const matchesAuthor = selectedAuthor === "All" || book.author === selectedAuthor;

        return matchesSearch && matchesCategory && matchesAuthor;
    });

    const categorizedBooks = filteredBooks.reduce((acc, book) => {
        acc[book.category] = acc[book.category] || [];
        acc[book.category].push(book);
        return acc;
    }, {} as { [key: string]: Book[] });

    return (
        <div className="min-h-screen bg-turquoise-light/30">
            <Navbar />

            <div className="max-w-6xl mx-auto px-4 py-10">
                <h1 className="text-3xl font-bold text-center text-black mb-8">
                    Book Collection
                </h1>

                {/* Filters */}
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
                    <input
                        type="text"
                        placeholder="Search by title or author..."
                        className="w-full px-4 py-2 border border-gray-300 rounded-md"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />

                    <select
                        value={selectedCategory}
                        onChange={(e) => setSelectedCategory(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-md"
                    >
                        {categories.map(category => (
                            <option key={category} value={category}>{category}</option>
                        ))}
                    </select>

                    <select
                        value={selectedAuthor}
                        onChange={(e) => setSelectedAuthor(e.target.value)}
                        className="w-full px-4 py-2 border border-gray-300 rounded-md"
                    >
                        {authors.map(author => (
                            <option key={author} value={author}>{author}</option>
                        ))}
                    </select>
                </div>

                <div className="bg-white rounded-lg p-8 shadow-sm">
                    {error ? (
                        <p className="text-center text-red-500">
                            Failed to fetch books. Please check if the backend is running.
                        </p>
                    ) : filteredBooks.length > 0 ? (
                        Object.entries(categorizedBooks).map(([category, books]) => (
                            <div key={category} className="mb-8">
                                <h2 className="text-xl font-semibold text-turquoise-light mb-4">{category}</h2>
                                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
                                    {books.map(book => (
                                        <div key={book.id} className="bg-gray-100 p-4 rounded-lg shadow-lg">
                                            {book.coverUrl && (
                                                <img
                                                    src={book.coverUrl}
                                                    alt={book.title}
                                                    className="h-48 w-full object-cover rounded-md mb-2"
                                                />
                                            )}
                                            <h3 className="text-lg font-bold text-gray-800">{book.title}</h3>
                                            <p className="text-sm text-gray-600 mb-2">{book.author}</p>
                                            <button
                                                onClick={() => handleViewDetails(book.id)}
                                                className="mt-2 w-full px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
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
                            No books match your filters.
                        </p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Books;
