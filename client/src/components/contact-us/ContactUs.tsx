import { useTranslation } from 'react-i18next';
import './ContactUs.scss';
import ContactUsBreadcrumb from './contact-us-breadcrumb/ContactUsBreadcrumb';

// The component that displays the contact us page
// which contains some info about us and a form that sends an email to us
export default function ContactUs() {
  const { t } = useTranslation();

  return (
    <>
      <ContactUsBreadcrumb />
      <section className="contact-section section_padding">
        <div className="container">
          <div className="row">
            <div className="col-12">
              <h2 className="contact-title">{t('contact.get.in.touch')}</h2>
            </div>
            <div className="col-lg-8">
              <form
                action="mailto:stefanbelis932@gmail.com"
                method="get"
                encType="text/plain"
                className="form-contact contact_form"
                id="contactForm">
                <div className="row">
                  <div className="col-12">
                    <div className="form-group">
                      <textarea
                        className="form-control w-100"
                        name="body"
                        id="body"
                        cols={30}
                        rows={9}
                        placeholder={t('contact.message')}></textarea>
                    </div>
                  </div>

                  <div className="col-12">
                    <div className="form-group">
                      <input
                        className="form-control"
                        name="bcc"
                        id="email"
                        type="email"
                        placeholder={t('contact.message.email')}
                      />
                    </div>
                  </div>
                  <div className="col-12">
                    <div className="form-group">
                      <input
                        className="form-control"
                        name="subject"
                        id="subject"
                        type="text"
                        placeholder={t('contact.message.subject')}
                      />
                    </div>
                  </div>
                </div>
                <div className="form-group mt-3">
                  <button
                    type="submit"
                    className="button button-contactForm btn_1">
                    {t('contact.button')}
                  </button>
                </div>
              </form>
            </div>
            <div className="col-lg-4">
              <div className="media contact-info">
                <span className="contact-info__icon">
                  <i className="ti-home"></i>
                </span>
                <div className="media-body">
                  <h3>{t('contact.address')}</h3>
                  <p>{t('contact.address.description')}</p>
                </div>
              </div>
              <div className="media contact-info">
                <span className="contact-info__icon">
                  <i className="ti-tablet"></i>
                </span>
                <div className="media-body">
                  <h3>{t('footer.contact.us.phone.text')}</h3>
                  <p>{t('contact.phone.description')}</p>
                </div>
              </div>
              <div className="media contact-info">
                <span className="contact-info__icon">
                  <i className="ti-email"></i>
                </span>
                <div className="media-body">
                  <h3>stefanbelis932@gmail.com</h3>
                  <p>{t('contact.email.description')}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
