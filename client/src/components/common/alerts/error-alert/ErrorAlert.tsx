import { useEffect } from 'react';
import { useErrorContext } from '../../../../contexts/ErrorContext';
import './ErrorAlert.scss';

export type ErrorAlertProps = {
  unmountAfter: number;
  message: string;
  id: string;
};

export default function ErrorAlert({
  unmountAfter,
  message,
  id,
}: ErrorAlertProps) {
  const { deleteError } = useErrorContext();

  useEffect(() => {
    setTimeout(() => {
      deleteError(id);
    }, unmountAfter);
  }, []);

  const onClose = () => {
    deleteError(id);
  };

  return (
    <div className="col-sm-12">
      <div
        className="alert fade alert-simple alert-danger alert-dismissible text-left font__family-montserrat font__size-16 font__weight-light brk-library-rendered rendered show"
        role="alert"
        data-brk-library="component__alert">
        <button
          onClick={onClose}
          type="button"
          className="close font__size-18"
          data-dismiss="alert">
          <span aria-hidden="true">
            <i className="fa fa-times danger "></i>
          </span>
          <span className="sr-only">Close</span>
        </button>
        <i className="start-icon far fa-times-circle faa-pulse animated"></i>
        <strong className="font__weight-semibold">Oh snap! </strong>
        {message}
      </div>
    </div>
  );
}
