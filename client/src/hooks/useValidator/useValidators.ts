import useAuthValidations from './validations/useAuthValidations';

export default function useValidators() {
  const authValidations = useAuthValidations();

  return {
    ...authValidations,
  };
}
