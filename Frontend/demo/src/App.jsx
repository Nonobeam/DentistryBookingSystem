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
import { DashBoard } from './staff/pages/DashBoard';
import { DentistList } from './staff/pages/DashBoard/components/DentistList/DentistList';
import { CarDash } from './staff/pages/DashBoard/components/CarDash/CarDash';
import AppointmentsPage from './staff/pages/DashBoard/components/ApointmenPage/ApointmenPage';
import { TimeTable } from './staff/pages/DashBoard/components/Timetable/Timetable';
import Profile from './staff/pages/DashBoard/components/Profile/Profile';
import EditForm from './staff/pages/DashBoard/components/EditForm/EditForm';
import AdminHomePage from './staff/pages/DashBoard/components/AdminHomePage/AdminHomePage';
import Schedule from './staff/pages/DashBoard/components/Timetable/Schedule/Schedule';
import RoleBasedRoute from './account/RoleBasedRoute';
import NotAuthorized from './account/NotAuthorized';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route exact path='/' element={<Homepage />} />
        <Route path='/booking' element={<RoleBasedRoute element={<Booking />} requiredRole="user" />}/>
        <Route path='/educational' element={<Educational />} />
        <Route path='/services' element={<Services />} />
        <Route path='/signup' element={<Signup />} />
        <Route path='/login' element={<Login />} />
        <Route path='/forgot' element={<ForgotPassword />} />
        <Route path='/not-authorized' element={<NotAuthorized />} />
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
        <Route path='/schedule' element={<Schedule />} />

      </Routes>
    </Router>
  );
};

export default App;
