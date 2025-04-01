
import React from 'react';
import { Link } from 'react-router-dom';
import { BookOpen } from 'lucide-react';

const Navbar = () => {
  return (
    <nav className="bg-turquoise-dark text-white py-4 px-6">
      <div className="max-w-7xl mx-auto flex items-center">
        <BookOpen className="h-6 w-6 mr-3" />
        
        <div className="flex space-x-8">
          <Link 
            to="/" 
            className="text-white hover:text-white/80 font-medium text-lg"
          >
            Summary
          </Link>
          <Link 
            to="/books" 
            className="text-white hover:text-white/80 font-medium text-lg"
          >
            Books
          </Link>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
