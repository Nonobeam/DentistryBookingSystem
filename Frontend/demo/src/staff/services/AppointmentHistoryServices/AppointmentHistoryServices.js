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
  getAllServices: async (workDate) => {
    try {
      const responseData = await api.get(
        `staff/booking/all-service?workDate=${workDate}`
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
  getAllAvailableSchedule: async ({ workDate, servicesID }) => {
    try {
      const responseData = await api.get(
        `staff/booking/available-schedules?workDate=${workDate}&servicesId=${servicesID}`
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
  makeBooking: async ({
    dependentID,
    customerMail,
    serviceId,
    dentistScheduleId,
  }) => {
    try {
      const responseData = await api.post(
        `staff/booking/make-booking/${dentistScheduleId}?dependentID=${dependentID}&customerMail=${customerMail}&serviceId=${serviceId}`
      );
      return responseData.data;
    } catch (error) {
      notification.error({
        message: 'Booking Failed',
        description: error.message,
      });
    }
  },
};
