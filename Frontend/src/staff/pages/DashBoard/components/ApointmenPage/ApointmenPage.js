import React from 'react';
import AppointmentHistory from '../Apointmenthistory/Apointmenthistory';
import { ScheduleWork } from './components/ScheduleWork/ScheduleWork';

const AppointmentsPage = () => {
  return (
    <div>
      <ScheduleWork />
      <AppointmentHistory />
    </div>
    
  );
};

export default AppointmentsPage;
