import { RegisterOptions } from 'react-hook-form';

export const TITLE_VALIDATIONS: RegisterOptions = {
  required: 'Title is required.',
  minLength: {
    value: 5,
    message: 'Title requires minimum five characters',
  },
  maxLength: {
    value: 50,
    message: 'Title requires maximum fifty characters',
  },
};

export const SUMMARY_VALIDATIONS: RegisterOptions = {
  required: 'Summary is required.',
  minLength: {
    value: 10,
    message: 'Summary requires minimum ten characters',
  },
};

export const CONTENT_VALIDATIONS: RegisterOptions = {
  required: 'Content is required.',
  minLength: {
    value: 150,
    message: 'Summary requires minimum one hundred and fifty characters',
  },
};
