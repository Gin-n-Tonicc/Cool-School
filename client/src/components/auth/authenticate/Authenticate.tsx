import { PropsWithChildren } from 'react';
import useAuthenticate from '../../../hooks/useAuthenticate';
import Spinner from '../../common/spinner/Spinner';

export default function Authenticate({ children }: PropsWithChildren) {
  const { loading } = useAuthenticate();

  return <>{loading ? <Spinner /> : children}</>;
}
