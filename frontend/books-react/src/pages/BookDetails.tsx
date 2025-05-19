
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import Navbar from "@/components/Navbar";
import jsPDF from "jspdf"

interface Book {
    id: string;
    title: string;
    content: string;
    author: string;
}

const BookDetails: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [book, setBook] = useState<Book | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    useEffect(() => {
        if (!id) return;
        axios.get(`http://localhost:8081/api/books/${id}`)
            .then(response => {
                if (response.data) {
                    setBook(response.data);
                } else {
                    setError(true);
                }
                setLoading(false);
            })
            .catch(() => {
                setError(true);
                setLoading(false);
            });
    }, [id]);
    const handleDownload = () => {
        if (!book) return;

        const doc = new jsPDF();
        const pageHeight = doc.internal.pageSize.height;
        const margin = 15;

        const author = book.author ||  "No content available.";
        const title = book.title ||"Untitled";
        const content = book.content ||  "No content available.";

        // Set title
        doc.setFontSize(16);
        doc.text(title, margin, margin);
        doc.text(author, margin, margin +10);

        // Prepare content text
        doc.setFontSize(12);
        const lines = doc.splitTextToSize(content, 180); // 180 = page width with margin
        let cursorY = margin + 20;

        lines.forEach(line => {
            if (cursorY > pageHeight - margin) {
                doc.addPage();
                cursorY = margin;
            }
            doc.text(line, margin, cursorY);
            cursorY += 7;
        });

        doc.save(`${title}.pdf`);
    };

    if (loading) return <p className="text-center mt-10 text-gray-600">Loading...</p>;
    if (error || !book) return <p className="text-center mt-10 text-red-500">Book not found.</p>;

    return (
        <div className="min-h-screen bg-turquoise-light/30">
            <Navbar />
            <div className="max-w-4xl mx-auto px-4 py-10">
                <div className="bg-white p-6 rounded-lg shadow-lg">
                    <h1 className="text-3xl font-bold text-gray-800">{book.title}</h1>
                    <h2 className="mt-4 text-gray-700 whitespace-pre-wrap">{book.author}</h2>
                    <p className="mt-4 text-gray-700 whitespace-pre-wrap">{book.content}</p>
                </div>
                <button
                    onClick={handleDownload}
                    className="mt-6 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                >
                    Download
                </button>
            </div>

        </div>
    );
};

export default BookDetails;