import useAuthValidations from './validations/useAuthValidations';
import useBlogCreateValidations from './validations/useBlogCreateValidations';
import useCommonValidations from './validations/useCommonValidations';
import useCourseCreateValidations from './validations/useCourseCreateValidations';
import useCourseSubsectionValidations from './validations/useCourseSubsectionValidations';
import useQuizCreateValidations from './validations/useQuizCreateValidations';

// The hook that groups all of the other validation error hooks into one
export default function useValidators() {
  const commonValidations = useCommonValidations();
  const authValidations = useAuthValidations();
  const courseSubsectionValidations = useCourseSubsectionValidations();
  const courseCreateValidations = useCourseCreateValidations();
  const blogCreateValidations = useBlogCreateValidations();
  const quizCreateValidations = useQuizCreateValidations();

  return {
    common: commonValidations,
    auth: authValidations,
    courseSubsection: courseSubsectionValidations,
    courseCreate: courseCreateValidations,
    blogCreate: blogCreateValidations,
    quizCreate: quizCreateValidations,
  };
}
