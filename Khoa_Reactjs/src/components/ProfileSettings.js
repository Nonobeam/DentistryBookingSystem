import React from 'react';
import { Avatar, Button, Popover } from 'antd';
import { UserOutlined } from '@ant-design/icons';

export const ProfileSettings = () => {
  const content = () => (
    <div>
      <p>Profile Settings</p>
      <Button type='primary' block>
        Edit Profile
      </Button>
      <Button type='default' block style={{ marginTop: '10px' }}>
        Logout
      </Button>
    </div>
  );
  return (
    <Popover content={content} trigger='click'>
      <Avatar style={{ marginRight: '1rem' }} size={42} icon={<UserOutlined />} />
    </Popover>
  );
};
