import { notification } from 'antd';
import api from '../../config/axios/axios';

export const AppointmentHistoryServices = {
  getAll: async (date, search) => {
    try {
      let endPoint = `staff/appointment-history`;
      if (date && search) {
        endPoint = `staff/appointment-history?date=${date}&search=${search}`;
      }
      const responseData = await api.get(endPoint);
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
  patchAppointment: async ({ appointmentId, status }) => {
    try {
      const responseData = await api.patch(
        `user/appointment-history/${appointmentId}?status=${status}`
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
  deteleateAppointment: async ({ appointmentId }) => {
    try {
      const responseData = await api.put(
        `staff/delete-booking/${appointmentId}`
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
  getDependents: async (customerMail) => {
    try {
      const responseData = await api.get(`staff/dependentList/${customerMail}`);
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
