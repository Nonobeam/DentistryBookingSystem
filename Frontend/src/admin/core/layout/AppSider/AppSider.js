import React from 'react';
import { Menu } from 'antd';
import { Link } from 'react-router-dom';
import { UserOutlined, TeamOutlined, IdcardOutlined, SolutionOutlined } from '@ant-design/icons';
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
    icon: <IdcardOutlined />,
    link: '/admin/dentist-list-admin'
  },
  {
    key: 'StaffList',
    label: 'Staff List',
    icon: <TeamOutlined />,
    link: '/admin/staff-list'
  },
  {
    key: 'ManagerList',
    label: 'Manager List',
    icon: <SolutionOutlined />,
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
      window.location.href = '/';    
    },
  }
];

export const AppSider = () => {
  return (
    <div
      style={{ 
        boxShadow: '2px 0 8px rgba(0,0,0,0.15)', 
        borderRight: '1px solid #e8e8e8', 
        height: '100vh',
        display: 'flex',
        flexDirection: 'column'
      }}
    >
      <div 
        className="logo" 
        style={{ 
          flex: '0 0 auto', 
          backgroundColor: 'white',
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '140px',
        }}
      >
        <Link to="/">
          <img
            src={require("../../../../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png")}
            alt="Logo"
            style={{ height: "120px", objectFit: 'contain' }}
          />
        </Link>
      </div>
      <Menu 
        mode='inline' 
        defaultSelectedKeys={['CustomerList']} 
        style={{ 
          borderRight: 'none', 
          flex: 1,
          backgroundColor: 'transparent',
          padding: '20px 0'
        }}
      >
        {items.map((item) => (
          <Menu.Item 
            key={item.key} 
            icon={React.cloneElement(item.icon, { style: { fontSize: '18px', color: '#1976d2' } })}
            style={{ 
              margin: '10px 0',
              borderRadius: '0 20px 20px 0',
              transition: 'all 0.3s',
              paddingLeft: '24px', // Thêm padding bên trái
            }}
            onClick={item.onClick}
          >
            <Link 
              to={item.link} 
              style={{ 
                color: '#333', 
                textDecoration: 'none',
                fontSize: '14px',
                fontWeight: 400,
                display: 'flex',
                alignItems: 'center',
              }}
            >
              <span style={{ marginLeft: '10px' }}>{item.label}</span>
            </Link>
          </Menu.Item>
        ))}
      </Menu>
    </div>
  );
};