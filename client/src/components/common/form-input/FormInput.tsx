import { UseControllerProps, useController } from 'react-hook-form';

export type FormInputProps = {
  type: string;
  inputClasses?: string;
  iconClasses?: string;
} & UseControllerProps<any>;

export default function FormInput(props: FormInputProps) {
  const { field, fieldState } = useController(props);

  return (
    <div className="form-group">
      <div className="form-error-container">
        <label htmlFor={props.name}>
          <i className={props.iconClasses}></i>
        </label>

        <input
          {...field}
          className={props.inputClasses}
          type={props.type}
          placeholder={props.name}
        />
        <p className="form-error">{fieldState.error?.message}</p>
      </div>
    </div>
  );
}
