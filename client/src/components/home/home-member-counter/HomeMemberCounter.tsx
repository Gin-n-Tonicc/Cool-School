import './HomeMemberCounter.scss';

export default function HomeMemberCounter() {
  return (
    <section className="member_counter">
      <div className="container">
        <div className="row">
          <div className="col-lg-3 col-sm-6">
            <div className="single_member_counter">
              <span className="counter">1024</span>
              <h4>All Teachers</h4>
            </div>
          </div>
          <div className="col-lg-3 col-sm-6">
            <div className="single_member_counter">
              <span className="counter">960</span>
              <h4> All Students</h4>
            </div>
          </div>
          <div className="col-lg-3 col-sm-6">
            <div className="single_member_counter">
              <span className="counter">1020</span>
              <h4>Online Students</h4>
            </div>
          </div>
          <div className="col-lg-3 col-sm-6">
            <div className="single_member_counter">
              <span className="counter">820</span>
              <h4>Ofline Students</h4>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
