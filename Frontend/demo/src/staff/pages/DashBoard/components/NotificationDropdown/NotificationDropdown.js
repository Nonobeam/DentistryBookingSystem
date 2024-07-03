import React from 'react';
import { Menu } from 'antd';

const NotificationDropdown = ({ notifications, onClose }) => {
  const handleMenuClick = (notificationId) => {
    // Implement your logic when a notification is clicked, e.g., mark as read
    console.log(`Notification ${notificationId} clicked`);
    // Close the dropdown or perform any other action
    onClose();
  };

  return (
    <Menu
      style={{
        marginTop: '8px', // Adjust spacing from the top
        boxShadow: '0px 8px 16px 0px rgba(0,0,0,0.2)', // Box shadow for dropdown
        borderRadius: '8px', // Rounded corners
        minWidth: '400px', // Minimum width of the dropdown
      }}
    >
      {notifications.map((notification) => (
        <Menu.Item
          key={notification.notificationID}
          onClick={() => handleMenuClick(notification.notificationID)}
          style={{
            padding: '12px', // Padding around each notification item
            borderBottom: '1px solid #f0f0f0', // Bottom border for separation
            cursor: 'pointer', // Pointer cursor on hover
            whiteSpace: 'normal', // Wrap long text in multiple lines
          }}
        >
          <span
            style={{
              display: 'block',
              fontSize: '14px',
              lineHeight: '18px',
              color: '#333', // Text color
            }}
          >
            {notification.message}
          </span>
        </Menu.Item>
      ))}
    </Menu>
  );
};

export default NotificationDropdown;
