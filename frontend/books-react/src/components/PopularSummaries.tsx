
import React from 'react';
import { Book } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useBookSummaries } from '@/hooks/useBookSummaries';
import { BookSummary } from '@/types';

const PopularSummaries = () => {
  const { data: popularBooks, isLoading, error } = useBookSummaries();

  return (
    <div className="max-w-5xl mx-auto mt-10 bg-white rounded-lg p-6 shadow-sm">
      <div className="flex items-center mb-4">
        <Book className="h-6 w-6 text-gray-600 mr-2" />
        <h2 className="text-xl text-gray-700 font-medium">Popular Book Summaries</h2>
      </div>
      
      {isLoading && (
        <div className="text-center py-8">
          <p className="text-gray-500">Loading popular summaries...</p>
        </div>
      )}

      {error && (
        <div className="text-center py-8">
          <p className="text-red-500">Error loading summaries. Please try again later.</p>
        </div>
      )}
      
      {popularBooks && popularBooks.length > 0 && (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6 mt-4">
          {popularBooks.map((book: BookSummary) => (
            <div key={book.id} className="flex flex-col items-center">
              <div className="w-36 h-48 relative mb-2">
                <img 
                  src={book.coverImage} 
                  alt={book.title}
                  className="w-full h-full object-cover rounded"
                />
              </div>
              <h3 className="font-medium text-center text-gray-800 mt-2">{book.title}</h3>
              <p className="text-sm text-gray-500 text-center">{book.author}</p>
              <Button 
                className="bg-turquoise hover:bg-turquoise-dark text-white rounded-full mt-2"
              >
                Click to view
              </Button>
            </div>
          ))}
        </div>
      )}

      {popularBooks && popularBooks.length === 0 && (
        <div className="text-center py-8">
          <p className="text-gray-500">No popular summaries available at the moment.</p>
        </div>
      )}
    </div>
  );
};

export default PopularSummaries;
