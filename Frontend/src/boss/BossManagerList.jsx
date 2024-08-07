import React, { useState, useEffect } from 'react';
import { Layout, Table, Button, Modal, message, Form, Input, DatePicker } from 'antd';
import BossSidebar from './BossSideBar';
import axios from 'axios';


const BossManagerList = () => {
  const [managers, setManagers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const [selectedManager, setSelectedManager] = useState(null);
  const [createFormVisible, setCreateFormVisible] = useState(false);
  const { Header, Content } = Layout;

  const [form] = Form.useForm();

  useEffect(() => {
    fetchManagers();
  }, []);

  const fetchManagers = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get('http://localhost:8080/api/v1/boss/all-manager', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setManagers(response.data); 
    } catch (error) {
      console.error('Error fetching managers:', error);
      message.error('Failed to fetch managers');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteManager = async (managerID) => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`http://localhost:8080/api/v1/boss/delete-manager/${managerID}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success('Manager deleted successfully');
      fetchManagers(); 
    } catch (error) {
      console.error('Error deleting manager:', error);
      message.error('Failed to delete manager');
    }
    setDeleteModalVisible(false); 
  };
  const handleCreateManager = async (values) => {
    try {
      const token = localStorage.getItem("token");
      await axios.post('http://localhost:8080/api/v1/boss/register/manager', values, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success('Manager created successfully');
      setCreateFormVisible(false);
      form.resetFields(); 
      fetchManagers(); 
    } catch (error) {
      // Check if the error response contains a custom message
      const errorMessage = error.response?.data?.message || 'Error creating manager';
      console.error('Error creating manager:', error);
      message.error(errorMessage); // Display the custom error message
    }
  };
  
  const columns = [
    { title: 'Name', dataIndex: 'name', key: 'name' },
    { title: 'Phone', dataIndex: 'phone', key: 'phone' },
    { title: 'Mail', dataIndex: 'mail', key: 'mail' },
    { title: 'Birthday', dataIndex: 'birthday', key: 'birthday' },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <span style={{ color: status === 1 ? 'green' : 'yellow' }}>{status === 1 ? 'Active' : 'Inactive'}</span>
      ),
    },
    {
      title: '',
      key: 'action',
      render: (text, record) =>
        record.status === 1 ? (
          <Button style={{color: 'red'}} onClick={() => {
            setSelectedManager(record);
            setDeleteModalVisible(true);
          }}>Delete</Button>
        ) : null,
    },
  ];

  return (
    <Layout style={{ minHeight: "100vh" }}>
          <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />
      <BossSidebar />
      <Layout style={{ padding: '24px 24px' }}>
        <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
          <h1>Manager List</h1>
          <Button type="primary" style={{ marginBottom: 16 }} onClick={() => setCreateFormVisible(true)}>
            Create Manager
          </Button>
          <Table
            dataSource={managers}
            columns={columns}
            loading={loading}
            rowKey={(record) => record.id}
          />
          <Modal
            title="Confirm Delete"
            open={deleteModalVisible}
            onOk={() => handleDeleteManager(selectedManager.id)}
            onCancel={() => setDeleteModalVisible(false)}
            okText="Accept"
            cancelText="Cancel"
          >
            <p>Are you sure you want to delete {selectedManager ? selectedManager.name : ''}?</p>
          </Modal>
          <Modal
            title="Create Manager"
            open={createFormVisible}
            onCancel={() => {
              setCreateFormVisible(false);
              form.resetFields();
            }}
            onOk={() => {
              form.validateFields().then(values => {
                handleCreateManager(values);
              }).catch(error => {
                console.error('Validation failed:', error);
              });
            }}
            okText="Create"
            cancelText="Cancel"
          >
            <Form form={form} layout="vertical">
              <Form.Item
                name="name"
                label="Name"
                rules={[{ required: true, message: 'Please enter the manager\'s name' }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="phone"
                label="Phone"
                rules={[{ required: true, message: 'Please enter the phone number' }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="mail"
                label="Mail"
                rules={[{ required: true, message: 'Please enter the email address' }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="birthday"
                label="Birthday"
                rules={[{ required: true, message: 'Please select the birthday' }]}
              >
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
              <Form.Item
                name="password"
                label="Password"
                rules={[{ required: true, message: 'Please enter the password' }]}
              >
                <Input.Password />
              </Form.Item>
            </Form>
          </Modal>
        </Content>
      </Layout>
    </Layout>
  );
};

export default BossManagerList;
