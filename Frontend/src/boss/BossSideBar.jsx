import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, TeamOutlined, BarChartOutlined, UserOutlined } from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';

const { Sider } = Layout;

const BossSidebar = () => {
  const location = useLocation();

  const pathToKey = {
    '/boss': '1',
    '/boss/manager': '2',
    '/boss/service': '3',
    '/boss/profile': '4'
  };

  const menuItems = [
    {
      key: '1',
      icon: <BarChartOutlined />,
      label: 'Dashboard',
      link: '/boss'
    },
    {
      key: '2',
      icon: <TeamOutlined />,
      label: 'Manager List',
      link: '/boss/manager'
    },
    {
      key: '3',
      icon: <HomeOutlined />,
      label: 'Service',
      link: '/boss/service'
    },
    {
      key: '4',
      icon: <UserOutlined />,
      label: 'Profile',
      link: '/boss/profile'
    },
    {
      key: '5',
      icon: <LuLogOut />,
      label: 'Logout',
      onClick: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('expirationTime');
        window.location.href = '/';
      }
    }
  ];

  return (
    <Sider 
      width={250} 
      style={{
        backgroundColor: 'white',
        boxShadow: '2px 0 8px rgba(0,0,0,0.15)',
      }}
    >
      <div 
        className="logo" 
        style={{ 
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '120px',
          backgroundColor: 'white',
        }}
      >
        <Link to="/">
          <img
            src={require("../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png")}
            alt="Logo"
            style={{ height: "100px", objectFit: 'contain' }}
          />
        </Link>
      </div>

      <Menu
        mode="inline"
        defaultSelectedKeys={[pathToKey[location.pathname]]}
        style={{ 
          height: '100%', 
          borderRight: 0,
          backgroundColor: 'transparent',
        }}
      >
        {menuItems.map(item => (
          <Menu.Item
            key={item.key}
            icon={React.cloneElement(item.icon, {
              style: { fontSize: '18px', color: '#1976d2' }
            })}
            onClick={item.onClick}
            style={{
              margin: '10px 0',
              borderRadius: '0 20px 20px 0',
              transition: 'all 0.3s',
            }}
          >
            {item.link ? (
              <Link 
                to={item.link}
                style={{
                  color: '#333',
                  textDecoration: 'none',
                  fontSize: '14px',
                  fontWeight: 400,
                }}
              >
                {item.label}
              </Link>
            ) : (
              <span
                style={{
                  color: '#333',
                  fontSize: '14px',
                  fontWeight: 400,
                }}
              >
                {item.label}
              </span>
            )}
          </Menu.Item>
        ))}
      </Menu>
    </Sider>
  );
};

export default BossSidebar;