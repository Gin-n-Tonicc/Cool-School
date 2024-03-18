import { RegisterOptions } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

const allowedFileMediaTypes = [
  'application/pdf',
  'image/jpeg',
  'image/png',
  'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  'application/vnd.ms-excel',
  'application/msword',
  'application/msword',
];

// The hook that construct validation for common form fields
// with a message based on the user's selected language
export default function useCommonValidations() {
  const { t } = useTranslation();

  const FILE_VALIDATIONS: RegisterOptions = {
    required: t('common.file.invalid.required'),
    validate: (value: FileList) => {
      if (!allowedFileMediaTypes.includes(value[0].type)) {
        return t('common.file.invalid.unsupported.type');
      }

      return true;
    },
  };

  const CATEGORY_VALIDATIONS: RegisterOptions = {
    required: t('common.category.invalid.required'),
    min: {
      value: 0,
      message: t('common.category.invalid.min'),
    },
  };

  return { FILE_VALIDATIONS, CATEGORY_VALIDATIONS };
}
