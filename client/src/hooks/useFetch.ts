import { UseFetch, UseFetchArgs, useFetch as useHttpFetch } from 'use-http';
import { useLocaleContext } from '../contexts/LocaleContext';

export function useFetch<TData = any>(...args: UseFetchArgs): UseFetch<TData> {
  const { locale } = useLocaleContext();

  let url = args[0];
  if (url && typeof url === 'string') {
    const urlAsUrl = new URL(url);
    urlAsUrl.searchParams.append('lang', locale);
    url = urlAsUrl.toString();
    args[0] = url;
  }

  return useHttpFetch(...args);
}
