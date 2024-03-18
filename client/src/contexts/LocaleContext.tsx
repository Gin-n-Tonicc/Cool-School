import { PropsWithChildren, createContext, useContext, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { getLocaleByLanguage } from '../types/enums/LanguageEnum';

type LocaleContextType = {
  language: string;
  locale: string;
};

const LocaleContext = createContext<LocaleContextType | null>(null);

// The component that provides all of the children
// with the necessary locale properties and functions
export function LocaleProvider({ children }: PropsWithChildren) {
  const { i18n } = useTranslation();

  const locale = useMemo(
    () => getLocaleByLanguage(i18n.language),
    [i18n.language]
  );

  return (
    <LocaleContext.Provider
      value={{
        language: i18n.language,
        locale: locale,
      }}>
      {children}
    </LocaleContext.Provider>
  );
}

export const useLocaleContext = () => {
  const localeContext = useContext(LocaleContext);

  if (!localeContext) {
    throw new Error(
      'useLocaleContext has to be used within <LocaleContext.Provider>'
    );
  }

  return localeContext;
};
