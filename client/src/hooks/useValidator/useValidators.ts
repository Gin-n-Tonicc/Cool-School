import useAuthValidations from './validations/useAuthValidations';
import useCommonValidations from './validations/useCommonValidations';
import useCourseCreateValidations from './validations/useCourseCreateValidations';
import useCourseSubsectionValidations from './validations/useCourseSubsectionValidations';

export default function useValidators() {
  const commonValidations = useCommonValidations();
  const courseSubsectionValidations = useCourseSubsectionValidations();
  const courseCreateValidations = useCourseCreateValidations();
  const authValidations = useAuthValidations();

  return {
    common: commonValidations,
    courseSubsection: courseSubsectionValidations,
    courseCreate: courseCreateValidations,
    auth: authValidations,
  };
}
