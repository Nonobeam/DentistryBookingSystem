import React, { useState } from 'react';
import { BiSearch } from 'react-icons/bi';
import { CiBellOn } from 'react-icons/ci';
import { FaUserCircle, FaCog } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { CardNotification as Notification } from '../../pages/DashBoard/components/CarNotification/CarNotification';

const NotificationDropdown = ({ onClose }) => {
  // Dummy notifications data
  const notifications = [
    { id: 1, message: 'Notification 1' },
    { id: 2, message: 'Notification 2' },
    { id: 3, message: 'Notification 3' },
  ];

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
      {/* <button
        style={{
          backgroundColor: '#f0f0f0',
          color: '#000',
          border: 'none',
          borderRadius: '3px',
          padding: '5px 10px',
          cursor: 'pointer',
          marginBottom: '10px',
        }}
        onClick={onClose}
      >
        Close
      </button> */}
      {notifications.map((notification) => (
        <Notification key={notification.id} content={notification.message} />
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
      <Link to='/profile'>
        <FaUserCircle
          className='user-icon'
          style={{ cursor: 'pointer', fontSize: '20px', color: '#333' }}
          onClick={handleUserIconClick}
        />
      </Link>
      {showUserDropdown && (
        <div
          style={{
            position: 'absolute',
            top: '50px',
            right: '50px',
            background: '#fff',
            padding: '10px',
            borderRadius: '5px',
            boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)',
          }}>
          <ul style={{ listStyleType: 'none', padding: 0 }}>
            <li>
              <Link to='/profile'>Profile</Link>
            </li>
            <li>
              <Link to='/settings'>Settings</Link>
            </li>
            <li>
              <button onClick={() => setShowUserDropdown(false)}>Logout</button>
            </li>
          </ul>
        </div>
      )}
      <FaCog
        className='settings-icon'
        style={{ cursor: 'pointer', fontSize: '20px', color: '#333' }}
      />
    </div>
  );
};
