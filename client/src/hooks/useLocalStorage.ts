import { useState } from 'react';

// The hook that stores a value to the local storage and saving it as a state
// allowing us to both update the local storage and the state at the same time
export const useLocalStorage = <T>(key: string, initialValue: Partial<T>) => {
  type K = typeof initialValue;

  // On load try to load local storage value
  // otherwise load the given initial value
  const [value, setValue] = useState<K>(() => {
    const storageData = localStorage.getItem(key);

    if (!storageData) {
      localStorage.setItem(key, JSON.stringify(initialValue));
      return initialValue;
    }

    return JSON.parse(storageData);
  });

  // Set storage data handler
  const setStorageData = (value: K | ((val: K) => K)) => {
    if (!value) return;

    // State set based on previous value
    if (value instanceof Function) {
      return setValue((oldValue: K) => {
        const result = value(oldValue);
        localStorage.setItem(key, JSON.stringify(result));
        return result;
      });
    }

    // Default state set
    localStorage.setItem(key, JSON.stringify(value));
    setValue(value);
  };

  return { value, setStorageData };
};
