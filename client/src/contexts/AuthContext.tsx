import { PropsWithChildren, createContext, useContext } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';
import { IAuthStorage } from '../interfaces/IAuthStorage';
import { IUser } from '../interfaces/IUser';

type AuthContextType = {
  user: Partial<IAuthStorage>;
  isAuthenticated: boolean;
  updateUser: (object: IUser) => void;
  loginUser: ({
    accessToken,
    refreshToken,
    user,
  }: {
    accessToken: string;
    refreshToken: string;
    user: IUser;
  }) => void;
  logoutUser: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: PropsWithChildren) {
  const { value: auth, setStorageData: setAuth } =
    useLocalStorage<IAuthStorage>('auth', {});

  const updateUser: AuthContextType['updateUser'] = (object) => {
    setAuth((oldUser) => ({ ...oldUser, ...object }));
  };

  const loginUser: AuthContextType['loginUser'] = ({
    accessToken,
    refreshToken,
    user,
  }) => {
    setAuth({
      accessToken,
      refreshToken,
      email: user.email,
      _id: user._id,
      username: user.username,
      firstname: user.firstname,
    });
  };

  const logoutUser: AuthContextType['logoutUser'] = () => {
    setAuth({});
  };

  const isAuthenticated = Boolean(auth?.accessToken);

  return (
    <AuthContext.Provider
      value={{
        user: auth,
        isAuthenticated,
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
