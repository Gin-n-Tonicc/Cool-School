import { Route, Routes } from 'react-router-dom';
import Blog from './components/blog/Blog';
import Footer from './components/common/footer/Footer';
import Header from './components/common/header/Header';
import ContactUs from './components/contact-us/ContactUs';
import Courses from './components/courses/Courses';
import Home from './components/home/Home';
import Login from './components/login/Login';
import NotFound from './components/not-found/NotFound';
import Register from './components/register/Register';
import './styles/style.scss';

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/courses" element={<Courses />} />
        <Route path="/blog" element={<Blog />} />
        <Route path="/contact" element={<ContactUs />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Footer />
    </>
  );
}

export default App;
