import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';
import { useFetch } from '../../../hooks/useFetch';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import Spinner from '../../common/spinner/Spinner';

// The component used to handle logout
export default function Logout() {
  const { logoutUser } = useAuthContext();
  const navigate = useNavigate();

  // Logout on mount
  const { loading } = useFetch(apiUrlsConfig.auth.logout, []);

  // Clear user from state and redirect to home
  useEffect(() => {
    if (!loading) {
      logoutUser();
      navigate(PagesEnum.Home);
    }
  }, [loading]);

  return <Spinner />;
}
