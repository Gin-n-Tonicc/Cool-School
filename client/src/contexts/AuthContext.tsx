import Cookies from 'js-cookie';
import { PropsWithChildren, createContext, useContext } from 'react';
import { AUTH_COOKIE_KEY_JWT } from '../constants/cookieConstants';
import { useLocalStorage } from '../hooks/useLocalStorage';
import { RolesEnum } from '../types/enums/RolesEnum';
import { IAuthStorage } from '../types/interfaces/IAuthStorage';
import { IUser } from '../types/interfaces/IUser';
import { isJwtExpired } from '../utils/jwtUtils';

type AuthContextType = {
  user: Partial<IAuthStorage>;
  isAuthenticated: boolean;
  hasFinishedOAuth2: boolean;
  isTeacher: boolean;
  updateUser: (v: IUser) => void;
  loginUser: (v: IUser) => void;
  logoutUser: () => void;
  removeRefreshToken: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: PropsWithChildren) {
  const { value: auth, setStorageData: setAuth } =
    useLocalStorage<IAuthStorage>('auth', {});

  const updateUser: AuthContextType['updateUser'] = (object) => {
    setAuth((oldUser) => ({ ...oldUser, ...object }));
  };

  const loginUser: AuthContextType['loginUser'] = (user) => {
    setAuth({
      email: user.email,
      id: user.id,
      username: user.username,
      firstname: user.firstname,
      role: user.role,
      additionalInfoRequired: user.additionalInfoRequired,
    });
  };

  const logoutUser: AuthContextType['logoutUser'] = () => {
    setAuth({});
  };

  const removeRefreshToken: AuthContextType['removeRefreshToken'] = () => {
    setAuth((auth) => ({ ...auth, refreshToken: undefined }));
  };

  const jwt = Cookies.get(AUTH_COOKIE_KEY_JWT);
  const isAuthenticated = Boolean(jwt) && !isJwtExpired(jwt);
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
        removeRefreshToken,
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
