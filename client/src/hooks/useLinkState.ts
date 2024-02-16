import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useFetch } from './useFetch';

export const useLinkState = <T>(url: string) => {
  const [data, setData] = useState<T | null>(null);
  const { state } = useLocation();
  const { get } = useFetch<T>(url);

  useEffect(() => {
    if (state) {
      return setData(state);
    }

    (async () => {
      const fetchedData = await get();
      setData(fetchedData);
    })();
  }, []);

  return { data, reset: () => setData(null) };
};
