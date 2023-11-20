import { RegisterOptions } from 'react-hook-form';

export const NAME_VALIDATIONS: RegisterOptions = {
  required: 'Name is required.',
  minLength: {
    value: 10,
    message: 'Name requires minimum ten characters',
  },
};

export const OBJECTIVES_VALIDATIONS: RegisterOptions = {
  required: 'Objectives are required.',
  minLength: {
    value: 150,
    message:
      'Objectives field requires minimum one hundred and fifty characters',
  },
};

export const ELIGIBILITY_VALIDATIONS: RegisterOptions = {
  required: 'Eligibility is required.',
  minLength: {
    value: 150,
    message:
      'Eligibility field requires minimum one hundred and fifty characters',
  },
};
