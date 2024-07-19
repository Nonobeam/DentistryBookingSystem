import React, { useEffect, useState } from 'react';
import { ServiceList } from '../ServiceList/ServiceList';
import { AppointmentHistoryServices } from '../../../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';
import { Booking } from '../Booking/Booking';
export const ScheduleWork = () => {
  const [services, setServices] = useState([]);
  const [workDate, setWorkDate] = useState(
    new Date().toISOString().slice(0, 10)
  );
  useEffect(() => {
    const fetchServices = async () => {
      const response = await AppointmentHistoryServices.getAllServices(
        workDate
      );
      setServices(response);
    };
    fetchServices();
  }, [workDate]);
  return (
    <>
      {/* <ServiceList services={services} /> */}
      <Booking />
    </>
  );
};
