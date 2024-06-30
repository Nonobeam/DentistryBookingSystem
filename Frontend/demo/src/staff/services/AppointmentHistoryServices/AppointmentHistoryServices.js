import { notification } from 'antd';
import api from '../../config/axios/axios';

export const AppointmentHistoryServices = {
  getAll: async () => {
    try {
      const responseData = await api.get('staff/appointment-history');
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
