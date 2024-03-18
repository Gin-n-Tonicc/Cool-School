import { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useErrorContext } from '../../../../contexts/ErrorContext';
import { ErrorTypeEnum } from '../../../../types/enums/ErrorTypeEnum';
import './ErrorAlert.scss';

interface IErrorAlertProps {
  unmountAfter: number;
  message: string;
  id: string;
  errorType: ErrorTypeEnum;
}

interface IErrorProps {
  close: Function;
  message: string;
  errorTitle: string;
}

// The component that displays the exception alert
function ExceptionError(props: IErrorProps) {
  return (
    <div className="col-sm-12">
      <div
        className="alert fade alert-simple alert-danger alert-dismissible text-left font__family-montserrat font__size-16 font__weight-light brk-library-rendered rendered show"
        role="alert"
        data-brk-library="component__alert">
        <button
          onClick={() => props.close()}
          type="button"
          className="close font__size-18"
          data-dismiss="alert">
          <span aria-hidden="true">
            <i className="fa fa-times danger "></i>
          </span>
          <span className="sr-only">Close</span>
        </button>
        <i className="start-icon far fa-times-circle faa-pulse animated"></i>
        <strong className="font__weight-semibold">{props.errorTitle}! </strong>
        {props.message}
      </div>
    </div>
  );
}

// The component that displays the heads up alert
function HeadsUpError(props: IErrorProps) {
  return (
    <div className="col-sm-12">
      <div
        className="alert fade alert-simple alert-info alert-dismissible text-left font__family-montserrat font__size-16 font__weight-light brk-library-rendered rendered show"
        role="alert"
        data-brk-library="component__alert">
        <button
          onClick={() => props.close()}
          type="button"
          className="close font__size-18"
          data-dismiss="alert">
          <span aria-hidden="true">
            <i className="fa fa-times blue-cross"></i>
          </span>
          <span className="sr-only">Close</span>
        </button>
        <i className="start-icon  fa fa-info-circle faa-shake animated"></i>
        <strong className="font__weight-semibold">
          {props.errorTitle}!
        </strong>{' '}
        {props.message}
      </div>
    </div>
  );
}

// The component that displays the appropriate alert based on the type
export default function ErrorAlert({
  unmountAfter,
  message,
  id,
  errorType,
}: IErrorAlertProps) {
  const { t } = useTranslation();
  const { deleteError } = useErrorContext();

  // Delete alert after {n} seconds
  useEffect(() => {
    setTimeout(() => {
      deleteError(id);
    }, unmountAfter);
  }, [id, unmountAfter, deleteError]);

  // On 'x' button click delete the error immediately
  const onClose = () => {
    deleteError(id);
  };

  // Render alert based on type
  if (errorType === ErrorTypeEnum.EXCEPTION) {
    return (
      <ExceptionError
        close={onClose}
        message={message}
        errorTitle={t('error')}
      />
    );
  }

  return (
    <HeadsUpError
      close={onClose}
      message={message}
      errorTitle={t('headsUpInfo')}
    />
  );
}
