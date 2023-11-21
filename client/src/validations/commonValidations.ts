import { RegisterOptions } from 'react-hook-form';

const allowedFileMediaTypes = [
  'application/pdf',
  'image/jpeg',
  'image/png',
  'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  'application/vnd.ms-excel',
  'application/msword',
  'application/msword',
];

export const IMAGE_FILE_VALIDATIONS: RegisterOptions = {
  required: 'Image is required.',
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
