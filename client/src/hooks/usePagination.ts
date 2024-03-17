import { useCallback, useEffect, useMemo, useState } from 'react';
import * as paginationUtils from '../utils/page';

type TogglePageFunction = (page: number) => void;
type SwitchPageFunction = () => void;

export interface PaginationProps {
  pages: number;
  currentPage: number;
  togglePage: TogglePageFunction;
  previousPage: SwitchPageFunction;
  nextPage: SwitchPageFunction;
}

// The hook that paginates a list to our likings
export function usePagination<T>(
  filteredList: T[] | undefined,
  pageSize: number
) {
  // Prepare state
  const [list, setList] = useState<T[]>([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [pages, setPages] = useState(1);

  // Update list
  useEffect(() => {
    if (!filteredList) {
      return;
    }

    setList(
      filteredList.slice((currentPage - 1) * pageSize, currentPage * pageSize)
    );
  }, [filteredList, currentPage, pageSize]);

  // Update pages
  useEffect(() => {
    if (!filteredList) {
      return;
    }

    const pages = Math.ceil(filteredList.length / pageSize);
    setPages(pages);
  }, [filteredList, pageSize]);

  // Keep pages value in bounds (v >= 1 && v <= max)
  const validatePage = useMemo(
    () => paginationUtils.validatePage.bind(null, pages),
    [pages]
  );

  // Toggle page by specific number
  const togglePage = useCallback(
    (page: number) => {
      setCurrentPage(validatePage(page));
    },
    [setCurrentPage, validatePage]
  );

  // Go to previous page
  const previousPage = useCallback(() => {
    setCurrentPage((currentPage) => validatePage(currentPage - 1));
  }, [setCurrentPage, validatePage]);

  // Go to next page
  const nextPage = useCallback(() => {
    setCurrentPage((currentPage) => validatePage(currentPage + 1));
  }, [setCurrentPage, validatePage]);

  return {
    list: [...list],
    pages,
    currentPage,
    validatePage,
    togglePage,
    previousPage,
    nextPage,
  };
}
