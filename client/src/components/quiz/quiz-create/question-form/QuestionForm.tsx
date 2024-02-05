import { SubmitHandler, useForm } from 'react-hook-form';
import { v4 as uuidV4 } from 'uuid';
import { IDefaultObject } from '../../../../types/interfaces/IDefaultObject';
import { IQuestion } from '../../../../types/interfaces/IQuestion';
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

export default function QuestionForm(props: QuestionFormProps) {
  const {
    handleSubmit,
    control,
    reset,
    formState: { errors },
  } = useForm<QuestionInputs>({
    defaultValues: {
      Description: '',
      Marks: '',
    },
    mode: 'onChange',
  });

  const onSubmit: SubmitHandler<QuestionInputs> = (data) => {
    const question: QuestionFormQuestion = {
      description: data.Description,
      marks: Number(data.Marks),
      customId: uuidV4(),
    };

    reset();
    props.onSubmit(question);
  };

  return (
    <>
      <div
        className="accordion quiz-subform-wrapper"
        id="quizQuestionCreateAccordion">
        <div className="quiz-subform-title-wrapper">
          <h4>Въпроси</h4>
          <button
            className="btn_4"
            data-toggle="collapse"
            data-target="#quizQuestionCreateContent"
            aria-expanded="true"
            aria-controls="quizQuestionCreateContent">
            Създай
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
                type="text"
                iconClasses="zmdi zmdi-face material-icons-name"
              />
              <FormInput
                control={control}
                name="Marks"
                type="number"
                iconClasses="zmdi zmdi-face material-icons-name"
              />
            </div>

            <div className="form-group form-button">
              <input
                type="submit"
                name="signup"
                id="signup"
                className="btn_4  "
                value="Създай въпрос"
              />
            </div>
          </form>
        </div>
      </div>
    </>
  );
}
