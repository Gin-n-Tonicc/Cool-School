import { SpinnerRoundFilled } from 'spinners-react';
import './Spinner.scss';

// The component that displays a spinner
export default function Spinner() {
  return (
    <div className="spinner-wrapper">
      <SpinnerRoundFilled
        size={150}
        thickness={100}
        speed={150}
        color="rgba(245, 121, 11, 1)"
      />
    </div>
  );
}
