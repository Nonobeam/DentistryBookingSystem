import React, { useState } from 'react';
import { Button, Modal, Input, Form, Avatar } from 'antd';

const { TextArea } = Input;

export const ModalInfo = ({ open, setOpen, info, showModal }) => {
  const [formData, setFormData] = useState(info);

  const handleCancel = () => {
    setOpen(false);
  };

  const handleSave = () => {
    // Handle save logic here, like sending data to backend
    console.log(formData);
    setOpen(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  return (
    <>
      <Modal
        title='Edit Information'
        open={open}
        onCancel={handleCancel}
        footer={[
          <Button key='cancel' onClick={handleCancel}>
            Cancel
          </Button>,
          <Button key='save' type='primary' onClick={handleSave}>
            Save
          </Button>,
        ]}>
        <div style={{ display: 'flex', justifyContent: 'center' }}>
          <Avatar size={120} src={formData.image} />
        </div>
        <Form layout='vertical' style={{ marginTop: '20px' }}>
          <Form.Item label='Name'>
            <Input name='name' value={formData.name} onChange={handleChange} />
          </Form.Item>
          <Form.Item label='Date of Birth'>
            <Input
              name='dateofbirth'
              value={formData.birthday}
              onChange={handleChange}
            />
          </Form.Item>
          <Form.Item label='Phone'>
            <Input
              name='gender'
              value={formData.gender}
              onChange={handleChange}
            />
          </Form.Item>
          <Form.Item label='Class'>
            <Input
              name='class'
              value={formData.class}
              onChange={handleChange}
            />
          </Form.Item>
          <Form.Item label='Feedback'>
            <TextArea
              name='feedback'
              value={formData.feedback}
              onChange={handleChange}
            />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};
