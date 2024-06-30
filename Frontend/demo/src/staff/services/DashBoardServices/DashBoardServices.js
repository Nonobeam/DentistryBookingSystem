import { notification } from 'antd';
import api from '../../config/axios/axios';

export const DashBoardServices = {
  getAll: async (data) => {
    try {
      const responseData = await api.get('staff/dashboard', data);
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
