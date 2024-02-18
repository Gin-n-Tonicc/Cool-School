import { PropsWithChildren, createContext, useContext } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';
import { RolesEnum } from '../types/enums/RolesEnum';
import { IAuthResponse } from '../types/interfaces/auth/IAuthResponse';
import { IAuthStorage } from '../types/interfaces/auth/IAuthStorage';
import { IUser } from '../types/interfaces/auth/IUser';
import { deleteJwtCookie, deleteRefreshCookie } from '../utils/cookieUtils';
import { isJwtExpired } from '../utils/jwtUtils';

type AuthContextType = {
  user: Partial<IAuthStorage>;
  isAuthenticated: boolean;
  hasFinishedOAuth2: boolean;
  isTeacher: boolean;
  updateUser: (v: IUser) => void;
  loginUser: (v: IAuthResponse) => void;
  logoutUser: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: PropsWithChildren) {
  const { value: auth, setStorageData: setAuth } =
    useLocalStorage<IAuthStorage>('auth', {});

  const updateUser: AuthContextType['updateUser'] = (object) => {
    setAuth((oldUser) => ({ ...oldUser, ...object }));
  };

  const loginUser: AuthContextType['loginUser'] = (authResponse) => {
    const user = authResponse.user;

    setAuth({
      email: user?.email,
      id: user?.id,
      username: user?.username,
      firstname: user?.firstname,
      role: user?.role,
      additionalInfoRequired: user?.additionalInfoRequired,
      accessToken: authResponse.accessToken,
      refreshToken: authResponse.refreshToken,
    });
  };

  const logoutUser: AuthContextType['logoutUser'] = () => {
    setAuth({});
    deleteJwtCookie();
    deleteRefreshCookie();
  };

  const isAuthenticated =
    Boolean(auth.accessToken) && !isJwtExpired(auth.accessToken);
  const hasFinishedOAuth2 =
    isAuthenticated && !Boolean(auth.additionalInfoRequired);

  const isTeacher = auth.role === RolesEnum.TEACHER;

  return (
    <AuthContext.Provider
      value={{
        user: auth,
        isAuthenticated,
        hasFinishedOAuth2,
        isTeacher,
        loginUser,
        logoutUser,
        updateUser,
      }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuthContext = () => {
  const authContext = useContext(AuthContext);

  if (!authContext) {
    throw new Error(
      'useAuthContext has to be used within <AuthContext.Provider>'
    );
  }

  return authContext;
};
