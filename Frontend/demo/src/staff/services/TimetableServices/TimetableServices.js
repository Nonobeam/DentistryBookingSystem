import { notification } from 'antd';
import api from '../../config/axios/axios';

const TimetableServices = {
  getAll: async ({ date, numDay }) => {
    try {
      const responseData = await api.get(
        `staff/timetable/${date}?numDay=${numDay}`
      );
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Booking Failed',
        description: error.message,
      });
      throw error;
    }
  },
  // ShowScheduleServices.js

  getAllDentists: async () => {
    try {
      const responseData = await api.get('/staff/dentist-schedule');
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Failed to fetch dentist list',
        description: error.message,
      });
      throw error;
    }
  },
  getAllDentistsAndTimeSlot: async (date) => {
    try {
      const responseData = await api.get(
        `/staff/show-set-schedule?date=${date}`
      );
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Failed to fetch dentist list',
        description: error.message,
      });
      throw error;
    }
  },
  setSchedule: async ({ dentistMail, startDate, endDate, slotNumber }) => {
    try {
      const responseData = await api.post(
        `/staff/set-schedule?dentistMail=${dentistMail}&startDate=${startDate}&endDate=${endDate}&slotNumber=${slotNumber}`
      );
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Failed to set schedule',
        description: error.message,
      });
      throw error;
    }
  },
};

export default TimetableServices;
