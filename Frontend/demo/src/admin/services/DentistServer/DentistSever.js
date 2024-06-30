import { notification } from 'antd';
import api from '../../config/axios';


export const CustomerServices = {
  getAll: async () => {
    try {
      const responseData = await api.get('admin/dentistList');
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