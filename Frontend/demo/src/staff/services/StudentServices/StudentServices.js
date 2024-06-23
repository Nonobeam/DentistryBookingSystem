import axios from 'axios';

const apiEndPoint = 'https://65459271fe036a2fa95473f5.mockapi.io/api/Student';

export const StudentServices = {
  getAll: () => {
    try {
      const responseData = axios.get(apiEndPoint);
      return responseData;
    } catch (error) {
      return error;
    }
  },
  getById: (id) => {
    try {
      const responseData = axios.get(`${apiEndPoint}/${id}`);
      return responseData;
    } catch (error) {
      return error;
    }
  },
};
