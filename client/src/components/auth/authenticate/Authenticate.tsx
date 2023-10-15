import { PropsWithChildren, useEffect } from 'react';
import { CachePolicies, useFetch } from 'use-http';
import { useAuthContext } from '../../../contexts/AuthContext';
import { IAuthResponse } from '../../../types/interfaces/IAuthResponse';
import { initialAuthUtils } from '../../../utils/initialAuthUtils';
import Spinner from '../../common/spinner/Spinner';

export default function Authenticate({ children }: PropsWithChildren) {
  const { user, loginUser, logoutUser } = useAuthContext();

  const { post, response, loading } = useFetch<IAuthResponse>(
    `${process.env.REACT_APP_API_URL}/auth/me`,
    { cachePolicy: CachePolicies.NO_CACHE }
  );

  useEffect(() => {
    async function fetchApi() {
      const data = await post({ accessToken: user.accessToken });
      initialAuthUtils.finishInitialAuth();

      if (response.ok) {
        loginUser(data);
      } else {
        logoutUser();
      }
    }

    fetchApi();
  }, []);

  return <>{loading ? <Spinner /> : children}</>;
}
