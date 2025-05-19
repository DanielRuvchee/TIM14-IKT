import React from 'react';
import { Book, FileText } from 'lucide-react';
import { Alert, AlertTitle, AlertDescription } from '@/components/ui/alert';

interface BookSummaryDisplayProps {
    summary: {
        title: string;
        author: string;
        summary: string;
    };
}

const BookSummaryDisplay = ({ summary }: BookSummaryDisplayProps) => {
    if (!summary) return null;

    // You can still add keyPoints if you like
    const keyPoints = [
        "Authentic relationships are crucial for personal growth",
        "Technology should enhance, not replace, human connection",
        "Failure is an essential part of the learning process",
        "Community support provides resilience in difficult times"
    ];

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
            </div>

            <Alert className="bg-turquoise/10 border-turquoise/20">
                <AlertTitle className="text-turquoise-dark">Key Takeaways</AlertTitle>
                <AlertDescription>
                    <ul className="list-disc pl-5 mt-2 space-y-1">
                        {keyPoints.map((point, i) => (
                            <li key={i} className="text-gray-700">{point}</li>
                        ))}
                    </ul>
                </AlertDescription>
            </Alert>
        </div>
    );
};

export default BookSummaryDisplay;
