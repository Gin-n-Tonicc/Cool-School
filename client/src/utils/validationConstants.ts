import { RegisterOptions } from 'react-hook-form';

// Common validations
export const EMAIL_VALIDATIONS: RegisterOptions = {
  required: 'Email is required.',
  pattern: {
    value: /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/gim,
    message: 'Email must be a valid email.',
  },
};

export const PASSWORD_VALIDATIONS: RegisterOptions = {
  required: 'Password is required.',
  pattern: {
    value: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/gim,
    message: 'Required minimum eight characters, one letter & one number',
  },
};

// Register validations
export const FIRST_NAME_VALIDATIONS: RegisterOptions = {
  required: 'First name is required.',
  minLength: {
    value: 2,
    message: 'First name requires minimum two characters',
  },
};

export const LAST_NAME_VALIDATIONS: RegisterOptions = {
  required: 'Last name is required.',
  minLength: {
    value: 2,
    message: 'Last name requires minimum two characters',
  },
};

export const USERNAME_VALIDATIONS: RegisterOptions = {
  required: 'Username is required.',
  pattern: {
    value: /^[a-z\d]{5,}$/gm,
    message: 'Required minimum five characters & no uppercase letters',
  },
};

export const ADDRESS_VALIDATIONS: RegisterOptions = {
  required: 'Address is required.',
  minLength: {
    value: 10,
    message: 'Address requires minimum ten characters',
  },
};

export const REPEAT_PASSWORD_VALIDATIONS: RegisterOptions = {
  ...PASSWORD_VALIDATIONS,
  required: 'Repeat password is required.',
};
