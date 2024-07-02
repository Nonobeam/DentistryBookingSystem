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
import { Profile } from './staff/pages/DashBoard/components/Profile/Profile';
import EditForm from './staff/pages/DashBoard/components/EditForm/EditForm';
import AdminHomePage from './staff/pages/DashBoard/components/AdminHomePage/AdminHomePage';
import Schedule from './staff/pages/DashBoard/components/Timetable/Schedule/Schedule';
import DentistInfo from './staff/pages/DashBoard/components/DentistList/components/DentistInfo/DentistInfo';
import RoleBasedRoute from './account/RoleBasedRoute';
import NotAuthorized from './account/NotAuthorized';
import ConfirmAccount from './account/ConfirmAccount';
import ResetPassword from './account/ResetPassword';
import { CustomerList } from './admin/pages/CustomerList/CustomerList';
import ManagerList from './admin/pages/ManagerList/ManagerList';
import StaffList from './admin/pages/StaffList/StaffList';
import DentistListAmin from './admin/pages/DentistListAmin/DentistListAmin';
import { Pages } from './admin/pages';
import { CustomerListDash } from './staff/pages/DashBoard/components/CustomerList/CustomerList';
import CustomerInfo from './staff/pages/DashBoard/components/CustomerList/components/CustomerInfo/CustomerInfo';



const App = () => {
  return (
    <Router>
      <Routes>
        <Route exact path='/' element={<Homepage />} />
        <Route
          path='/booking'
          element={
            <RoleBasedRoute
              element={<Booking />}
              requiredRole={['CUSTOMER', 'MANAGER']}
            />
          }
        />
        <Route path='/educational' element={<Educational />} />
        <Route path='/services' element={<Services />} />
        <Route path='/signup' element={<Signup />} />
        <Route path='/login' element={<Login />} />
        <Route path='/forgot' element={<ForgotPassword />} />
        <Route path='/not-authorized' element={<NotAuthorized />} />
        <Route path='/confirm' element={<ConfirmAccount />} />
        <Route path='/resetPassword/:token' element={<ResetPassword />} />

        <Route path='/staff' element={<DashBoard />}>
          <Route path='' element={<AdminHomePage />} />
          <Route path='dentist-list' element={<DentistList />} />
          <Route
            path='dentist-list/detail/:dentistID'
            element={<DentistInfo />}
          />

          <Route path='dashboard' element={<CarDash />} />
          <Route path='appointment-history' element={<AppointmentsPage />} />
          <Route path='timetable' element={<TimeTable />} />
          <Route path='profile' element={<Profile />} />
          <Route path='customer-list' element={<CustomerListDash />} />
          <Route path='customer-list/detail/:customerID' element={<CustomerInfo />} />
        </Route>

        <Route path='/editform' element={<EditForm />} />
        <Route path='/schedule' element={<Schedule />} />
        

        <Route path='/admin' element={<Pages />}>
          <Route path='customer-list-admin' element={<CustomerList />} />
          <Route path='dentist-list-admin' element={<DentistListAmin />} />
          <Route path='manager-list' element={<ManagerList />} />
          <Route path='staff-list' element={<StaffList />} />
        </Route>
      </Routes>
    </Router>
  );
};

export default App;
