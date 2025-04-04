import React from "react";

const BookCard = ({ book, onViewDetails }) => {
    return (
        <div className="bg-white rounded-lg shadow-md overflow-hidden w-40">
            <img
                src={book.coverUrl}
                alt={book.title}
                className="w-full h-40 object-cover"
            />
            <div className="p-3">
                <h4 className="text-sm font-semibold text-gray-800 truncate">{book.title}</h4>
                <button
                    onClick={() => onViewDetails(book.id)}
                    className="mt-2 w-full px-3 py-1 bg-blue-500 text-white text-xs font-medium rounded-md hover:bg-blue-600 transition"
                >
                    View Details
                </button>
            </div>
        </div>
    );
};

export default BookCard;
