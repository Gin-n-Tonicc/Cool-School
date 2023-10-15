import { Route, Routes } from 'react-router-dom';
import Authenticate from './components/auth/authenticate/Authenticate';
import Login from './components/auth/login/Login';
import Logout from './components/auth/logout/Logout';
import Register from './components/auth/register/Register';
import Blog from './components/blog/Blog';
import AlertBox from './components/common/alert-box/AlertBox';
import Footer from './components/common/footer/Footer';
import Header from './components/common/header/Header';
import ProtectedRoute from './components/common/protected-route/ProtectedRoute';
import ContactUs from './components/contact-us/ContactUs';
import Courses from './components/courses/Courses';
import ErrorBoundary from './components/error-boundary/ErrorBoundary';
import Home from './components/home/Home';
import HttpProvider from './components/http-provider/HttpProvider';
import NavigationMiddleware from './components/navigation-middleware/NavigationMiddleware';
import NotFound from './components/not-found/NotFound';
import Quizzes from './components/quizzes/Quizzes';
import { AuthProvider } from './contexts/AuthContext';
import { ErrorProvider } from './contexts/ErrorContext';
import './styles/style.scss';
import { PagesEnum } from './types/enums/PagesEnum';

function App() {
  return (
    <>
      <ErrorBoundary>
        <ErrorProvider>
          <NavigationMiddleware>
            <AuthProvider>
              <HttpProvider>
                <Authenticate>
                  <AlertBox />
                  <Header />
                  <Routes>
                    {/* Everyone */}
                    <Route path={PagesEnum.Home} element={<Home />} />
                    <Route path={PagesEnum.Courses} element={<Courses />} />
                    <Route path={PagesEnum.Blog} element={<Blog />} />
                    <Route path={PagesEnum.Contact} element={<ContactUs />} />

                    {/* Only guests */}
                    <Route element={<ProtectedRoute onlyUser={false} />}>
                      <Route path={PagesEnum.Login} element={<Login />} />
                      <Route path={PagesEnum.Register} element={<Register />} />
                    </Route>

                    {/* Only logged users */}
                    <Route element={<ProtectedRoute onlyUser={true} />}>
                      <Route path={PagesEnum.Logout} element={<Logout />} />
                      <Route path={PagesEnum.Quizzes} element={<Quizzes />} />
                    </Route>

                    <Route path="*" element={<NotFound />} />
                  </Routes>
                </Authenticate>
              </HttpProvider>
            </AuthProvider>
          </NavigationMiddleware>
        </ErrorProvider>
        <Footer />
      </ErrorBoundary>
    </>
  );
}

export default App;
