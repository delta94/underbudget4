import { useCallback, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import authenticate from '../../../api/auth/authenticate';
import setApiToken from '../../../utils/setApiToken';

// eslint-disable-next-line import/prefer-default-export
export function useLogin() {
  const [errorMessage, setErrorMessage] = useState(null);
  const dismissError = useCallback(() => setErrorMessage(null), []);

  const navigate = useNavigate();
  const location = useLocation();
  const { from } = location.state || { from: { pathname: '/' } };

  const handleLogin = useCallback(
    async (values) => {
      return authenticate({
        password: values.password,
        source: 'me',
      })
        .then((res) => {
          setApiToken(res.data.token);
          setErrorMessage(null);
          navigate(from, { replace: true });
        })
        .catch(() => {
          setErrorMessage('Login failed');
        });
    },
    [from, navigate],
  );

  return {
    dismissError,
    errorMessage,
    handleLogin,
  };
}
