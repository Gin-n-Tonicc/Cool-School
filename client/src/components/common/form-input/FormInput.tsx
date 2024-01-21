import { UseControllerProps, useController } from 'react-hook-form';
import FormErrorWrapper from '../form-error-wrapper/FormErrorWrapper';

export type FormInputProps = {
  type: string;
  inputClasses?: string;
  iconClasses?: string;
  placeholder?: string;
} & UseControllerProps<any>;

export default function FormInput(props: FormInputProps) {
  const { field, fieldState } = useController(props);

  return (
    <FormErrorWrapper message={fieldState.error?.message}>
      <label htmlFor={props.name}>
        <i className={props.iconClasses}></i>
      </label>

      <input
        {...field}
        className={props.inputClasses}
        type={props.type}
        placeholder={props.placeholder || props.name}
      />
    </FormErrorWrapper>
  );
}
