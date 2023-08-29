import { Link, useLocation } from 'react-router-dom';

export interface HeaderNavItemProps {
  text: string;
  pathName: string;
}

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
