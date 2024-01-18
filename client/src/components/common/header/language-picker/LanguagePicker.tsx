import { useTranslation } from 'react-i18next';
import { LanguageEnum } from '../../../../types/enums/LanguageEnum';
import LanguageChoice from './language-choice/LanguageChoice';

export default function LanguagePicker() {
  const { i18n } = useTranslation();

  return (
    <>
      <li className="nav-item dropdown">
        <p className="nav-link dropdown-toggle">
          <i className="zmdi zmdi-globe-alt"></i> <span>{i18n.language}</span>
        </p>
        <div className="dropdown-menu" aria-labelledby="navbarDropdown">
          <LanguageChoice
            language={LanguageEnum.BULGARIAN}
            translationKey="language.picker.bulgarian"
          />
          <LanguageChoice
            language={LanguageEnum.ENGLISH}
            translationKey="language.picker.english"
          />
        </div>
      </li>
    </>
  );
}
