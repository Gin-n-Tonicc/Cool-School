import { useMemo } from 'react';
import { useLocation } from 'react-router-dom';

export default function useUrlSearchParam(paramName: string) {
  const { search } = useLocation();

  const searchParam = useMemo(
    () => new URLSearchParams(search).get(paramName),
    [search]
  );

  return searchParam;
}
