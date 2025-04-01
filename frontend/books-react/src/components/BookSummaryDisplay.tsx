
import React from 'react';
import { Book, FileText } from 'lucide-react';
import { Alert, AlertTitle, AlertDescription } from '@/components/ui/alert';

interface BookSummaryDisplayProps {
  fileName: string;
}

const BookSummaryDisplay = ({ fileName }: BookSummaryDisplayProps) => {
  // This would normally come from an API after processing the file
  // For now, we'll use dummy text
  const summary = {
    title: fileName.replace(/\.[^/.]+$/, ""), // Remove file extension
    author: "Unknown Author",
    summary: `This is an AI-generated summary of the book "${fileName.replace(/\.[^/.]+$/, "")}". 
    
    The book explores themes of human connection in the digital age. The main character embarks on a journey of self-discovery through various challenges and encounters with different people.
    
    The story highlights important lessons about perseverance, friendship, and the impact of technology on our lives. Through vivid descriptions and compelling dialogue, the author creates a rich narrative that engages readers from beginning to end.
    
    Key insights from the book include:
    - The importance of authentic relationships
    - Finding balance in a technology-driven world
    - Learning from failure and embracing vulnerability
    - The power of community and shared experiences
    
    The conclusion ties together these themes, leaving readers with thought-provoking questions about their own lives and relationships.`,
    keyPoints: [
      "Authentic relationships are crucial for personal growth",
      "Technology should enhance, not replace, human connection",
      "Failure is an essential part of the learning process",
      "Community support provides resilience in difficult times"
    ]
  };

  return (
    <div className="mt-8 bg-white rounded-lg p-6 shadow-sm">
      <div className="flex items-center mb-6">
        <Book className="h-6 w-6 text-turquoise mr-2" />
        <h2 className="text-xl text-gray-800 font-semibold">Summary for: {summary.title}</h2>
      </div>
      
      <div className="mb-6">
        <p className="text-sm text-gray-500 mb-1">Book by {summary.author}</p>
      </div>
      
      <div className="border-t border-gray-100 pt-4 mb-6">
        <h3 className="flex items-center text-lg font-medium text-gray-700 mb-3">
          <FileText className="h-5 w-5 text-turquoise mr-2" />
          Summary
        </h3>
        <div className="text-gray-600 whitespace-pre-line">
          {summary.summary}
        </div>
      </div>
      
      <Alert className="bg-turquoise/10 border-turquoise/20">
        <AlertTitle className="text-turquoise-dark">Key Takeaways</AlertTitle>
        <AlertDescription>
          <ul className="list-disc pl-5 mt-2 space-y-1">
            {summary.keyPoints.map((point, index) => (
              <li key={index} className="text-gray-700">{point}</li>
            ))}
          </ul>
        </AlertDescription>
      </Alert>
    </div>
  );
};

export default BookSummaryDisplay;