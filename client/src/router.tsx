import { RouteObject, createBrowserRouter } from 'react-router-dom';
import Blog from './components/blog/Blog';
import Home from './components/home/Home';

const routes: RouteObject[] = [
  {
    path: '/',
    element: <Home />,
    children: [
      {
        path: 'blog',
        element: <Blog />,
      },
    ],
  },
];

export default createBrowserRouter(routes);
