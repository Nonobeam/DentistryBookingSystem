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
  getDentistById: async (mail) => {
    try {
      const responseData = await api.get(`staff/dentistList/${mail}`);
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
  setService: async (serviceID, dentistID) => {
    try {
      const responseData = await api.post(
        `staff/set-service/?serviceID=${serviceID}&dentistID=${dentistID}`
      );
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
  getAllServices: async (dentistID) => {
    try {
      const responseData = await api.get('staff/show-service');
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
