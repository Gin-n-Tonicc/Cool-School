import { useCallback, useEffect } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
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

export default function AnswerForm(props: AnswerFormProps) {
  const { quizCreate } = useValidators();

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

  const onSubmit: SubmitHandler<AnswerInputs> = (data) => {
    const isCorrect = Boolean(data.correct.length);

    const answer: AnswerFormAnswer = {
      text: data.Text,
      isCorrect,
      customQuestionId: data.questionId,
    };

    reset({ questionId: data.questionId, Text: '', correct: '' });
    props.onSubmit(answer);
  };

  return (
    <>
      <div
        className="accordion quiz-subform-wrapper"
        id="quizAnswerCreateAccordion">
        <div className="quiz-subform-title-wrapper">
          <h4>Отговори</h4>
          <button
            className="btn_4"
            data-toggle="collapse"
            data-target="#quizAnswerCreateContent"
            aria-expanded="true"
            aria-controls="quizAnswerCreateContent">
            Създай
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
                      label: `Въпрос - ${x.description}`,
                    })) || []
                }
                onCategoryChange={() => {}}
                customOnChange={onQuestionChange}
                placeholder="Избери въпрос"
              />
            </FormErrorWrapper>
            <div className="number-controls">
              <FormInput
                control={control}
                name="Text"
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
                    <p>Вярно</p>
                  </div>
                  <div className="form-check-inline">
                    <input
                      {...register('correct')}
                      type="radio"
                      className="form-check-input"
                      value={''}
                    />
                    <p>Невярно</p>
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
                value="Създай отговор"
              />
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
