import { useTranslation } from 'react-i18next';
import { LanguageEnum } from '../../../../../types/enums/LanguageEnum';

interface LanguageChoiceProps {
  language: LanguageEnum;
  translationKey: string;
}

export default function LanguageChoice(props: LanguageChoiceProps) {
  const { t, i18n } = useTranslation();

  const onClickLanguageChange = (e: any) => {
    e.preventDefault();
    const language = e.currentTarget.getAttribute('data-value');
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
