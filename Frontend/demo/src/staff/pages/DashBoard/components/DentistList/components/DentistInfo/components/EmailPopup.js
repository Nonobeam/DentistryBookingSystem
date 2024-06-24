import { Button, DatePicker, Input, Popconfirm, Popover } from 'antd';
import React from 'react';
import { HiOutlineMail } from 'react-icons/hi';

const onChange = (date, dateString) => {
  console.log(date, dateString);
};

export const EmailPopup = () => {
  return (
    <Popover
      content={
        <div>
          <p>Appointment Date:</p>
          <DatePicker onChange={onChange} />
          <p>Message:</p>
          <Input placeholder='Basic usage' />
        </div>
      }
      trigger='click'
      placement='bottom'
      title={'Send Email to Customer'}>
      <Button
        icon={<HiOutlineMail style={{ marginRight: '5px' }} />}
        style={{
          marginBottom: '10px',
          display: 'block',
          marginLeft: 'auto',
          color: 'white',
          backgroundColor: 'blue',
          borderColor: 'blue',
        }}>
        Notify Email
      </Button>
    </Popover>
  );
};
