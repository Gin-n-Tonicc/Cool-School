import { Link, useLocation } from 'react-router-dom';

export interface HeaderNavItemProps {
  text: string;
  pathName: string;
}

// The component that displays a single navbar link
// which change styles based on the location the client is on
export default function HeaderNavItem(props: HeaderNavItemProps) {
  const location = useLocation();
  let classNames = 'nav-item ';

  if (location.pathname === props.pathName) {
    classNames += 'active';
  }

  return (
    <li className={classNames}>
      <Link className="nav-link" to={props.pathName}>
        {props.text}
      </Link>
    </li>
  );
}
