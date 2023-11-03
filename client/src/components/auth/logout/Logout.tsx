import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';

export default function Logout() {
  const { logoutUser } = useAuthContext();
  const navigate = useNavigate();

  const { loading } = useFetch(apiUrlsConfig.auth.logout, []);

  useEffect(() => {
    if (!loading) {
      logoutUser();
      navigate('/');
    }
  }, [loading]);

  return <p>Logging out...</p>;
}
