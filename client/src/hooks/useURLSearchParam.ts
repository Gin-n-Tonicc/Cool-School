import { useMemo } from 'react';
import { useLocation } from 'react-router-dom';

// The hook that allows us to keep the sate of one specific search param
export default function useUrlSearchParam(paramName: string) {
  const { search } = useLocation();

  const searchParam = useMemo(
    () => new URLSearchParams(search).get(paramName),
    [search]
  );

  return searchParam;
}
