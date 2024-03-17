import { useCallback, useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import useValidators from '../../../../hooks/useValidator/useValidators';
import { IDefaultObject } from '../../../../types/interfaces/common/IDefaultObject';
import { IAnswer } from '../../../../types/interfaces/quizzes/IAnswer';
import CategorySelect from '../../../common/category-select/CategorySelect';
import FormErrorWrapper from '../../../common/form-error-wrapper/FormErrorWrapper';
import FormInput from '../../../common/form-input/FormInput';
import { QuestionState } from '../question-form/QuestionForm';

export type AnswerFormAnswer = Partial<IAnswer> & { customQuestionId: string };
export type AnswerState = IDefaultObject<AnswerFormAnswer>;

type AnswerInputs = {
  Text: string;
  questionId: string;
  correct: string;
};

interface AnswerFormProps {
  onSubmit: (data: AnswerFormAnswer) => void;
  questions: QuestionState;
}

// The component that displays
// and handles the answer form
export default function AnswerForm(props: AnswerFormProps) {
  const { t } = useTranslation();
  const { quizCreate } = useValidators();

  // Handle form
  const {
    handleSubmit,
    control,
    reset,
    register,
    setValue,
    formState: { errors },
  } = useForm<AnswerInputs>({
    defaultValues: {
      Text: '',
      questionId: '',
      correct: '',
    },
    mode: 'onChange',
  });

  // Register question dropdown
  useEffect(() => {
    register('questionId', { ...quizCreate.ANSWER_QUESTION_VALIDATIONS });
  }, []);

  const onQuestionChange = useCallback(
    (val: string | undefined) => {
      setValue('questionId', val || '', {
        shouldValidate: true,
        shouldDirty: true,
        shouldTouch: true,
      });
    },
    [setValue]
  );

  // Handle form submit
  const onSubmit: SubmitHandler<AnswerInputs> = (data) => {
    const isCorrect = Boolean(data.correct.length);

    const answer: AnswerFormAnswer = {
      text: data.Text,
      isCorrect,
      customQuestionId: data.questionId,
    };

    // Pass data to the parent component and reset form except the question dropdown
    reset({ questionId: data.questionId, Text: '', correct: '' });
    props.onSubmit(answer);
  };

  return (
    <>
      <div
        className="accordion quiz-subform-wrapper"
        id="quizAnswerCreateAccordion">
        <div className="quiz-subform-title-wrapper">
          <h4>{t('quizzes.create.answers.subheading')}</h4>
          <button
            className="btn_4"
            data-toggle="collapse"
            data-target="#quizAnswerCreateContent"
            aria-expanded="true"
            aria-controls="quizAnswerCreateContent">
            {t('quizzes.create.subsection.button')}
          </button>
        </div>
        <div
          id="quizAnswerCreateContent"
          className="collapse"
          aria-labelledby="headingOne"
          data-parent="#quizAnswerCreateAccordion">
          <form
            onSubmit={handleSubmit(onSubmit)}
            className="register-form quiz-subform"
            id="register-form">
            <FormErrorWrapper message={errors.questionId?.message}>
              <CategorySelect
                categories={
                  Object.values(props.questions)
                    .filter((x) => x.description)
                    .map((x) => ({
                      value: `${x.customId}`,
                      label: `${t(
                        'quizzes.create.answer.select.question.text'
                      )} - ${x.description}`,
                    })) || []
                }
                onCategoryChange={() => {}}
                customOnChange={onQuestionChange}
                placeholder={t('quizzes.create.answer.select.question')}
              />
            </FormErrorWrapper>
            <div className="number-controls">
              <FormInput
                control={control}
                name="Text"
                placeholder={t('quizzes.create.answer.text')}
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
                rules={quizCreate.ANSWER_TEXT_VALIDATIONS}
              />
              <div>
                <FormErrorWrapper message={undefined}>
                  <div className="form-check-inline">
                    <input
                      {...register('correct')}
                      type="radio"
                      className="form-check-input"
                      value={'true'}
                    />
                    <p>{t('quizzes.create.answer.correct')}</p>
                  </div>
                  <div className="form-check-inline">
                    <input
                      {...register('correct')}
                      type="radio"
                      className="form-check-input"
                      value={''}
                    />
                    <p>{t('quizzes.create.answer.incorrect')}</p>
                  </div>
                </FormErrorWrapper>
              </div>
            </div>

            <div className="form-group form-button">
              <input
                type="submit"
                name="signup"
                id="signup"
                className="btn_4"
                value={t('quizzes.create.subsection.answers.button')}
              />
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
