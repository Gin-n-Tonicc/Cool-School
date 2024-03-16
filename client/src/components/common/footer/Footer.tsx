import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';
import logo from '../../../images/logo.png';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import './Footer.scss';

// The component that displays a information
// about us and the web app positioned at the bottom of the page
export default function Footer() {
  const { t } = useTranslation();

  return (
    <footer className="footer-area">
      <div className="container">
        <div className="row justify-content-between">
          <div className="col-sm-6 col-md-4 col-xl-3">
            <div className="single-footer-widget footer_1">
              <Link to={PagesEnum.Home}>
                <img className="site-logo" src={logo} alt="" />
              </Link>
              <p>{t('footer.text')}</p>
              <p>{t('moto')}</p>
            </div>
          </div>

          <div className="col-xl-3 col-sm-6 col-md-4">
            <div className="single-footer-widget footer_2">
              <h4>{t('footer.contact.us')}</h4>
              <div className="contact_info">
                <p>
                  <span> {t('footer.contact.us.address')}</span>{' '}
                  {t('footer.contact.us.address.text')}
                </p>
                <p>
                  <span> {t('footer.contact.us.phone')}</span>{' '}
                  {t('footer.contact.us.phone.text')}
                </p>
                <p>
                  <span> {t('footer.contact.us.email')}</span>{' '}
                  {t('footer.contact.us.email.text')}
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
