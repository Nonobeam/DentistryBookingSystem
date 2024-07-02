import { notification } from 'antd';
import api from '../../config/axios/axios';

const TimetableServices = {
  
  getAll: async ({ date, numDay }) => {
    try {
      const responseData = await api.get(`staff/timetable/${date}?numDay=${numDay}`);
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Booking Failed',
        description: error.message,
      });
      throw error;
    }
  },
  
};

export default TimetableServices;
