import { notification } from 'antd';
import api from '../../config/axios/axios';

export const CustomerServicess = {
  getAll: async () => {
    try {
      const responseData = await api.get('staff/customerList');
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
    getCustomerById: async (mail) => {
      try {
        const responseData = await api.get(`staff/customerList/${mail}`);
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
