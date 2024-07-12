import React from 'react';
import { Menu } from 'antd';
import { Link } from 'react-router-dom';
import { UserOutlined } from '@ant-design/icons';
import { LuLogOut } from 'react-icons/lu';

const items = [
  {
    key: 'CustomerList',
    label: 'Customer List',
    icon: <UserOutlined />,
    link: '/admin/customer-list-admin'
  },
  {
    key: 'DentistList',
    label: 'Dentist List',
    icon: <UserOutlined />,
    link: '/admin/dentist-list-admin'
  },
  {
    key: 'StaffList',
    label: 'Staff List',
    icon: <UserOutlined />,
    link: '/admin/staff-list'
  },
  {
    key: 'ManagerList',
    label: 'Manager List',
    icon: <UserOutlined />,
    link: '/admin/manager-list'
  },
  {
    key: 'Logout',
    label: 'Logout',
    icon: <LuLogOut />,
    onClick: () => {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      localStorage.removeItem("expirationTime");
      window.location.href = '/'; // Redirect to the homepage after logout// Remove access token or any other stored data    
 
    },
  }
];

export const AppSider = () => {
  return (
    <div
    style={{ backgroundColor: '#fff', boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)', borderRight: 'none', height: '100vh' }}
  >
    <div className='demo-logo-vertical' style={{ height: '0px' }} />
    <div className="logo" style={{ flex: '0 0 auto', backgroundColor: 'white' }}>
        <Link to="/">
          <img
            src={require("G:/fu/kif 5/swp/New folder/thu/DentistryBookingSystem/Frontend/demo/src/img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png")}
            alt="Logo"
            style={{ height: "140px", paddingBottom: "10px", paddingLeft: "15px"}}
          />
        </Link>
      </div>
    <div >
      <div className='demo-logo-vertical' style={{ height: '40px' }} />
      <Menu mode='inline' defaultSelectedKeys={['CustomerList']} style={{ borderRight: 'none', textAlign: 'left' }}>
        {items.map((item) => (
          <Menu.Item key={item.key} style={{ margin: 0 }}>
            <Link to={item.link} style={{ color: '#333', textDecoration: 'none' }}>
              {item.icon}
              <span style={{ marginLeft: '10px' }}>{item.label}</span>
            </Link>
          </Menu.Item>
        ))}
      </Menu>
    </div>
    </div>
  );
};
