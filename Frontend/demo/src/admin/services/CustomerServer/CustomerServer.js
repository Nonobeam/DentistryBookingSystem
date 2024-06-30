import { notification } from 'antd';
import api from '../../config/axios';

export const CustomerServices = {
  getAll: async () => {
    try {
      const responseData = await api.get('admin/customerList');
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
  updateCustomer: async (data) => {
    try {
      const responseData = await api.put(`admin/update/${data.id}`, data);
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
  deleteCustomer: async (id) => {
    try {
      const responseData = await api.delete(`admin/${id}`);
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
