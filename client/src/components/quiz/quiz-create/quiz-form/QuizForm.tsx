import { RefObject } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
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

export default function QuizForm(props: QuizFormProps) {
  const { quizCreate } = useValidators();

  const {
    handleSubmit,
    control,
    reset,
    watch,
    register,
    setValue,
    formState: { errors },
  } = useForm<QuizFormInputs>({
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

    props.onSubmit(quiz);
  };

  return (
    <>
      <h4>Данни за тест</h4>
      <form
        className="register-form"
        id="register-form"
        onSubmit={handleSubmit(onSubmit)}>
        <FormInput
          control={control}
          name="Title"
          type="text"
          iconClasses="zmdi zmdi-face material-icons-name"
          rules={quizCreate.TITLE_VALIDATIONS}
        />
        <FormInput
          control={control}
          name="Description"
          type="text"
          iconClasses="zmdi zmdi-face material-icons-name"
          rules={quizCreate.DESCRIPTION_VALIDATIONS}
        />
        <div className="number-controls">
          <FormInput
            control={control}
            name="Attempt Limit"
            type="number"
            iconClasses="zmdi zmdi-face material-icons-name"
            rules={quizCreate.ATTEMPT_LIMIT_VALIDATIONS}
          />
          <FormInput
            control={control}
            name="Minute Duration"
            type="number"
            iconClasses="zmdi zmdi-face material-icons-name"
            rules={quizCreate.QUIZ_DURATION_VALIDATIONS}
          />
        </div>

        <div className="number-controls">
          <FormInput
            control={control}
            name="Start Date"
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
