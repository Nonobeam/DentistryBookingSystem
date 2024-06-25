import { notification } from 'antd';
import api from '../../config/axios/axios';

export const DentistServices = {
  getAll: async () => {
    try {
      const responseData = await api.get('staff/dentist/all');
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Error',
        description: error.message,
        onClick: () => {
          console.log('Notification Clicked!');
        },
      });
    }
  },
  getDentistById: async (id) => {
    try {
      const responseData = await api.get(`staff/dentistList/${id}`);
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Error',
        description: error.message,
        onClick: () => {
          console.log('Notification Clicked!');
        },
      });
    }
  },
};
