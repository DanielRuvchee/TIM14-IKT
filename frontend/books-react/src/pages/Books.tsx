
import React from 'react';
import Navbar from '@/components/Navbar';

const Books = () => {
  return (
    <div className="min-h-screen bg-turquoise-light/30">
      <Navbar />
      
      <div className="max-w-5xl mx-auto px-4 py-10">
        <h1 className="text-3xl font-bold text-center text-white mb-8">
          Book Collection
        </h1>
        
        <div className="bg-white rounded-lg p-8 shadow-sm">
          <p className="text-center text-gray-600">
            Your book collection will appear here after you upload books for summarization.
          </p>
        </div>
      </div>
    </div>
  );
};

export default Books;
