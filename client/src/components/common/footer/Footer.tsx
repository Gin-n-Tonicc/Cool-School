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
                Discover the unparalleled advantages that make online education the ideal choice for
                those seeking a convenient, flexible, and enriching learning journey.
              </p>
              <p>THE IDEAL SCHOOL</p>
            </div>
          </div>
          <div className="col-sm-6 col-md-4 col-xl-4">
            <div className="single-footer-widget footer_2">
              <h4>Newsletter</h4>
              <p>
                Stay updated with our latest trends.
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
                  <span> Address:</span> 7700 Targovishte town, Targovishte municipality, Targovishte region, ”Cap. Danajiev" 22
                </p>
                <p>
                  <span> Phone:</span> 00359 601 6-30-70
                </p>
                <p>
                  <span> Email:</span> stefanbelis932@gmail.com
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
