import React, { useEffect, useState } from 'react';
import { PersonalServices } from '../../../../staff/services/PersonalServices/PersonalServices';
import NotificationDropdown from '../../../../staff/pages/DashBoard/components/NotificationDropdown/NotificationDropdown';



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

  

  const handleNotificationClick = (notification) => {
    // Logic to handle notification click
    console.log('Notification clicked:', notification);
  };

  

  return (
    
      <div style={{ position: 'relative' }}>
        
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
      
  );
};
