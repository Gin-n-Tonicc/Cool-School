import './HomeBanner.scss';

export default function HomeBanner() {
  return (
    <section className="banner_part">
      <div className="container">
        <div className="row align-items-center">
          <div className="col-lg-6 col-xl-6">
            <div className="banner_text">
              <div className="banner_text_iner">
                <h5>COOL SCHOOL</h5>
                <h1>THE IDEAL SCHOOL</h1>
                <p>
                Welcome to a transformative educational experience where the pains of traditional
                learning are replaced with the seamless benefits of online schools.
                   Discover the unparalleled advantages that make online education
                   the ideal choice for those seeking a convenient, flexible, and enriching learning journey.
                </p>
                <a href="#" className="btn_1">
                  View Course{' '}
                </a>
                <a href="#" className="btn_2">
                  Get Started{' '}
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
