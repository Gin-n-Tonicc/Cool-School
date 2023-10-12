import './InternalError.scss';

export default function InternalError() {
  return (
    <section className="error-container">
      <p className="error-number">500</p>
      <p className="error-title">Ooops!!</p>
      <p className="error-text">
        Try refreshing the page or feel free to contact us if the problem
        persists
      </p>
    </section>
  );
}
