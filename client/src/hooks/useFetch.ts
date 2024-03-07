import { useLocation } from 'react-router-dom';
import { UseFetch, UseFetchArgs, useFetch as useHttpFetch } from 'use-http';
import { useLocaleContext } from '../contexts/LocaleContext';
import { PagesEnum } from '../types/enums/PagesEnum';

export function useFetch<TData = any>(...args: UseFetchArgs): UseFetch<TData> {
  const { locale } = useLocaleContext();
  const { pathname } = useLocation();

  let url = args[0];
  if (url && typeof url === 'string') {
    if (!pathname.includes(PagesEnum.Admin)) {
      const urlAsUrl = new URL(url);
      urlAsUrl.searchParams.append('lang', locale);
      url = urlAsUrl.toString();
      args[0] = url;
    }
  }

  return useHttpFetch(...args);
}
