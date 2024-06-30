import api from '../../config/axios/axios';

export const PersonalServices = {
  getPersonalInfo: async () => {
    try {
      const response = await api.get(`/staff/info`);
      return response.data;
    } catch (err) {
      console.log(err);
    }
  },
  updatePersonalInfo: async (data) => {
    try {
      const response = await api.put(`/staff/info/update`, data);
      return response.data;
    } catch (err) {
      console.log(err);
    }
  },
  getNotificationStaff: async () => {
    try {
      const response = await api.get(`/staff`);
      return response.data;
    } catch (err) {
      console.log(err);
    }
  },
};
