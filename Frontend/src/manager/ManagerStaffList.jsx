import React, { useState, useEffect } from 'react';
import { Table, Layout, Button, Modal, Form, Input, Dropdown, Menu, DatePicker, Spin, message } from 'antd';
import { DownOutlined } from '@ant-design/icons';
import axios from 'axios';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';
import ManagerSidebar from './ManagerSidebar';

const { Header, Content } = Layout;

const ManagerStaffList = () => {
  const [staff, setStaff] = useState([]);
  const [editingStaff, setEditingStaff] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [deleteModalVisible, setDeleteModalVisible] = useState(false); 
  const [deletingStaffId, setDeletingStaffId] = useState(null); 
  const [loading, setLoading] = useState(false);
  const [modalLoading, setModalLoading] = useState(false);
  const history = useNavigate();

  const fetchStaff = async () => {
    setLoading(true);
    const token = localStorage.getItem("token");
    try {
      const response = await axios.get('http://localhost:8080/api/v1/manager/all-staff', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setStaff(response.data);
    } catch (error) {
      console.error('There was an error fetching the staff data!', error);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    fetchStaff();
  }, []);

  const showEditModal = (record) => {
    setEditingStaff(record);
    form.setFieldsValue({
      ...record,
      birthday: dayjs(record.birthday)
    });
    setIsModalVisible(true);
  };

  const handleEdit = async () => {
    const values = await form.validateFields();
    const updatedStaff = {
      id: editingStaff.id,
      name: values.name,
      phone: values.phone,
      mail: values.mail,
      birthday: values.birthday.format('YYYY-MM-DD'),
      status: editingStaff.status
    };
    const token = localStorage.getItem("token");
    setModalLoading(true);
    try {
      await axios.put('http://localhost:8080/api/v1/manager/editWorker', updatedStaff, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setIsModalVisible(false);
      setEditingStaff(null);
      fetchStaff();
      message.success("Staff member updated successfully");
    } catch (error) {
      message.error("Failed to update staff member");
    } finally {
      setModalLoading(false);
    }
  };

  const handleDentistList = (id) => {
    history(`/manager/dentist/${id}`);
  };

  const handleDelete = async () => {
    const token = localStorage.getItem("token");
    setModalLoading(true);
    try {
      await axios.delete(`http://localhost:8080/api/v1/manager/delete-user/${deletingStaffId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success("Staff member deleted successfully");
      setDeleteModalVisible(false); 
      fetchStaff();
    } catch (error) {
      console.error('There was an error deleting the staff member!', error);
      message.error(error.response?.data || "An error occurred");
    } finally {
      setModalLoading(false);
    }
  };

  const showDeleteConfirmation = (id) => {
    setDeletingStaffId(id); 
    setDeleteModalVisible(true); 
  };

  const actionsMenu = (record) => (
    <Menu
      items={[
        {
          key: 'dentistList',
          label: 'Dentist List',
          onClick: () => handleDentistList(record.id),
          style:{ color: 'white',background: '#00BFFF' }
        },
        {
          key: 'edit',
          label: 'Edit',
          onClick: () => showEditModal(record),
          style:{ color: 'green' },
        },       
        {
          key: 'delete',
          label: 'Delete',
          onClick: () => showDeleteConfirmation(record.id),
          danger: true,
          style:{ color: 'red' }
        }
        
      ]}
    />
  );

  const actionsDropdown = (record) => (
    <Dropdown overlay={actionsMenu(record)} placement="bottomLeft" trigger={['click']} >
      <Button size='60'>
        <DownOutlined />
      </Button>
    </Dropdown>
  );

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone',
      render: (text) => <span>{text}</span>,
    },
    {
      title: 'Email',
      dataIndex: 'mail',
      key: 'mail',
    },
    {
      title: 'Birthday',
      dataIndex: 'birthday',
      key: 'birthday',
      render: (date) => dayjs(date).format('DD-MM-YYYY')
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: status => (
        <span style={{ color: status === 1 ? 'green' : 'red' }}>{status === 1 ? 'Active' : 'Inactive'}</span>

      ),
    
    },
    {
      title: 'Clinic',
      dataIndex: 'clinicName',
      key: 'clinicName',
    },
    {
      title: '      ',
      key: 'actions',
      render: (record) => actionsDropdown(record)
    }
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <ManagerSidebar />

      <Layout style={{ padding: '0 auto' }}>
      <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />

        <Content style={{ padding: 30, margin: 0, minHeight: 280 }}>
          <h1>Staff List</h1>
          <Spin spinning={loading}>
            <Table columns={columns} dataSource={staff} rowKey="id" />
          </Spin>
        </Content>
      </Layout>
      <Modal
        title="Confirm Delete"
        open={deleteModalVisible}
        onOk={handleDelete}
        onCancel={() => setDeleteModalVisible(false)}
        okText="Delete"
        cancelText="Cancel"
        confirmLoading={modalLoading}
      >
        <p>Are you sure you want to delete this staff member?</p>
      </Modal>
      {/* Edit Staff Modal */}
      <Modal
        title="Edit Staff"
        open={isModalVisible}
        onOk={handleEdit}
        onCancel={() => setIsModalVisible(false)}
        confirmLoading={modalLoading}
      >
        <Form form={form} layout="vertical">
          <Form.Item
            name="name"
            label="Name"
            rules={[{ required: true, message: 'Please enter the name' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="phone"
            label="Phone"
            rules={[
              { required: true, message: 'Please enter the phone number' },
              { pattern: /^\d{10}$/, message: 'Phone number must be 10 digits' }
            ]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="mail"
            label="Email"
            rules={[
              { required: true, message: 'Please enter the email' },
              { type: 'email', message: 'Please enter a valid email' }
            ]}
          >
            <Input disabled/>
          </Form.Item>
          <Form.Item
            name="birthday"
            label="Birthday"
            rules={[{ required: true, message: 'Please enter the birthday' }]}
          >
            <DatePicker format="DD-MM-YYYY" />
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  );
};

export default ManagerStaffList;
