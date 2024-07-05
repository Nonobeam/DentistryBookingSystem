// NotificationDropdown.jsx

import React, { useState } from 'react';
import { Menu, Modal, Form, Input, Button } from 'antd';
import { PersonalServices } from '../../../../services/PersonalServices/PersonalServices';

const { Item: FormItem } = Form;

const NotificationDropdown = ({ notifications, onNotificationClick }) => {
  const [showForm, setShowForm] = useState(false);
  const [selectedNotification, setSelectedNotification] = useState(null);

  const handleMenuClick = (notification) => {
    setSelectedNotification(notification);
    setShowForm(true);
    onNotificationClick(notification); // Notify parent component about the click
  };

  const handleCancel = () => {
    setShowForm(false);
    setSelectedNotification(null);
  };

  const handleFormSubmit = async (values) => {
    try {
      const { email, subject, message } = values;
      const notificationID = selectedNotification.notificationID;
      await PersonalServices.updateNotificationStaff({
        notificationID,
        mail: email,
        subject,
        text: message,
      });
      console.log('Email sent successfully!');
      setShowForm(false);
      setSelectedNotification(null);
    } catch (error) {
      console.error('Error sending email:', error);
      // Handle error (e.g., show error message to the user)
    }
  };
  return (
    <>
      <Menu>
        {notifications.map((notification) => (
          <Menu.Item
            key={notification.notificationID}
            onClick={() => handleMenuClick(notification)}
          >
            {notification.message}
          </Menu.Item>
        ))}
      </Menu>

      {selectedNotification && (
        <Modal
          title="Send Email"
          visible={showForm}
          onCancel={handleCancel}
          footer={null}
        >
          <Form onFinish={handleFormSubmit}>
            <FormItem
              label="Email"
              name="email"
              rules={[{ required: true, message: 'Please input your email!' }]}
            >
              <Input placeholder="Enter email" />
            </FormItem>
            <FormItem
              label="Subject"
              name="subject"
              rules={[{ required: true, message: 'Please input the subject!' }]}
            >
              <Input placeholder="Enter subject" />
            </FormItem>
            <FormItem
              label="Message"
              name="message"
              rules={[{ required: true, message: 'Please input the message!' }]}
            >
              <Input.TextArea rows={4} placeholder="Enter message" />
            </FormItem>
            <FormItem>
              <Button type="primary" htmlType="submit">
                Send
              </Button>
            </FormItem>
          </Form>
        </Modal>
      )}
    </>
  );
};

export default NotificationDropdown;
