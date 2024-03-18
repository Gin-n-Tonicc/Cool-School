import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useLocation } from 'react-router-dom';
import { useAuthContext } from '../../../contexts/AuthContext';
import logo from '../../../images/logo.png';
import { AdminPagesEnum } from '../../../types/enums/AdminPagesEnum';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { RolesEnum } from '../../../types/enums/RolesEnum';
import './Header.scss';
import HeaderNavItem from './header-nav-item/HeaderNavItem';
import LanguagePicker from './language-picker/LanguagePicker';

// The component that displays all of the guests links
function GuestLinks() {
  const { t } = useTranslation();

  return (
    <>
      <HeaderNavItem text={t('navbar.login')} pathName={PagesEnum.Login} />
      <HeaderNavItem
        text={t('navbar.register')}
        pathName={PagesEnum.Register}
      />
    </>
  );
}

// The component that displays all of the user common and condition links
// changes based on if the user has finished oauth2 or not
// changes based on if the user is teacher or is not a teacher
function UserLinks(props: { isTeacher: boolean; hasFinishedOAuth2: boolean }) {
  const { t } = useTranslation();

  return (
    <>
      {props.hasFinishedOAuth2 ? (
        <>
          {props.isTeacher && (
            <HeaderNavItem
              text={t('courses.create.button')}
              pathName={PagesEnum.CoursesCreate}
            />
          )}
          <HeaderNavItem
            text={t('blogs.create.button')}
            pathName={PagesEnum.BlogCreate}
          />
        </>
      ) : (
        <HeaderNavItem
          text={t('navbar.finish.register')}
          pathName={PagesEnum.FinishRegister}
        />
      )}
      <HeaderNavItem text={t('navbar.logout')} pathName={PagesEnum.Logout} />
    </>
  );
}

// The component that displays all of the common links
// and auth specific links (user or guest)
function UserNav(props: {
  isAuthenticated: boolean;
  isTeacher: boolean;
  hasFinishedOAuth2: boolean;
}) {
  const { t } = useTranslation();

  return (
    <ul className="navbar-nav align-items-center">
      <HeaderNavItem text={t('navbar.home')} pathName={PagesEnum.Home} />
      <HeaderNavItem text={t('navbar.contact')} pathName={PagesEnum.Contact} />
      <HeaderNavItem text={t('navbar.courses')} pathName={PagesEnum.Courses} />
      <HeaderNavItem text={t('navbar.blogs')} pathName={PagesEnum.Blog} />
      {props.isAuthenticated ? <UserLinks {...props} /> : <GuestLinks />}
    </ul>
  );
}

// The component that displays all of the admin links
function AdminNav() {
  const { t } = useTranslation();

  return (
    <ul className="navbar-nav align-items-center">
      <HeaderNavItem
        text={t('admin.users')}
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.USERS}`}
      />
      <HeaderNavItem
        text={t('admin.categories')}
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.CATEGORIES}`}
      />
      <HeaderNavItem
        text={t('admin.blogs')}
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.BLOGS}`}
      />
      <HeaderNavItem
        text={t('admin.courses')}
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.COURSES}`}
      />
      <HeaderNavItem
        text={t('admin.subsections')}
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.SUBSECTIONS}`}
      />
      <HeaderNavItem
        text={t('admin.resources')}
        pathName={`${PagesEnum.Admin}/${AdminPagesEnum.RESOURCES}`}
      />
    </ul>
  );
}

// The component that decides which links to display (user, guest or admin)
export default function Header() {
  const { user, isAuthenticated, hasFinishedOAuth2 } = useAuthContext();
  const location = useLocation();

  // All the paths that require black coloured links
  const homeMenuPaths = useMemo(
    () =>
      [
        PagesEnum.Home,
        PagesEnum.ForgottenPassword,
        PagesEnum.Login,
        PagesEnum.Register,
        PagesEnum.FinishRegister,
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

  // All the paths that include placeholders and
  // require black coloured links
  const placeholderPaths = useMemo(
    () =>
      [PagesEnum.QuizStart, PagesEnum.QuizCreate].map((x) => {
        const indexOfPlaceholder = x.indexOf(':');
        return x.substring(0, indexOfPlaceholder);
      }),
    []
  );

  const isTeacher = RolesEnum.TEACHER === user.role;
  let headerClasses = 'main_menu ';

  // Add classes for the color of the links
  if (
    homeMenuPaths.includes(location.pathname) ||
    placeholderPaths.some((x) => location.pathname.includes(x))
  ) {
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
                        hasFinishedOAuth2={hasFinishedOAuth2}
                      />
                    )}

                    <li className="d-lg-block">
                      <LanguagePicker />
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
