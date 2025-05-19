import React from 'react';
import { Book, FileText } from 'lucide-react';

import jsPDF from "jspdf";

interface BookSummaryDisplayProps {
    summary: {
        title: string;
        author: string;
        summary: string;
    };
}


const BookSummaryDisplay = ({ summary }: BookSummaryDisplayProps) => {

    const handleDownload = () => {
        if (!summary) return;

        const doc = new jsPDF();
        const pageHeight = doc.internal.pageSize.height;
        const margin = 15;

        const title = summary.title||"Untitled";
        const content = summary.summary||"No content available.";
        const author = summary.author||"No content available.";

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
    if (!summary) return null;

    // You can still add keyPoints if you like
    return (
        <div className="mt-8 bg-white rounded-lg p-6 shadow-sm">
            <div className="flex items-center mb-6">
                <Book className="h-6 w-6 text-turquoise mr-2" />
                <h2 className="text-xl text-gray-800 font-semibold">
                    Summary for: {summary.title}
                </h2>
            </div>

            <p className="text-sm text-gray-500 mb-6">
                Book by {summary.author}
            </p>

            <div className="border-t border-gray-100 pt-4 mb-6">
                <h3 className="flex items-center text-lg font-medium text-gray-700 mb-3">
                    <FileText className="h-5 w-5 text-turquoise mr-2" />
                    Summary
                </h3>
                <div className="text-gray-600 whitespace-pre-line">
                    {summary.summary}
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

export default BookSummaryDisplay;
