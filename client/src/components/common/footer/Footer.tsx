import logo from '../../../images/logo.png';
import './Footer.scss';

export default function Footer() {
  return (
    <footer className="footer-area">
      <div className="container">
        <div className="row justify-content-between">
          <div className="col-sm-6 col-md-4 col-xl-3">
            <div className="single-footer-widget footer_1">
              <a href="index.html">
                <img className="site-logo" src={logo} alt="" />
              </a>
              <p>
                But when shot real her. Chamber her one visite removal six
                sending himself boys scot exquisite existend an{' '}
              </p>
              <p>But when shot real her hamber her </p>
            </div>
          </div>
          <div className="col-sm-6 col-md-4 col-xl-4">
            <div className="single-footer-widget footer_2">
              <h4>Newsletter</h4>
              <p>
                Stay updated with our latest trends Seed heaven so said place
                winged over given forth fruit.
              </p>
              <form>
                <div className="form-group">
                  <div className="input-group mb-3">
                    <input
                      type="text"
                      className="form-control"
                      placeholder="Enter email address"
                      // onFocus="this.placeholder = ''"
                      // onblur="this.placeholder = 'Enter email address'"
                    />
                    <div className="input-group-append">
                      <button className="btn btn_1" type="button">
                        <i className="ti-angle-right"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </form>
              <div className="social_icon">
                <a href="#">
                  {' '}
                  <i className="ti-facebook"></i>{' '}
                </a>
                <a href="#">
                  {' '}
                  <i className="ti-twitter-alt"></i>{' '}
                </a>
                <a href="#">
                  {' '}
                  <i className="ti-instagram"></i>{' '}
                </a>
                <a href="#">
                  {' '}
                  <i className="ti-skype"></i>{' '}
                </a>
              </div>
            </div>
          </div>
          <div className="col-xl-3 col-sm-6 col-md-4">
            <div className="single-footer-widget footer_2">
              <h4>Contact us</h4>
              <div className="contact_info">
                <p>
                  <span> Address:</span> Hath of it fly signs bear be one
                  blessed after{' '}
                </p>
                <p>
                  <span> Phone:</span> +2 36 265 (8060)
                </p>
                <p>
                  <span> Email:</span> info@TODO:add_email.com{' '}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="container-fluid">
        <div className="row">
          <div className="col-lg-12">
            <div className="copyright_part_text text-center">
              <div className="row">
                <div className="col-lg-12">
                  <p className="footer-text m-0">
                    Copyright &copy;
                    {new Date().getFullYear()} All rights reserved
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}
