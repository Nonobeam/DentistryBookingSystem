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
      const response = await api.get(`/staff/notification`);
      return response.data;
    } catch (err) {
      console.log(err);
    }
  }, getClinicStaff: async () => {
    try {
      const response = await api.get(`/staff/clinic`);
      return response.data;
    } catch (err) {
      console.log(err);
    }
  },
  updateNotificationStaff: async ({notificationID, mail, subject, text}) => {
    try {
      const response = await api.post(`/staff/send-mail?${notificationID }?mail=${mail}&subject=${subject}&text=${text}`);
      return response.data;
    } catch (err) {
      console.log(err);
    }
  },
};
