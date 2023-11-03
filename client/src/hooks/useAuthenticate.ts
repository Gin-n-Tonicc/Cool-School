import { useEffect } from 'react';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../config/apiUrls';
import { useAuthContext } from '../contexts/AuthContext';
import { IAuthResponse } from '../types/interfaces/IAuthResponse';
import { initialAuthUtils } from '../utils/initialAuthUtils';

export default function useAuthenticate(shouldLogoutUser: boolean = true) {
  const { user, loginUser, logoutUser } = useAuthContext();

  const { post, response, loading } = useFetch<IAuthResponse>(
    apiUrlsConfig.auth.me
  );

  useEffect(() => {
    async function fetchApi() {
      const data = await post({ accessToken: user.accessToken });
      initialAuthUtils.finishInitialAuth();

      if (response.ok) {
        loginUser(data);
      } else if (shouldLogoutUser) {
        logoutUser();
      }
    }

    fetchApi();
  }, []);

  return { user, loading };
}
