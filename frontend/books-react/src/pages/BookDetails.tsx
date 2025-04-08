import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Navbar from "@/components/Navbar";

interface Book {
    id: string;
    title: string;
    author: string;
    description: string;
    coverUrl: string;
    pdfUrl: string;
}

const mockBooks: Book[] = [
    {
        id: "1",
        title: "Crime and Punishment",
        author: "Fyodor Dostoevsky",
        description: "Crime and Punishment follows the mental anguish and moral dilemmas of Rodion Raskolnikov, an impoverished ex-student in Saint Petersburg who plans to kill an unscrupulous pawnbroker, an old woman who stores money and valuable objects in her flat. He theorises that with the money he could liberate himself from poverty and go on to perform great deeds, and seeks to convince himself that certain crimes are justifiable if they are committed in order to remove obstacles to the higher goals of \"extraordinary\" men. Once the deed is done, however, he finds himself wracked with confusion, paranoia, and disgust. His theoretical justifications lose all their power as he struggles with guilt and horror and is confronted with both internal and external consequences of his deed.",
        coverUrl: "https://upload.wikimedia.org/wikipedia/en/4/4b/Crimeandpunishmentcover.png",
        pdfUrl: "/mock-books/crime-and-punishment.pdf",
    },

];

const BookDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [book, setBook] = useState<Book | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<boolean>(false);

    useEffect(() => {
        const mockBook = mockBooks.find((b) => b.id === id);
        if (mockBook) {
            setBook(mockBook);
            setLoading(false);
        } else {
            setError(true);
            setLoading(false);
        }
    }, [id]);

    if (loading) return <p className="text-center mt-10 text-gray-600">Loading...</p>;
    if (error) return <p className="text-center mt-10 text-red-500">Book not found.</p>;

    return (
        <div className="min-h-screen bg-turquoise-light/30">
            <Navbar />
            <div className="max-w-4xl mx-auto px-4 py-10 flex items-center">
                <div className="bg-white p-6 rounded-lg shadow-lg flex">
                    <img src={book?.coverUrl} alt={book?.title} className="w-60 h-auto rounded-lg shadow-lg" />
                    <div className="ml-6">
                        <h1 className="text-3xl font-bold text-gray-800">{book?.title}</h1>
                        <h2 className="text-lg text-gray-600">by {book?.author}</h2>
                        <p className="mt-4 text-gray-700">{book?.description}</p>
                        <a
                            href={book?.pdfUrl}
                            download
                            className="mt-4 inline-block px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                        >
                            Download as PDF
                        </a>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default BookDetails;