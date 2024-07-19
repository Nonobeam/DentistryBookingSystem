import { notification } from 'antd';
import api from '../../config/axios';


export const ManagerServices = {
  getAll: async () => {
    try {
      const responseData = await api.get('admin/managerList');
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