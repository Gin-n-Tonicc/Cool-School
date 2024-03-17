import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useFetch } from './useFetch';

// The hook that gets the data from location state (if exists)
// but if that data doesn't exist it will fetch it from the server
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
