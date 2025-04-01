
import React, { useState } from 'react';
import { Upload, Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';

const FileUploader = () => {
  const [isDragging, setIsDragging] = useState(false);
  const [file, setFile] = useState<File | null>(null);

  const handleDragEnter = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(true);
  };

  const handleDragLeave = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);
  };

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (!isDragging) {
      setIsDragging(true);
    }
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      const droppedFile = e.dataTransfer.files[0];
      setFile(droppedFile);
      console.log("File selected:", droppedFile.name);
      // Process file here
    }
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const selectedFile = e.target.files[0];
      setFile(selectedFile);
      console.log("File selected:", selectedFile.name);
      // Process file here
    }
  };

  return (
    <div className="mx-auto max-w-2xl mt-4">
      <div 
        className={`bg-white rounded-lg p-10 shadow-sm ${
          isDragging ? 'border-2 border-turquoise' : 'border border-gray-200'
        }`}
        onDragEnter={handleDragEnter}
        onDragLeave={handleDragLeave}
        onDragOver={handleDragOver}
        onDrop={handleDrop}
      >
        <div className="text-center">
          <p className="text-gray-400 mb-2">Drag and drop file to summarize</p>
          <p className="text-gray-400 mb-4">or</p>

          <label htmlFor="file-upload">
            <div className="inline-block">
              <Button 
                className="bg-turquoise hover:bg-turquoise-dark flex items-center space-x-1 px-4"
              >
                <Plus className="h-4 w-4" />
                <span>Choose a file</span>
              </Button>
              <input 
                id="file-upload"
                type="file"
                className="hidden" 
                onChange={handleFileSelect}
              />
            </div>
          </label>
        </div>
      </div>
    </div>
  );
};

export default FileUploader;
