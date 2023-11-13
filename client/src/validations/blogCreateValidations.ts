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

const allowedFileMediaTypes = [
  'application/pdf',
  'image/jpeg',
  'image/png',
  'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  'application/vnd.ms-excel',
  'application/msword',
  'application/msword',
];

export const FILE_VALIDATIONS: RegisterOptions = {
  required: 'Blog image is required.',
  validate: (value: FileList) => {
    if (!allowedFileMediaTypes.includes(value[0].type)) {
      return 'Unsupported file type. Must be .png, .jpg/.jpeg, .xls/.xlsx, .doc/.docx';
    }

    return true;
  },
};

export const CATEGORY_VALIDATIONS: RegisterOptions = {
  required: 'Category is required.',
  min: {
    value: 0,
    message: 'Please select a valid category',
  },
};
