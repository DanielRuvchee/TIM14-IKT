import React, { useRef, useState } from 'react';
import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';

const FileUploader = () => {
  const [isDragging, setIsDragging] = useState(false);
  const [file, setFile] = useState<File | null>(null);
  const [summary, setSummary] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

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
    setIsDragging(true);
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setIsDragging(false);

    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      const droppedFile = e.dataTransfer.files[0];
      setFile(droppedFile);
      uploadFile(droppedFile);
    }
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      const selectedFile = e.target.files[0];
      setFile(selectedFile);
      uploadFile(selectedFile);
    }
  };

  const handleButtonClick = () => {
    fileInputRef.current?.click();
  };

  const uploadFile = async (file: File) => {
    const formData = new FormData();
    formData.append('file', file);

    setSummary(null);
    setError(null);

    try {
      const response = await fetch('http://localhost:8080`/api/summarize/pdf', {
        method: 'POST',
        body: formData,
      });

      if (!response.ok) {
        throw new Error(`Upload failed: ${response.statusText}`);
      }

      const result = await response.text();
      setSummary(result);
    } catch (err: any) {
      setError(err.message || 'Failed to upload and summarize file.');
    }
  };

  return (
      <div className="mx-auto max-w-2xl mt-4">
        <div
            className={`bg-white rounded-lg p-10 shadow-sm transition-all ${
                isDragging ? 'border-2 border-turquoise bg-blue-50' : 'border border-gray-200'
            }`}
            onDragEnter={handleDragEnter}
            onDragLeave={handleDragLeave}
            onDragOver={handleDragOver}
            onDrop={handleDrop}
        >
          <div className="text-center">
            <p className="text-gray-400 mb-2">Drag and drop a PDF to summarize</p>
            <p className="text-gray-400 mb-4">or</p>

            <div className="flex justify-center">
              <Button
                  type="button"
                  onClick={handleButtonClick}
                  className="bg-turquoise hover:bg-turquoise-dark flex items-center space-x-1 px-4"
              >
                <Plus className="h-4 w-4" />
                <span>Choose a file</span>
              </Button>
            </div>

            <input
                type="file"
                accept="application/pdf"
                ref={fileInputRef}
                onChange={handleFileSelect}
                className="hidden"
            />

            {file && (
                <p className="mt-4 text-sm text-gray-600">
                  Selected: <strong>{file.name}</strong>
                </p>
            )}
          </div>
        </div>

        {summary && (
            <div className="mt-6 bg-gray-100 p-4 rounded shadow">
              <h3 className="text-lg font-semibold mb-2 text-gray-800">Summary</h3>
              <p className="text-gray-700 whitespace-pre-wrap">{summary}</p>
            </div>
        )}

        {error && (
            <div className="mt-6 text-red-500 text-center">
              <p>Error: {error}</p>
            </div>
        )}
      </div>
  );
};

export default FileUploader;
