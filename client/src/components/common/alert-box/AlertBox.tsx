import { useErrorContext } from '../../../contexts/ErrorContext';
import ErrorAlert from '../alerts/error-alert/ErrorAlert';
import './AlertBox.scss';

// The component that groups and displays all of the alerts
export default function AlertBox() {
  const { errors } = useErrorContext();

  return (
    <section className="alert-container">
      <div className="container mt-5">
        <div className="row">
          {errors.map((x) => (
            <ErrorAlert {...x} key={x.id} />
          ))}
        </div>
      </div>
    </section>
  );
}
