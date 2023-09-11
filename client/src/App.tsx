import { Route, Routes } from 'react-router-dom';
import Blog from './components/blog/Blog';
import Footer from './components/common/footer/Footer';
import Header from './components/common/header/Header';
import ProtectedRoute from './components/common/protected-route/ProtectedRoute';
import ContactUs from './components/contact-us/ContactUs';
import Courses from './components/courses/Courses';
import Home from './components/home/Home';
import HttpProvider from './components/http-provider/HttpProvider';
import Login from './components/login/Login';
import Logout from './components/logout/Logout';
import NotFound from './components/not-found/NotFound';
import Register from './components/register/Register';
import { AuthProvider } from './contexts/AuthContext';
import './styles/style.scss';

function App() {
  return (
    <>
      <AuthProvider>
        <Header />
        <HttpProvider>
          <Routes>
            {/* Everyone */}
            <Route path="/" element={<Home />} />
            <Route path="/courses" element={<Courses />} />
            <Route path="/blog" element={<Blog />} />
            <Route path="/contact" element={<ContactUs />} />

            {/* Only guests */}
            <Route element={<ProtectedRoute onlyUser={false} />}>
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
            </Route>

            {/* Only logged users */}
            <Route element={<ProtectedRoute onlyUser={true} />}>
              <Route path="/logout" element={<Logout />} />
            </Route>

            <Route path="*" element={<NotFound />} />
          </Routes>
        </HttpProvider>
      </AuthProvider>
      <Footer />
    </>
  );
}

export default App;
