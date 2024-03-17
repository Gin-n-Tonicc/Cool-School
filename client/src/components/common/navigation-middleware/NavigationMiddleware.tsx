import { PropsWithChildren, useEffect, useMemo } from 'react';
import { useLocation } from 'react-router-dom';
import { PagesEnum } from '../../../types/enums/PagesEnum';

// The component used as a middleware to perform
// an action whenever the client navigates
export default function NavigationMiddleware(props: PropsWithChildren) {
  const { pathname } = useLocation();
  const excludedPages = useMemo(() => [PagesEnum.Blog.toString()], []);

  // Scroll to the top of the page
  // unless includes an excluded page
  useEffect(() => {
    if (excludedPages.includes(pathname)) {
      return;
    }

    global.window.scrollTo(0, 0);
  }, [pathname]);

  return <>{props.children}</>;
}
