import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Profile from '../../pages/DashBoard/components/Profile/Profile';
import { DashBoard } from '../../pages/DashBoard';
import { StaffDash } from '../../pages/DashBoard/components/StafDash/StaffDashboard';
import { DentistList } from '../../pages/DashBoard/components/DentistList/DentistList';
import EditForm from '../../pages/DashBoard/components/EditForm/EditForm';
import AppointmentsPage from '../../pages/DashBoard/components/ApointmenPage/ApointmenPage';
import { TimeTable } from '../../pages/DashBoard/components/Timetable/Timetable';


export const AppRouting = () => {
  return (
    <Routes>
      <Route path='/dashboard' element={<DashBoard />}>
        <Route path='dentist-list' element={<DentistList />} />
        <Route path='' element={<StaffDash />} />
        <Route path='appointment-history' element={<AppointmentsPage />} />
        <Route path='timetable' element={<TimeTable />} />
      </Route>
      <Route path='/profile' element={<Profile />} />
      <Route path='/editform' element={<EditForm />} />
    </Routes>
  );
};
