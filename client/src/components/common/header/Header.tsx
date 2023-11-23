import { useMemo } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';
import logo from '../../../images/logo.png';
import { AdminPagesEnum } from '../../../types/enums/AdminPagesEnum';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { RolesEnum } from '../../../types/enums/RolesEnum';
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

function UserLinks(props: { isTeacher: boolean }) {
  return (
    <>
      {props.isTeacher && (
        <HeaderNavItem
          text="Create Course"
          pathName={PagesEnum.CoursesCreate}
        />
      )}
      <HeaderNavItem text="Create Blog" pathName={PagesEnum.BlogCreate} />
      <HeaderNavItem text="Logout" pathName={PagesEnum.Logout} />
    </>
  );
}

function UserNav(props: { isAuthenticated: boolean; isTeacher: boolean }) {
  return (
    <ul className="navbar-nav align-items-center">
      <HeaderNavItem text="Home" pathName={PagesEnum.Home} />
      <HeaderNavItem text="Contact" pathName={PagesEnum.Contact} />
      <HeaderNavItem text="Courses" pathName={PagesEnum.Courses} />
      <HeaderNavItem text="Blog" pathName={PagesEnum.Blog} />
      {props.isAuthenticated ? <UserLinks {...props} /> : <GuestLinks />}
    </ul>
  );
}

function AdminNav() {
  return (
    <ul className="navbar-nav align-items-center">
      <HeaderNavItem
        text="Users"
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.USERS}`}
      />
      <HeaderNavItem
        text="Categories"
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.CATEGORIES}`}
      />
      <HeaderNavItem
        text="Blogs"
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.BLOGS}`}
      />
      <HeaderNavItem
        text="Courses"
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.COURSES}`}
      />
      <HeaderNavItem
        text="Subsections"
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.SUBSECTIONS}`}
      />
      <HeaderNavItem
        text="Resources"
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.RESOURCES}`}
      />
    </ul>
  );
}

export default function Header() {
  const { user, isAuthenticated } = useAuthContext();
  const isTeacher = RolesEnum.TEACHER === user.role;

  const homeMenuPaths = useMemo(
    () =>
      [
        PagesEnum.Home,
        PagesEnum.Login,
        PagesEnum.Register,
        PagesEnum.Logout,
        PagesEnum.BlogCreate,
        PagesEnum.CoursesCreate,
        PagesEnum.Admin,
        PagesEnum.NotFound,
        `${PagesEnum.Admin}/${AdminPagesEnum.USERS}`,
        `${PagesEnum.Admin}/${AdminPagesEnum.CATEGORIES}`,
        `${PagesEnum.Admin}/${AdminPagesEnum.BLOGS}`,
        `${PagesEnum.Admin}/${AdminPagesEnum.COURSES}`,
        `${PagesEnum.Admin}/${AdminPagesEnum.SUBSECTIONS}`,
        `${PagesEnum.Admin}/${AdminPagesEnum.RESOURCES}`,
      ].map((x) => x.toString()),
    []
  );

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
                    {location.pathname.includes(PagesEnum.Admin) ? (
                      <AdminNav />
                    ) : (
                      <UserNav
                        isTeacher={isTeacher}
                        isAuthenticated={isAuthenticated}
                      />
                    )}
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
