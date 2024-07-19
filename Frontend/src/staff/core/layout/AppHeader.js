import React, { useEffect, useState } from 'react';
import { CiBellOn } from 'react-icons/ci';
import { PersonalServices } from '../../services/PersonalServices/PersonalServices';
import NotificationDropdown from '../../pages/DashBoard/components/NotificationDropdown/NotificationDropdown';

export const AppHeader = () => {
  const [showBellDropdown, setShowBellDropdown] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [filteredNotifications, setFilteredNotifications] = useState([]);
  const [clinicName, setClinicName] = useState('');

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

    const fetchClinicName = async () => {
      try {
        const response = await PersonalServices.getClinicStaff();
        setClinicName(response);
      } catch (error) {
        console.error('Error fetching clinic name:', error);
      }
    };

    fetchNotifications();
    fetchClinicName();
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
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '0 20px',
        color: '#1890ff',
        position: 'relative', // Add relative positioning to handle absolute positioning of the dropdown
      }}
    >
      <div
        style={{
          flex: 1,
          textAlign: 'center', // Center the clinic name
          color: 'white',
          fontFamily: 'Georgia',
          fontSize: '22px',
        }}
      >
        {clinicName}
      </div>
      <div style={{ position: 'absolute', right: '20px' }}>
        <CiBellOn
          className='bell-icon'
          style={{ cursor: 'pointer', fontSize: '26px', color: 'white' }}
          onClick={handleBellIconClick}
        />
        {showBellDropdown && (
          <div
            style={{
              position: 'absolute',
              top: '40px',
              right: '0', // Adjust this to align with the icon
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
