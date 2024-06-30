import React from 'react';
import AppointmentHistory from '../Apointmenthistory/Apointmenthistory';
import { appointmentData } from '../../../../utils/data';


const AppointmentsPage = () => {
  return (
    <div>
      <AppointmentHistory appointments={appointmentData} />
    </div>
  );
};

export default AppointmentsPage;
