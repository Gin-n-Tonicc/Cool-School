import { RefObject } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import useValidators from '../../../../hooks/useValidator/useValidators';
import { IQuiz } from '../../../../types/interfaces/quizzes/IQuiz';
import FormInput from '../../../common/form-input/FormInput';

export type QuizFormInputs = {
  Title: string;
  Description: string;
  'Attempt Limit': string;
  'Minute Duration': string;
  'Start Date': string;
  'End Date': string;
};

interface QuizFormProps {
  onSubmit: (data: IQuiz) => void;
  submitRef: RefObject<HTMLButtonElement>;
  subsectionId: number;
}

// The component that displays
// and handles the quiz form
export default function QuizForm(props: QuizFormProps) {
  const { quizCreate } = useValidators();
  const { t } = useTranslation();

  // Handle form
  const { handleSubmit, control } = useForm<QuizFormInputs>({
    defaultValues: {
      Title: '',
      Description: '',
      'Attempt Limit': '',
      'Minute Duration': '',
      'Start Date': '',
      'End Date': '',
    },
    mode: 'onChange',
  });

  // Handle form submit
  const onSubmit: SubmitHandler<QuizFormInputs> = (data) => {
    const quiz: IQuiz = {
      title: data.Title,
      description: data.Description,
      startTime: new Date(data['Start Date']),
      endTime: new Date(data['End Date']),
      attemptLimit: Number(data['Attempt Limit']),
      quizDurationInMinutes: Number(data['Minute Duration']),
      subsectionId: props.subsectionId,
      totalMarks: 0,
      id: 0,
    };

    // Pass data to the parent component
    props.onSubmit(quiz);
  };

  return (
    <>
      <h4>{t('quizzes.create.quiz.subheading')}</h4>
      <form
        className="register-form"
        id="register-form"
        onSubmit={handleSubmit(onSubmit)}>
        <FormInput
          control={control}
          name="Title"
          placeholder={t('quizzes.create.title')}
          type="text"
          iconClasses="zmdi zmdi-face material-icons-name"
          rules={quizCreate.TITLE_VALIDATIONS}
        />
        <FormInput
          control={control}
          name="Description"
          placeholder={t('quizzes.create.description')}
          type="text"
          iconClasses="zmdi zmdi-face material-icons-name"
          rules={quizCreate.DESCRIPTION_VALIDATIONS}
        />
        <div className="number-controls">
          <FormInput
            control={control}
            name="Attempt Limit"
            placeholder={t('quizzes.create.attempt.limit')}
            type="number"
            iconClasses="zmdi zmdi-face material-icons-name"
            rules={quizCreate.ATTEMPT_LIMIT_VALIDATIONS}
          />
          <FormInput
            control={control}
            name="Minute Duration"
            placeholder={t('quizzes.create.minute.duration')}
            type="number"
            iconClasses="zmdi zmdi-face material-icons-name"
            rules={quizCreate.QUIZ_DURATION_VALIDATIONS}
          />
        </div>

        <div className="number-controls">
          <FormInput
            control={control}
            name="Start Date"
            placeholder={t('quizzes.create.start.date')}
            type="text"
            iconClasses="zmdi zmdi-face material-icons-name"
            rules={quizCreate.START_TIME_VALIDATIONS}
            onFocus={(e) => {
              e.currentTarget.type = 'datetime-local';
            }}
          />
          <FormInput
            control={control}
            name="End Date"
            placeholder={t('quizzes.create.end.date')}
            type="text"
            iconClasses="zmdi zmdi-face material-icons-name"
            rules={quizCreate.END_TIME_VALIDATIONS}
            onFocus={(e) => {
              e.currentTarget.type = 'datetime-local';
            }}
          />
        </div>
        <button
          ref={props.submitRef}
          type="submit"
          style={{ display: 'none' }}
        />
      </form>
    </>
  );
}
