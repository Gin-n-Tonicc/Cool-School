import { RegisterOptions } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

// The hook that construct validation for blogCreate form fields
// with a message based on the user's selected language
export default function useBlogCreateValidations() {
  const { t } = useTranslation();

  const TITLE_VALIDATIONS: RegisterOptions = {
    required: t('blogs.create.title.invalid.required'),
    minLength: {
      value: 5,
      message: t('blogs.create.title.invalid.min-length'),
    },
    maxLength: {
      value: 50,
      message: t('blogs.create.title.invalid.max-length'),
    },
  };

  const SUMMARY_VALIDATIONS: RegisterOptions = {
    required: t('blogs.create.summary.invalid.required'),
    minLength: {
      value: 10,
      message: t('blogs.create.summary.invalid.min-length'),
    },
  };

  const CONTENT_VALIDATIONS: RegisterOptions = {
    required: t('blogs.create.content.invalid.required'),
    minLength: {
      value: 150,
      message: t('blogs.create.content.invalid.min-length'),
    },
  };

  return { TITLE_VALIDATIONS, SUMMARY_VALIDATIONS, CONTENT_VALIDATIONS };
}
