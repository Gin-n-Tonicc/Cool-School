import useAuthValidations from './validations/useAuthValidations';
import useBlogCreateValidations from './validations/useBlogCreateValidations';
import useCommonValidations from './validations/useCommonValidations';
import useCourseCreateValidations from './validations/useCourseCreateValidations';
import useCourseSubsectionValidations from './validations/useCourseSubsectionValidations';

export default function useValidators() {
  const commonValidations = useCommonValidations();
  const authValidations = useAuthValidations();
  const courseSubsectionValidations = useCourseSubsectionValidations();
  const courseCreateValidations = useCourseCreateValidations();
  const blogCreateValidations = useBlogCreateValidations();

  return {
    common: commonValidations,
    auth: authValidations,
    courseSubsection: courseSubsectionValidations,
    courseCreate: courseCreateValidations,
    blogCreate: blogCreateValidations,
  };
}
