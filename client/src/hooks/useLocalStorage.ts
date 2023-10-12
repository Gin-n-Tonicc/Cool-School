import { useState } from 'react';

export const useLocalStorage = <T>(key: string, initialValue: Partial<T>) => {
  type K = typeof initialValue;

  const [value, setValue] = useState<K>(() => {
    const storageData = localStorage.getItem(key);

    if (!storageData) {
      localStorage.setItem(key, JSON.stringify(initialValue));
      return initialValue;
    }

    return JSON.parse(storageData);
  });

  const setStorageData = (value: K | ((val: K) => K)) => {
    if (!value) return;

    if (value instanceof Function) {
      return setValue((oldValue: K) => {
        const result = value(oldValue);
        localStorage.setItem(key, JSON.stringify(result));
        return result;
      });
    }

    localStorage.setItem(key, JSON.stringify(value));
    setValue(value);
  };

  return { value, setStorageData };
};
