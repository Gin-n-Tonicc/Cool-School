import { RegisterOptions } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

// The hook that construct validation for auth form fields
// with a message based on the user's selected language
export default function useAuthValidations() {
  const { t } = useTranslation();

  // Common validations
  const EMAIL_VALIDATIONS: RegisterOptions = {
    required: t('auth.email.required'),
    pattern: {
      value: /[a-z0-9]+@[a-z]+\.[a-z]{2,3}/gim,
      message: t('auth.email.invalid.pattern'),
    },
  };

  const PASSWORD_VALIDATIONS: RegisterOptions = {
    required: t('auth.password.required'),
    pattern: {
      value: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/gim,
      message: t('auth.password.invalid.pattern'),
    },
  };

  // Register validations
  const FIRST_NAME_VALIDATIONS: RegisterOptions = {
    required: t('auth.first.name.required'),
    minLength: {
      value: 2,
      message: t('auth.first.name.invalid.min-length'),
    },
  };

  const LAST_NAME_VALIDATIONS: RegisterOptions = {
    required: t('auth.last.name.required'),
    minLength: {
      value: 2,
      message: t('auth.last.name.invalid.min-length'),
    },
  };

  const USERNAME_VALIDATIONS: RegisterOptions = {
    required: t('auth.username.required'),
    pattern: {
      value: /^[a-z\d]{5,}$/gm,
      message: t('auth.username.invalid.pattern'),
    },
  };

  const ADDRESS_VALIDATIONS: RegisterOptions = {
    required: t('auth.address.required'),
    minLength: {
      value: 10,
      message: t('auth.address.invalid.min-length'),
    },
  };

  const DESCRIPTION_VALIDATIONS: RegisterOptions = {
    required: t('auth.description.required'),
    minLength: {
      value: 60,
      message: t('auth.description.invalid.min-length'),
    },
    maxLength: {
      value: 120,
      message: t('auth.description.invalid.max-length'),
    },
  };

  const REPEAT_PASSWORD_VALIDATIONS: RegisterOptions = {
    ...PASSWORD_VALIDATIONS,
    required: t('auth.repeat.password.required'),
  };

  return {
    EMAIL_VALIDATIONS,
    PASSWORD_VALIDATIONS,
    FIRST_NAME_VALIDATIONS,
    LAST_NAME_VALIDATIONS,
    USERNAME_VALIDATIONS,
    ADDRESS_VALIDATIONS,
    DESCRIPTION_VALIDATIONS,
    REPEAT_PASSWORD_VALIDATIONS,
  };
}
