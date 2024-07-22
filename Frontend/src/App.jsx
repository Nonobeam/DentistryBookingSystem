import React from 'react';
import 'antd/dist/reset.css';
import './styles.css'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Signup from './account/Signup';
import Login from './account/Login';
import ForgotPassword from './account/Forgot';
import Services from './homepage/Services';
import Educational from './homepage/Educational';
import Booking from './homepage/Booking';
import Homepage from './homepage/Homepage';
import UserFeedback from'./homepage/Feedback';
import { DashBoard } from './staff/pages/DashBoard';
import { DentistList } from './staff/pages/DashBoard/components/DentistList/DentistList';
import { StaffDashboard } from './staff/pages/DashBoard/components/StaffDash/StaffDashboard';
import AppointmentsPage from './staff/pages/DashBoard/components/ApointmenPage/ApointmenPage';
import { TimeTable } from './staff/pages/DashBoard/components/Timetable/Timetable';
import StaffProfile from './staff/pages/DashBoard/components/Profile/Profile';
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
import History from './homepage/History';
import UserProfile from './account/UserProfile';
import TodayAppointments from './dentist/TodayAppointments';
import DenHistory from './dentist/DenHistory';
import DenProfile from './dentist/DenProfile';
import PatientInfo from './dentist/PatientInfo';
import DentistSchedule from './dentist/DentistSchedule';
import ManagerStaffList from './manager/ManagerStaffList';
import ManagerDentistList from './manager/ManagerDentistList';
import ManagerClinicList from './manager/ManagerClinicList';
import ManagerDashboard from './manager/ManagerDashboard';
import BossDashboard from './boss/BossDashboard';
import BossService from './boss/BossService';
import BossManagerList from './boss/BossManagerList';
import BossProfile from './boss/BossProfile';
import ManagerProfile from './manager/ManagerProfile';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="*" element={<NotAuthorized />} />
        {/* HOMEPAGES */}
        <Route exact path='/' element={<Homepage />} />
        <Route path='/educational' element={<Educational />} />
        <Route path='/services' element={<Services />} />

        {/* ERROR PAGES */}
        <Route path='/not-authorized' element={<NotAuthorized />} />

        {/* AUTHORIZE PAGES */}
        <Route path='/signup' element={<Signup />} />
        <Route path='/login' element={<Login />} />
        <Route path='/forgot' element={<ForgotPassword />} />
        <Route path='/api/v1/auth/confirm' element={<ConfirmAccount />} />
        <Route path='/resetPassword/:token' element={<ResetPassword />} />

        {/* CUSTOMER'S PAGES */}
        <Route path='/booking' element={<RoleBasedRoute element={<Booking />} requiredRole={['CUSTOMER']}/>}/>
        <Route path='/history' element={<RoleBasedRoute element={<History />} requiredRole={['CUSTOMER']}/>}/>
        <Route path='/profile' element={<RoleBasedRoute element={<UserProfile />} requiredRole={['CUSTOMER']}/>}/>
        <Route path='/appointment-feedback' element={<RoleBasedRoute element={<UserFeedback/>} requiredRole={['CUSTOMER']}/>}/>

        {/* DENTIST PAGES */}
        <Route path='/dentist'>
          <Route path='' element={<RoleBasedRoute element={<TodayAppointments />} requiredRole={['DENTIST']}/>}/>
          <Route path='profile' element={<RoleBasedRoute element={<DenProfile />} requiredRole={['DENTIST']}/>}/>
          <Route path='history' element={<RoleBasedRoute element={<DenHistory />} requiredRole={['DENTIST']}/>}/>
          <Route path='patient/:customerID' element={<RoleBasedRoute element={<PatientInfo />} requiredRole={['DENTIST']}/>}/>
          <Route path='schedule' element={<RoleBasedRoute element={<DentistSchedule />} requiredRole={['DENTIST']}/>}/> 
        </Route>

        {/* MANAGER PAGES */}
        <Route path='/manager'>
          <Route path='' element={<RoleBasedRoute element={<ManagerDashboard />} requiredRole={['MANAGER']}/>}/>
          <Route path='staff' element={<RoleBasedRoute element={<ManagerStaffList />} requiredRole={['MANAGER']}/>}/>
          <Route path='dentist/' element={<RoleBasedRoute element={<ManagerDentistList />} requiredRole={['MANAGER']}/>}/>
          <Route path='dentist/:id' element={<RoleBasedRoute element={<ManagerDentistList />} requiredRole={['MANAGER']}/>}/>
          <Route path='clinic' element={<RoleBasedRoute element={<ManagerClinicList />} requiredRole={['MANAGER']}/>} />
          <Route path='profile' element={<RoleBasedRoute element={<ManagerProfile />} requiredRole={['MANAGER']}/>}/>
        </Route>

        {/* BOSS PAGES */}
        <Route path='/boss'>
          <Route path='' element={<RoleBasedRoute element={<BossDashboard />} requiredRole={['BOSS']}/>}/>
          <Route path='service' element={<RoleBasedRoute element={<BossService />} requiredRole={['BOSS']}/>}/>
          <Route path='manager' element={<RoleBasedRoute element={<BossManagerList />} requiredRole={['BOSS']}/>}/>
          <Route path='profile' element={<RoleBasedRoute element={<BossProfile />} requiredRole={['BOSS']}/>}/>
        </Route>

        {/* STAFF PAGES */}
        <Route path='/staff' element={<RoleBasedRoute element={<DashBoard />} requiredRole={['STAFF']} />}>
          <Route path='' element={<RoleBasedRoute element={<StaffDashboard />} requiredRole={['STAFF']} />} />
          <Route path='dentist-list' element={<RoleBasedRoute element={<DentistList />} requiredRole={['STAFF']} />} />
          <Route path='dentist-list/detail/:dentistID' element={<RoleBasedRoute element={<DentistInfo />} requiredRole={['STAFF']} />} />
          <Route path='appointment-history' element={<RoleBasedRoute element={<AppointmentsPage />} requiredRole={['STAFF']} />} />
          <Route path='timetable' element={<RoleBasedRoute element={<TimeTable />} requiredRole={['STAFF']} />} />
          <Route path='profile' element={<RoleBasedRoute element={<StaffProfile />} requiredRole={['STAFF']} />} />
          <Route path='customer-list' element={<RoleBasedRoute element={<CustomerListDash />} requiredRole={['STAFF']} />} />
          <Route path='customer-list/detail/:customerID' element={<RoleBasedRoute element={<CustomerInfo />} requiredRole={['STAFF']} />} />
          <Route path='schedule' element={<RoleBasedRoute element={<Schedule />} requiredRole={['STAFF']} />} /> 
       </Route>

          {/* ADMIN PAGES */}
          <Route path='/admin' element={<RoleBasedRoute element={<Pages />} requiredRole={['ADMIN']} />}>
            <Route path='customer-list-admin' element={<RoleBasedRoute element={<CustomerList />} requiredRole={['ADMIN']} />} />
            <Route path='dentist-list-admin' element={<RoleBasedRoute element={<DentistListAmin />} requiredRole={['ADMIN']} />} />
            <Route path='manager-list' element={<RoleBasedRoute element={<ManagerList />} requiredRole={['ADMIN']} />} />
            <Route path='staff-list' element={<RoleBasedRoute element={<StaffList />} requiredRole={['ADMIN']} />} />
          </Route>

      </Routes>
    </Router>
  );
};

export default App;
