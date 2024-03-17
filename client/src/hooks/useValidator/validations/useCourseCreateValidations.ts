import { RegisterOptions } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

// The hook that construct validation for courseCreate form fields
// with a message based on the user's selected language
export default function useCourseCreateValidations() {
  const { t } = useTranslation();

  const NAME_VALIDATIONS: RegisterOptions = {
    required: t('courses.create.name.invalid.required'),
    minLength: {
      value: 10,
      message: t('courses.create.name.invalid.minLength'),
    },
  };

  const OBJECTIVES_VALIDATIONS: RegisterOptions = {
    required: t('courses.create.objectives.invalid.required'),
    minLength: {
      value: 150,
      message: t('courses.create.objectives.invalid.minLength'),
    },
  };

  const ELIGIBILITY_VALIDATIONS: RegisterOptions = {
    required: t('courses.create.eligibility.invalid.required'),
    minLength: {
      value: 150,
      message: t('courses.create.objectives.invalid.minLength'),
    },
  };

  return { NAME_VALIDATIONS, OBJECTIVES_VALIDATIONS, ELIGIBILITY_VALIDATIONS };
}
