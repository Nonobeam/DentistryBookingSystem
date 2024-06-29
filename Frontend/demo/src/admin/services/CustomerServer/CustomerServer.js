import { notification } from 'antd';
import api from '../../config/axios/axios';

export const DentistServices = {
  getAll: async () => {
    try {
      const responseData = await api.get('staff/dentistList');
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