import { notification } from 'antd';
import api from '../../config/axios/axios';

export const DashBoardServices = {
 
  getAll: async ({
    date,
    year,
  }) => {
    try {
      const responseData = await api.get(`staff/dashboard?date=${date}&year=${year}`);
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
