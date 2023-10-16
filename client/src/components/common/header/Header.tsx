import { useMemo } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';
import logo from '../../../images/logo.png';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import './Header.scss';
import HeaderNavItem from './header-nav-item/HeaderNavItem';

function GuestLinks() {
  return (
    <>
      <HeaderNavItem text="Login" pathName={PagesEnum.Login} />
      <HeaderNavItem text="Register" pathName={PagesEnum.Register} />
    </>
  );
}

function UserLinks() {
  return (
    <>
      <HeaderNavItem text="Logout" pathName={PagesEnum.Logout} />
    </>
  );
}

export default function Header() {
  const { isAuthenticated } = useAuthContext();

  const homeMenuPaths = useMemo(
    () =>
      [
        PagesEnum.Home,
        PagesEnum.Login,
        PagesEnum.Register,
        PagesEnum.Logout,
        PagesEnum.Admin,
      ].map((x) => x.toString()),
    []
  );

  const location = useLocation();

  // if (PagesEnum.Admin.toString().includes(location.pathname)) {
  //   return <></>;
  // }

  let headerClasses = 'main_menu ';

  if (homeMenuPaths.includes(location.pathname)) {
    headerClasses += 'home_menu';
  } else {
    headerClasses += 'single_page_menu';
  }

  return (
    <>
      <header className={headerClasses}>
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-12">
              <nav className="navbar navbar-expand-lg navbar-light">
                <Link to={PagesEnum.Home} className="navbar-brand">
                  <img className="site-logo" src={logo} alt="logo" />
                </Link>
                <button
                  className="navbar-toggler"
                  type="button"
                  data-toggle="collapse"
                  data-target="#navbarSupportedContent"
                  aria-controls="navbarSupportedContent"
                  aria-expanded="false"
                  aria-label="Toggle navigation">
                  <span className="navbar-toggler-icon"></span>
                </button>

                <div
                  className="collapse navbar-collapse main-menu-item justify-content-end"
                  id="navbarSupportedContent">
                  <ul className="navbar-nav align-items-center">
                    <HeaderNavItem text="Home" pathName="/" />
                    <HeaderNavItem text="Courses" pathName="/courses" />
                    <HeaderNavItem text="Blog" pathName="/blog" />
                    <HeaderNavItem text="Contact" pathName="/contact" />
                    <HeaderNavItem text="Admin" pathName="/admin" />
                    {isAuthenticated ? <UserLinks /> : <GuestLinks />}
                  </ul>
                </div>
              </nav>
            </div>
          </div>
        </div>
      </header>
    </>
  );
}
