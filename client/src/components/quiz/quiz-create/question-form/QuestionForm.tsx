import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { v4 as uuidV4 } from 'uuid';
import useValidators from '../../../../hooks/useValidator/useValidators';
import { IDefaultObject } from '../../../../types/interfaces/common/IDefaultObject';
import { IQuestion } from '../../../../types/interfaces/quizzes/IQuestion';
import FormInput from '../../../common/form-input/FormInput';

export type QuestionFormQuestion = Partial<IQuestion> & {
  customId?: string;
};

export type QuestionState = IDefaultObject<QuestionFormQuestion>;

type QuestionInputs = {
  Description: string;
  Marks: string;
};

interface QuestionFormProps {
  onSubmit: (data: QuestionFormQuestion) => void;
}

// The component that displays
// and handles the question form
export default function QuestionForm(props: QuestionFormProps) {
  const { t } = useTranslation();
  const { quizCreate } = useValidators();

  // Handle form
  const { handleSubmit, control, reset } = useForm<QuestionInputs>({
    defaultValues: {
      Description: '',
      Marks: '',
    },
    mode: 'onChange',
  });

  // Handle form submit
  const onSubmit: SubmitHandler<QuestionInputs> = (data) => {
    const question: QuestionFormQuestion = {
      description: data.Description,
      marks: Number(data.Marks),
      customId: uuidV4(),
    };

    // Reset form and Pass data to the parent component
    reset();
    props.onSubmit(question);
  };

  return (
    <>
      <div
        className="accordion quiz-subform-wrapper"
        id="quizQuestionCreateAccordion">
        <div className="quiz-subform-title-wrapper">
          <h4>{t('quizzes.create.questions.subheading')}</h4>
          <button
            className="btn_4"
            data-toggle="collapse"
            data-target="#quizQuestionCreateContent"
            aria-expanded="true"
            aria-controls="quizQuestionCreateContent">
            {t('quizzes.create.subsection.button')}
          </button>
        </div>
        <div
          id="quizQuestionCreateContent"
          className="collapse"
          aria-labelledby="headingOne"
          data-parent="#quizQuestionCreateAccordion">
          <form
            onSubmit={handleSubmit(onSubmit)}
            className="register-form quiz-subform"
            id="register-form">
            <div className="number-controls">
              <FormInput
                control={control}
                name="Description"
                placeholder={t('quizzes.create.description')}
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={quizCreate.DESCRIPTION_VALIDATIONS}
              />
              <FormInput
                control={control}
                name="Marks"
                placeholder={t('quizzes.create.marks')}
                type="number"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={quizCreate.QUESTION_MARKS_VALIDATIONS}
              />
            </div>

            <div className="form-group form-button">
              <input
                type="submit"
                name="signup"
                id="signup"
                className="btn_4  "
                value={t('quizzes.create.subsection.questions.button')}
              />
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
