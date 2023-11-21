import { RegisterOptions } from 'react-hook-form';

export const TITLE_VALIDATIONS: RegisterOptions = {
  required: 'Title is required.',
  minLength: {
    value: 10,
    message: 'Title requires minimum ten characters',
  },
};

export const DESCRIPTION_VALIDATIONS: RegisterOptions = {
  required: 'Description is required.',
  minLength: {
    value: 50,
    message: 'Description must be between 50 and 254 characters',
  },
  maxLength: {
    value: 254,
    message: 'Description must be between 50 and 254 characters',
  },
};
