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
      <label htmlFor={props.name}>
        <i className={props.iconClasses}></i>
      </label>

      <input
        {...field}
        className={props.inputClasses}
        type={props.type}
        placeholder={props.name}
      />

      {/* DEBUGGING PURPOSES */}
      {/* <p>{fieldState.isTouched && 'Touched'}</p>
      <p>{fieldState.isDirty && 'Dirty'}</p>
      <p>{fieldState.invalid ? 'invalid' : 'valid'}</p> */}
    </div>
  );
}
