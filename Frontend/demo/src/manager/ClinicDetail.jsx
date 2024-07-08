import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Layout, Button, Form, Input, TimePicker, Select, message } from 'antd';
import axios from 'axios';
import moment from 'moment';
import ManagerSidebar from './ManagerSidebar';

const { Content } = Layout;
const { Option } = Select;

const ClinicDetail = () => {
  const location = useLocation();
  const history = useNavigate();
  const clinic = location.state?.clinic;

  const [form] = Form.useForm();

  if (!clinic) {
    message.error('No clinic data found');
    history('/manager/clinics');
    return null;
  }

  const handleEditClinic = async (values) => {
    try {
      const token = localStorage.getItem('token');
      const payload = {
        ...values,
        id: clinic.id,
        slotDuration: {
          hour: values.slotDuration.hour(),
          minute: values.slotDuration.minute(),
          second: values.slotDuration.second(),
          nano: 0,
        },
        openTime: {
          hour: values.openTime.hour(),
          minute: values.openTime.minute(),
          second: values.openTime.second(),
          nano: 0,
        },
        closeTime: {
          hour: values.closeTime.hour(),
          minute: values.closeTime.minute(),
          second: values.closeTime.second(),
          nano: 0,
        },
        breakStartTime: {
          hour: values.breakStartTime.hour(),
          minute: values.breakStartTime.minute(),
          second: values.breakStartTime.second(),
          nano: 0,
        },
        breakEndTime: {
          hour: values.breakEndTime.hour(),
          minute: values.breakEndTime.minute(),
          second: values.breakEndTime.second(),
          nano: 0,
        },
      };
      await axios.put('http://localhost:8080/api/v1/manager/editClinic', payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success('Clinic details updated successfully');
    } catch (error) {
      console.error('There was an error updating the clinic!', error);
      message.error('Failed to update clinic');
    }
  };

  const handleNewStaff = () => {
    // Implement the logic for adding a new staff member
    message.info('New staff functionality to be implemented');
  };

  const handleNewDentist = () => {
    // Implement the logic for adding a new dentist
    message.info('New dentist functionality to be implemented');
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <ManagerSidebar />
      <Layout className="site-layout">
        <Content style={{ margin: '24px 16px 0', minHeight: 280 }}>
          <h2>Clinic Detail</h2>
          <Form
            form={form}
            layout="vertical"
            onFinish={handleEditClinic}
            initialValues={{
              ...clinic,
              slotDuration: moment(clinic.slotDuration, 'HH:mm:ss'),
              openTime: moment(clinic.openTime, 'HH:mm:ss'),
              closeTime: moment(clinic.closeTime, 'HH:mm:ss'),
              breakStartTime: moment(clinic.breakStartTime, 'HH:mm:ss'),
              breakEndTime: moment(clinic.breakEndTime, 'HH:mm:ss'),
              status: clinic.status,
            }}
          >
            <Form.Item
              label="Name"
              name="name"
              rules={[{ required: true, message: 'Please input the clinic name!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item
              label="Phone"
              name="phone"
              rules={[{ required: true, message: 'Please input the phone number!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item
              label="Address"
              name="address"
              rules={[{ required: true, message: 'Please input the address!' }]}
            >
              <Input />
            </Form.Item>
            <Form.Item
              label="Slot Duration"
              name="slotDuration"
              rules={[{ required: true, message: 'Please input the slot duration!' }]}
            >
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item
              label="Open Time"
              name="openTime"
              rules={[{ required: true, message: 'Please input the opening time!' }]}
            >
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item
              label="Close Time"
              name="closeTime"
              rules={[{ required: true, message: 'Please input the closing time!' }]}
            >
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item
              label="Break Start Time"
              name="breakStartTime"
              rules={[{ required: true, message: 'Please input the break start time!' }]}
            >
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item
              label="Break End Time"
              name="breakEndTime"
              rules={[{ required: true, message: 'Please input the break end time!' }]}
            >
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item
              label="Status"
              name="status"
              rules={[{ required: true, message: 'Please select the status!' }]}
            >
              <Select>
                <Option value={1}>Active</Option>
                <Option value={0}>Inactive</Option>
              </Select>
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit">
                Save Changes
              </Button>
            </Form.Item>
          </Form>
          <Button type="primary" onClick={handleNewStaff} style={{ marginRight: '16px' }}>
            New Staff
          </Button>
          <Button type="primary" onClick={handleNewDentist}>
            New Dentist
          </Button>
        </Content>
      </Layout>
    </Layout>
  );
};

export default ClinicDetail;
