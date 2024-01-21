import translationBulgarian from './locales/bg/translation.json';
import translationEnglish from './locales/en/translation.json';

import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import { LanguageEnum } from './types/enums/LanguageEnum';

export const defaultNS = 'main';
export const resources = {
  [LanguageEnum.ENGLISH]: {
    main: translationEnglish,
  },
  [LanguageEnum.BULGARIAN]: {
    main: translationBulgarian,
  },
} as const;

i18next.use(initReactI18next).init({
  lng: LanguageEnum.BULGARIAN,
  ns: ['main'],
  defaultNS,
  resources,
});
