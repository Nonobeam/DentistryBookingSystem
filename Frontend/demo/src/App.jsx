import React from 'react';
import 'antd/dist/reset.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Signup from './account/Signup';
import Login from './account/Login';
import ForgotPassword from './account/Forgot';
import Services from './homepage/Services';
import Educational from './homepage/Educational';
import Booking from './homepage/Booking';
import Homepage from './homepage/Homepage';
import { DashBoard } from './admin/pages/DashBoard';
import { DentistList } from './admin/pages/DashBoard/components/DentistList/DentistList';
import { CarDash } from './admin/pages/DashBoard/components/CarDash/CarDash';
import AppointmentsPage from './admin/pages/DashBoard/components/ApointmenPage/ApointmenPage';
import { TimeTable } from './admin/pages/DashBoard/components/Timetable/Timetable';
import Profile from './admin/pages/DashBoard/components/Profile/Profile';
import EditForm from './admin/pages/DashBoard/components/EditForm/EditForm';
import AdminHomePage from './admin/pages/DashBoard/components/AdminHomePage';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route exact path='/' element={<Homepage />} />
        <Route exact path='/booking' element={<Booking />} />
        <Route path='/educational' element={<Educational />} />
        <Route path='/services' element={<Services />} />
        <Route path='/signup' element={<Signup />} />
        <Route path='/login' element={<Login />} />
        <Route path='/forgot' element={<ForgotPassword />} />
      </Routes>
      <Routes>
        <Route path='/dashboard' element={<DashBoard />}>
          <Route path='dentist-list' element={<DentistList />} />
          <Route path='' element={<CarDash />} />
          <Route path='appointment-history' element={<AppointmentsPage />} />
          <Route path='admin-home-page' element={<AdminHomePage />} />
          <Route path='timetable' element={<TimeTable />} />
        </Route>
        <Route path='/profile' element={<Profile />} />
        <Route path='/editform' element={<EditForm />} />
      </Routes>
    </Router>
  );
};

export default App;
