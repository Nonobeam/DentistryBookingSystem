import React from 'react';
import AppointmentHistory from '../Apointmenthistory/Apointmenthistory';
import { appointmentData } from '../../../../utils/data';
import { ScheduleWork } from './components/ScheduleWork/ScheduleWork';

const AppointmentsPage = () => {
  return (
    <div>
      <ScheduleWork />
      <AppointmentHistory appointments={appointmentData} />
    </div>
  );
};

export default AppointmentsPage;
