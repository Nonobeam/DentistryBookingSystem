import React, { useState, useEffect } from 'react';
import { Layout, Table, Button, Modal, message, Form, Input} from 'antd';
import BossSidebar from './BossSideBar';
import axios from 'axios';

const { Content } = Layout;

const BossServiceList = () => {
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(false);
  const [deleteModalVisible, setDeleteModalVisible] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [createFormVisible, setCreateFormVisible] = useState(false);

  const [form] = Form.useForm();

  useEffect(() => {
    fetchServices();
  }, []);

  const fetchServices = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get('http://localhost:8080/api/v1/boss/service/all', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setServices(response.data); 
    } catch (error) {
      console.error('Error fetching services:', error);
      message.error('Failed to fetch services all');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteService = async (serviceID) => {
    try {
      const token = localStorage.getItem("token");
      await axios.delete(`http://localhost:8080/api/v1/boss/service/delete/${serviceID}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success('Service deleted successfully');
      fetchServices(); // Refresh manager list after deletion
    } catch (error) {
      console.error('Error deleting service:', error);
      message.error('Failed to delete service');
    }
    setDeleteModalVisible(false); // Close delete confirmation modal
  };

  const handleCreateService = async (values) => {
    try {
      const token = localStorage.getItem("token");
      await axios.post('http://localhost:8080/api/v1/boss/service/add', values, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success('Service created successfully');
      setCreateFormVisible(false); // Close create form modal
      form.resetFields(); // Reset form fields
      fetchServices(); // Refresh manager list after creation
    } catch (error) {
      console.error('Error creating service:', error);
      message.error('Failed to create service');
    }
  };
  const columns = [
    {
      title: 'No.',
      dataIndex: 'index',
      key: 'index',
      render: (text, record, index) => index + 1,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Action',
      key: 'action',
      render: (text, record) =>
        
          <Button type="danger" onClick={() => {
            setSelectedService(record);
            setDeleteModalVisible(true);
          }}>Delete</Button>
  
    },
  ];

  return (
    <>
      <BossSidebar />
      <Layout style={{ padding: '24px 24px' }}>
        <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
          <h2>Service List</h2>
          <Button type="primary" style={{ marginBottom: 16 }} onClick={() => setCreateFormVisible(true)}>
            Create new service
          </Button>
          <Table
            dataSource={services}
            columns={columns}
            loading={loading}
            rowKey={(record) => record.serviceID}
          />
          <Modal
            title="Confirm Delete"
            visible={deleteModalVisible}
            onOk={() => handleDeleteService(selectedService.serviceID)}
            onCancel={() => setDeleteModalVisible(false)}
            okText="Accept"
            cancelText="Cancel"
          >
            <p>Are you sure you want to delete {selectedService ? selectedService.name : ''}?</p>
          </Modal>
          <Modal
            title="Create new service"
            visible={createFormVisible}
            onCancel={() => {
              setCreateFormVisible(false);
              form.resetFields();
            }}
            onOk={() => {
              form.validateFields().then(values => {
                handleCreateService(values);
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
                rules={[{ required: true, message: 'Please enter the service\'s name' }]}
              >
  <Input />
              </Form.Item>
            </Form>
          </Modal>
        </Content>
      </Layout>
    </>
  );
};

export default BossServiceList;