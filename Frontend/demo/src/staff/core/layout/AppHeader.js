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
  const [showUserDropdown, setShowUserDropdown] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [searchValue, setSearchValue] = useState(''); // State for search input value
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

  // Function to handle search input change
  const handleSearchInputChange = (e) => {
    const { value } = e.target;
    setSearchValue(value);

    // Perform filtering based on search value
    const filtered = notifications.filter((notification) =>
      notification.title.toLowerCase().includes(value.toLowerCase())
    );
    setFilteredNotifications(filtered);
  };

  const handleBellIconClick = () => {
    setShowBellDropdown(!showBellDropdown);
  };

  const handleUserIconClick = () => {
    setShowUserDropdown(!showUserDropdown);
  };

  const handleLogout = () => {
    // Perform logout action here
    console.log('Logging out...');

    // Example: Clear token from localStorage and redirect to login page
    localStorage.removeItem('accessToken'); // Remove access token or any other stored data
    message.success('Logged out successfully'); // Show a success message (optional)
    
    // Redirect to login page
    window.location.href = '/login';
  };

  const handleNotificationClick = (notification) => {
    // Logic to handle notification click
    console.log('Notification clicked:', notification);
  };

  const menu = (
    <Menu>
      <Menu.Item key='profile'>
        <Link to='/staff/profile'>Profile</Link>
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
        value={searchValue}
        onChange={handleSearchInputChange} // Handle input change
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
              notifications={filteredNotifications} // Pass filtered notifications to dropdown
              onNotificationClick={handleNotificationClick}
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
