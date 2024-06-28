import React, { useEffect, useState } from 'react';
import { BiSearch } from 'react-icons/bi';
import { CiBellOn } from 'react-icons/ci';
import { FaUserCircle, FaCog } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { CardNotification as Notification } from '../../pages/DashBoard/components/CarNotification/CarNotification';
import { Button, Dropdown, Menu } from 'antd';
import { PersonalServices } from '../../services/PersonalServices/PersonalServices';

const NotificationDropdown = ({ onClose }) => {
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

  return (
    <div
      style={{
        position: 'fixed',
        top: '50px',
        right: '20px',
        padding: '10px',
        background: '#fff',
        color: '#000',
        zIndex: 999,
        boxShadow: '0px 0px 5px 0px rgba(0,0,0,0.5)', // Add box shadow for better visibility
        borderRadius: '5px',
        // Add border radius for rounded corners
      }}>
      {notifications.map((notification) => (
        <Notification
          key={notification.notificationID}
          content={notification.message}
        />
      ))}
    </div>
  );
};

export const AppHeader = () => {
  const [showBellDropdown, setShowBellDropdown] = useState(false);
  const [showUserDropdown, setShowUserDropdown] = useState(false);

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
        <Link to='/dashboard/profile'>Profile</Link>
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
      }}>
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
          <NotificationDropdown onClose={() => setShowBellDropdown(false)} />
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
