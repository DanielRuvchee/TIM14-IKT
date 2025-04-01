
import { useQuery } from '@tanstack/react-query';
import { BookSummary } from '@/types';

// This would be replaced with actual API fetch in a production environment
const fetchPopularBookSummaries = async (): Promise<BookSummary[]> => {
  // Simulating database fetch with a timeout
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        {
          id: 1,
          title: "Crime and Punishment",
          author: "Fyodor Dostoevsky",
          coverImage: "/lovable-uploads/86e63297-d924-49db-94cf-4c85d53cb72b.png",
        },
        {
          id: 2,
          title: "Decameron",
          author: "Giovanni Boccaccio",
          coverImage: "/lovable-uploads/86e63297-d924-49db-94cf-4c85d53cb72b.png",
        },
        {
          id: 3,
          title: "Waiting for Godot",
          author: "Samuel Beckett",
          coverImage: "/lovable-uploads/86e63297-d924-49db-94cf-4c85d53cb72b.png",
        },
        {
          id: 4,
          title: "Don Quixote",
          author: "Miguel de Cervantes",
          coverImage: "/lovable-uploads/86e63297-d924-49db-94cf-4c85d53cb72b.png",
        },
      ]);
    }, 1000);
  });
};

export const useBookSummaries = () => {
  return useQuery({
    queryKey: ['popularBookSummaries'],
    queryFn: fetchPopularBookSummaries,
  });
};
