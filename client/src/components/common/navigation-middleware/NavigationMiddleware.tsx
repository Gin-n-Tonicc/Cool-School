import { PropsWithChildren, useEffect, useMemo } from 'react';
import { useLocation } from 'react-router-dom';
import { PagesEnum } from '../../../types/enums/PagesEnum';

export default function NavigationMiddleware(props: PropsWithChildren) {
  const { pathname } = useLocation();
  const excludedPages = useMemo(() => [PagesEnum.Blog.toString()], []);

  useEffect(() => {
    if (excludedPages.includes(pathname)) {
      return;
    }

    global.window.scrollTo(0, 0);
  }, [pathname]);

  return <>{props.children}</>;
}
