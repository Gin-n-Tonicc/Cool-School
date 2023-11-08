import { PropsWithChildren } from 'react';

interface FormErrorWrapperProps extends PropsWithChildren {
  message: string | undefined;
}

export default function FormErrorWrapper(props: FormErrorWrapperProps) {
  return (
    <div className="form-group">
      <div className="form-error-container">
        {props.children}
        <p className="form-error">{props.message}</p>
      </div>
    </div>
  );
}
