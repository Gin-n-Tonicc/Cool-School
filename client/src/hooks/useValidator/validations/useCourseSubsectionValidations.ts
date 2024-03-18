import { RegisterOptions } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

// The hook that construct validation for subsectionCreate form fields
// with a message based on the user's selected language
export default function useCourseSubsectionValidations() {
  const { t } = useTranslation();

  const TITLE_VALIDATIONS: RegisterOptions = {
    required: t('courses.subsection.title.required'),
    minLength: {
      value: 10,
      message: t('courses.subsection.title.invalid.min-length'),
    },
  };

  const DESCRIPTION_VALIDATIONS: RegisterOptions = {
    required: t('courses.subsection.description.required'),
    minLength: {
      value: 50,
      message: t('courses.subsection.description.invalid.length'),
    },
    maxLength: {
      value: 254,
      message: t('courses.subsection.description.invalid.length'),
    },
  };

  return { TITLE_VALIDATIONS, DESCRIPTION_VALIDATIONS };
}
