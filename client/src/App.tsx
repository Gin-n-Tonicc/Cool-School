import { Route, Routes } from 'react-router-dom';
import Blog from './components/blog/Blog';
import Footer from './components/common/footer/Footer';
import Header from './components/common/header/Header';
import ContactUs from './components/contact-us/ContactUs';
import Home from './components/home/Home';
import NotFound from './components/not-found/NotFound';
import './styles/style.scss';

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/blog" element={<Blog />} />
        <Route path="/contact" element={<ContactUs />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
      <Footer />
    </>
  );
}

export default App;
