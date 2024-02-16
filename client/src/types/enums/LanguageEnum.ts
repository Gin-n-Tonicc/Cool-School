export enum LanguageEnum {
  BULGARIAN = 'Български',
  ENGLISH = 'English',
}

const languageToLocale = {
  [LanguageEnum.BULGARIAN as string]: 'bg',
  [LanguageEnum.ENGLISH as string]: 'en',
};

export const getLocaleByLanguage = (language: string) => {
  return languageToLocale[language];
};
