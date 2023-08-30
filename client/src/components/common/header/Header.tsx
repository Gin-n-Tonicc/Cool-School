import { useMemo } from 'react';
import { Link, useLocation } from 'react-router-dom';
import logo from '../../../images/logo.png';
import './Header.scss';
import HeaderNavItem from './header-nav-item/HeaderNavItem';

export default function Header() {
  const homeMenuPaths = useMemo(() => ['/'], []);
  const location = useLocation();

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
                <Link to="/" className="navbar-brand">
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

                    {/* TODO: FINISH PAGES IN FUTURE */}
                    {/* <li className="nav-item dropdown">
                      <a
                        className="nav-link dropdown-toggle"
                        href="blog.html"
                        id="navbarDropdown"
                        role="button"
                        data-toggle="dropdown"
                        aria-haspopup="true"
                        aria-expanded="false">
                        Pages
                      </a>
                      <div
                        className="dropdown-menu"
                        aria-labelledby="navbarDropdown">
                        <a className="dropdown-item" href="single-blog.html">
                          Single blog
                        </a>
                        <a className="dropdown-item" href="elements.html">
                          Elements
                        </a>
                      </div>
                    </li> */}
                    <HeaderNavItem text="Contact" pathName="/contact" />
                    <li className="d-none d-lg-block">
                      <a className="btn_1" href="#">
                        Get a Quote
                      </a>
                    </li>
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
