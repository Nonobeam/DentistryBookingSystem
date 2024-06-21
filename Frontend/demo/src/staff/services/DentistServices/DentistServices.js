import api from '../../config/axios/axios';

export const DentistServices = {
  getAll: async () => {
    try {
      const responseData = await api.get('staff/dentist/all');
      return responseData.data;
    } catch (error) {
      return error;
    }
  },
};
