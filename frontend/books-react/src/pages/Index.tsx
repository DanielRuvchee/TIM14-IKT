//
// import React from 'react';
// import Navbar from '@/components/Navbar';
// import FileUploader from '@/components/FileUploader';
// import PopularSummaries from '@/components/BookSummaryDisplay';
//
// const Index = () => {
//   return (
//     <div className="min-h-screen bg-turquoise-light/30">
//       <Navbar />
//
//       <div className="max-w-5xl mx-auto px-4 pt-16 pb-24">
//         <h1 className="text-3xl font-bold text-center text-white mb-8">
//           AI Book Summarizer - Summarize Your Books
//         </h1>
//
//         <FileUploader />
//
//         <PopularSummaries fileName={''} />
//       </div>
//     </div>
//   );
// };
//
// export default Index;
import React, { useState } from 'react';
import Navbar from '@/components/Navbar';
import FileUploader from '@/components/FileUploader';
import BookSummaryDisplay from '@/components/BookSummaryDisplay';


const Index = () => {
    const [summaryData, setSummaryData] = useState<null | {
        title: string;
        author: string;
        summary: string;
    }>(null);

    return (
        <div className="min-h-screen bg-turquoise-light/30">
            <Navbar />

            <div className="max-w-5xl mx-auto px-4 pt-16 pb-24">
                <h1 className="text-3xl font-bold text-center text-black mb-8">
                    AI Book Summarizer - Summarize Your Books
                </h1>

                <FileUploader onSummaryReady={setSummaryData} />

                {summaryData && <BookSummaryDisplay summary={summaryData} />}
            </div>
        </div>
    );
};

export default Index;
