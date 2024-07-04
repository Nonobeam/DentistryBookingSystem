import React, { useEffect, useState } from 'react';
import { BiSearch } from 'react-icons/bi';
import { CiBellOn } from 'react-icons/ci';
import { FaUserCircle } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { Button, Dropdown, Menu } from 'antd';
 // Đường dẫn tới NotificationDropdown

import { PersonalServices } from '../../services/PersonalServices/PersonalServices';
import NotificationDropdown from '../../pages/DashBoard/components/NotificationDropdown/NotificationDropdown';

export const AppHeader = () => {
  const [showBellDropdown, setShowBellDropdown] = useState(false);
  const [showUserDropdown, setShowUserDropdown] = useState(false);
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await PersonalServices.getNotificationStaff();
        setNotifications(response);
      } catch (error) {
        console.error('Error fetching notifications:', error);
      }
    };
    fetchNotifications();
  }, []);

  const handleBellIconClick = () => {
    setShowBellDropdown(!showBellDropdown); // Toggle bell dropdown visibility
  };

  const handleUserIconClick = () => {
    setShowUserDropdown(!showUserDropdown); // Toggle user dropdown visibility
  };

  const handleLogout = () => {
    // Logic for logout action
    console.log('Logging out...');
  };

  const menu = (
    <Menu>
      <Menu.Item key='profile'>
        <Link to='/staff/profile'>Profile</Link>
      </Menu.Item>
      <Menu.Item key='settings'>
        <Link to='/settings'>Settings</Link>
      </Menu.Item>
      <Menu.Item key='logout'>
        <Button type='link' onClick={handleLogout}>
          Logout
        </Button>
      </Menu.Item>
    </Menu>
  );

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'flex-end',
        alignItems: 'center',
        gap: '20px',
        color: '#fff',
      }}
    >
      <input
        style={{
          border: 'none',
          width: '140px',
          background: 'transparent',
          padding: '5px',
          outline: 'none',
        }}
        type='text'
        placeholder='Search'
      />
      <BiSearch className='search-icon' style={{ cursor: 'pointer' }} />
      <div style={{ position: 'relative' }}>
        <CiBellOn
          className='bell-icon'
          style={{ cursor: 'pointer', fontSize: '20px', color: '#333' }}
          onClick={handleBellIconClick}
        />
        {showBellDropdown && (
          <div
            style={{
              position: 'absolute',
              top: '40px',
              right: '10px',
              minWidth: '300px', // Adjust the width as needed
              background: '#fff',
              boxShadow: '0px 0px 5px 0px rgba(0,0,0,0.5)',
              borderRadius: '8px',
              padding: '10px',
              zIndex: 999,
            }}
          >
            <NotificationDropdown
              notifications={notifications}
              onClose={() => setShowBellDropdown(false)}
            />
          </div>
        )}
      </div>
      <Dropdown overlay={menu} placement='bottomRight' trigger={['click']}>
        <div>
          <FaUserCircle
            className='user-icon'
            style={{ cursor: 'pointer', fontSize: '20px', color: '#333' }}
            onClick={handleUserIconClick}
          />
        </div>
      </Dropdown>
    </div>
  );
};
