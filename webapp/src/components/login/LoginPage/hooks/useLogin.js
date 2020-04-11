import { useCallback, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';

import authenticate from '../../../../api/auth/authenticate';
import setApiToken from '../../../../utils/setApiToken';

// eslint-disable-next-line import/prefer-default-export
export function useLogin() {
  const [errorMessage, setErrorMessage] = useState(null);
  const dismissError = useCallback(() => setErrorMessage(null), []);

  const history = useHistory();
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
          history.replace(from);
        })
        .catch(() => {
          setErrorMessage('Login failed');
        });
    },
    [from],
  );

  return {
    dismissError,
    errorMessage,
    handleLogin,
  };
}
