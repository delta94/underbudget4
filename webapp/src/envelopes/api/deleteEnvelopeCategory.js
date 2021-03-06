import axios from 'axios';

import getAuthHeaders from '../../common/utils/getAuthHeaders';

export default (id) =>
  axios.delete(`/api/envelope-categories/${id}`, {
    headers: getAuthHeaders(),
  });
