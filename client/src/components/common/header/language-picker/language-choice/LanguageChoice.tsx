import { MouseEventHandler } from 'react';
import { useTranslation } from 'react-i18next';
import { LanguageEnum } from '../../../../../types/enums/LanguageEnum';

interface LanguageChoiceProps {
  language: LanguageEnum;
  translationKey: string;
}

// The component that displays a single language and
// handles it by changing the language for the whole application
export default function LanguageChoice(props: LanguageChoiceProps) {
  const { t, i18n } = useTranslation();

  const onClickLanguageChange: MouseEventHandler<HTMLAnchorElement> = (e) => {
    e.preventDefault();
    const language = e.currentTarget.getAttribute('data-value') || undefined;
    i18n.changeLanguage(language);
  };

  let classes = 'dropdown-item';
  if (i18n.language === props.language) {
    classes += ' active';
  }

  return (
    <a
      href=""
      className={classes}
      data-value={props.language}
      onClick={onClickLanguageChange}>
      {t(props.translationKey)}
    </a>
  );
}
