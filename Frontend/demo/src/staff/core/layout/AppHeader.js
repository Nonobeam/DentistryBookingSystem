import React, { useEffect, useState } from 'react';
import { BiSearch } from 'react-icons/bi';
import { CiBellOn } from 'react-icons/ci';
import { FaUserCircle } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { Button, Dropdown, Menu, message } from 'antd';
import NotificationDropdown from '../../pages/DashBoard/components/NotificationDropdown/NotificationDropdown';
import { PersonalServices } from '../../services/PersonalServices/PersonalServices';

export const AppHeader = () => {
  const [showBellDropdown, setShowBellDropdown] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [filteredNotifications, setFilteredNotifications] = useState([]);

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        const response = await PersonalServices.getNotificationStaff();
        setNotifications(response);
        setFilteredNotifications(response); // Initialize filtered notifications with all notifications
      } catch (error) {
        console.error('Error fetching notifications:', error);
      }
    };
    fetchNotifications();
  }, []);

  
  const handleBellIconClick = () => {
    setShowBellDropdown(!showBellDropdown);
  };



  const handleNotificationClick = (notification) => {
    console.log('Notification clicked:', notification);
  };


  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'flex-end',
        alignItems: 'center',
        gap: '20px',
        color: '#1890ff',
      }}
    >
      
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
              background: '#F5F5F5',
              boxShadow: '0px 0px 5px 0px rgba(0,0,0,0.5)',
              borderRadius: '8px',
              padding: '10px',
              zIndex: 999,
            }}
          >
            <NotificationDropdown
              notifications={filteredNotifications} // Pass filtered notifications to dropdown
              onNotificationClick={handleNotificationClick}
            />
          </div>
        )}
      </div>

    </div>
  );
};
