import React from 'react';
import AppointmentHistory from '../Apointmenthistory/Apointmenthistory';
import { appointmentData } from '../../../../utils/data';


const AppointmentsPage = () => {
    // const appointments = [
    //     {
    //       date: '2024-06-15',
    //       time: '10:00 AM',
    //       patientName: 'John Doe',
    //       dentist: 'Dr. Smith',
    //       treatment: 'Checkup',
    //     },
    //     {
    //       date: '2024-06-16',
    //       time: '11:00 AM',
    //       patientName: 'Jane Doe',
    //       dentist: 'Dr. Johnson',
    //       treatment: 'Cleaning',
    //     },
    //     {
    //       date: '2024-06-17',
    //       time: '12:00 PM',
    //       patientName: 'Alice Smith',
    //       dentist: 'Dr. Brown',
    //       treatment: 'Extraction',
    //     },
    //     {
    //       date: '2024-06-18',
    //       time: '2:00 PM',
    //       patientName: 'Bob Jones',
    //       dentist: 'Dr. Lee',
    //       treatment: 'Fillings',
    //     },
    //     {
    //       date: '2024-06-19',
    //       time: '3:00 PM',
    //       patientName: 'Emily Davis',
    //       dentist: 'Dr. Garcia',
    //       treatment: 'Root Canal',
    //     },
    //     // Add more appointment objects as needed
    //   ];
      

  return (
    <div>
      <AppointmentHistory appointments={appointmentData} />
    </div>
  );
};

export default AppointmentsPage;
