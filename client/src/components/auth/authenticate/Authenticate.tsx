import { PropsWithChildren } from 'react';
import useAuthenticate from '../../../hooks/useAuthenticate';
import Spinner from '../../common/spinner/Spinner';

// The component used to authenticate the user on mount
export default function Authenticate({ children }: PropsWithChildren) {
  // Authenticate user
  const { loading } = useAuthenticate();

  return <>{loading ? <Spinner /> : children}</>;
}
