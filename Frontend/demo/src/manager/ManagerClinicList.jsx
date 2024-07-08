import React, { useState, useEffect } from 'react';
import { Table, Layout, Button, Modal, Form, Input, TimePicker, Select, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import moment from 'moment';
import ManagerSidebar from './ManagerSidebar';

const { Content } = Layout;
const { Option } = Select;

const ManagerClinicList = () => {
  const [clinics, setClinics] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();
  const history = useNavigate();

  useEffect(() => {
    fetchClinics();
  }, []);

  const fetchClinics = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/v1/manager/all-clinic', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setClinics(response.data);
    } catch (error) {
      console.error('There was an error fetching the clinic data!', error);
    }
  };

  const showModal = () => {
    setIsModalVisible(true);
  };

  const handleCancel = () => {
    setIsModalVisible(false);
  };

  const handleCreateClinic = async (values) => {
    try {
      const token = localStorage.getItem('token');
      await axios.post('http://localhost:8080/api/v1/manager/create-clinic', values, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      message.success('Clinic created successfully');
      setIsModalVisible(false);
      fetchClinics(); // Refresh the clinic list
    } catch (error) {
      console.error('There was an error creating the clinic!', error);
      message.error('Failed to create clinic');
    }
  };

  const handleRowClick = (clinic) => {
    history(`/manager/clinic/${clinic.id}`, { clinic });
  };

  const formatTime = (timeString) => {
    const [hours, minutes] = timeString.split(':');
    return `${hours}:${minutes}`;
  };

  const formatSlot = (timeString) => {
    const [hours, minutes, seconds] = timeString.split(':');
    return `${minutes}:${seconds}`;
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      render: (text, record) => (
        <a onClick={() => handleRowClick(record)}>{text}</a>
      ),
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone',
    },
    {
      title: 'Address',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: 'Slot Duration',
      dataIndex: 'slotDuration',
      key: 'slotDuration',
      render: (slot) => formatSlot(slot),
    },
    {
      title: 'Open Time',
      dataIndex: 'openTime',
      key: 'openTime',
      render: (time) => formatTime(time),
    },
    {
      title: 'Close Time',
      dataIndex: 'closeTime',
      key: 'closeTime',
      render: (time) => formatTime(time),
    },
    {
      title: 'Break Start Time',
      dataIndex: 'breakStartTime',
      key: 'breakStartTime',
      render: (time) => formatTime(time),
    },
    {
      title: 'Break End Time',
      dataIndex: 'breakEndTime',
      key: 'breakEndTime',
      render: (time) => formatTime(time),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (status === 1 ? 'Active' : 'Inactive'),
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <ManagerSidebar />
      <Layout className="site-layout">
        <Content style={{ margin: '24px 16px 0', minHeight: 280 }}>
          <h2>Clinic List</h2>
          <Button type="primary" onClick={showModal} style={{ marginBottom: '16px' }}>
            Create Clinic
          </Button>
          <Table columns={columns} dataSource={clinics} rowKey="id" />
          <Modal
            title="Create Clinic"
            visible={isModalVisible}
            onCancel={handleCancel}
            footer={null}
          >
            <Form
              form={form}
              layout="vertical"
              onFinish={handleCreateClinic}
              initialValues={{ status: 1 }}
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
                  Create
                </Button>
              </Form.Item>
            </Form>
          </Modal>
        </Content>
      </Layout>
    </Layout>
  );
};

export default ManagerClinicList;
