/* eslint-disable react/prop-types */
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';

import LoginPage from './LoginPage';

export default {
  title: 'login/LoginPage',
  component: LoginPage,
  decorators: [
    (story) => story({ mock: new MockAdapter(axios) }),
    (story) => <MemoryRouter>{story()}</MemoryRouter>,
  ],
};

export const LoginFailure = (_, { mock }) => {
  mock.onPost('/api/authenticate').reply(401);
  return <LoginPage />;
};

export const LoginSuccess = (_, { mock }) => {
  mock.onPost('/api/authenticate').reply(201, { token: 'generated-auth-token' });
  return <LoginPage />;
};
