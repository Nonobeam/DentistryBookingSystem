import React, { useState, useEffect } from 'react';
import { Table, Layout, Button, Modal, Form, Input, message } from 'antd';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import ManagerSidebar from './ManagerSidebar';

const { Content } = Layout;

const ManagerStaffList = () => {
  const [staff, setStaff] = useState([]);
  const [editingStaff, setEditingStaff] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [deleteModalVisible, setDeleteModalVisible] = useState(false); 
  const [deletingStaffId, setDeletingStaffId] = useState(null); 
  const history = useNavigate();

  useEffect(() => {
    const fetchStaff = async () => {
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
      }
    };
    fetchStaff();
  }, []);

  const showEditModal = (record) => {
    setEditingStaff(record);
    form.setFieldsValue(record);
    setIsModalVisible(true);
  };

  const handleEdit = async () => {
    const values = await form.validateFields();
    const updatedStaff = {
      id: editingStaff.id,
      name: values.name,
      phone: values.phone,
      mail: values.mail,
      birthday: values.birthday,
      status: editingStaff.status
    };
    const token = localStorage.getItem("token");
    try {
      await axios.put('http://localhost:8080/api/v1/manager/editWorker', updatedStaff, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setStaff(staff.map(item => (item.id === updatedStaff.id ? updatedStaff : item)));
      setIsModalVisible(false);
      setEditingStaff(null);
    } catch (error) {
      console.error('There was an error updating the staff data!', error);
    }
  };

  const handleDentistList = (id) => {
    history(`/manager/dentist/${id}`);
  };

  const handleDelete = async () => {
    const token = localStorage.getItem("token");
    try {
      await axios.delete(`http://localhost:8080/api/v1/manager/delete-user/${deletingStaffId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success("Staff member deleted successfully");
      
      setDeleteModalVisible(false); 
      window.location.reload();
    } catch (error) {
      console.error('There was an error deleting the staff member!', error);
      message.error(error.response?.data || "An error occurred");
    }
  };

  const showDeleteConfirmation = (id) => {
    setDeletingStaffId(id); // Set the staff id to be deleted
    setDeleteModalVisible(true); // Show the delete confirmation modal
  };

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
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
    },
    {
      title: 'Clinic Name',
      dataIndex: 'clinicName',
      key: 'clinicName',
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (text, record) => (
        <>
          <Button onClick={() => showEditModal(record)}>Edit</Button>
          <Button onClick={() => handleDentistList(record.id)} style={{ marginLeft: 8 }}>Dentist List</Button>
          <Button onClick={() => showDeleteConfirmation(record.id)} type="danger" style={{ marginLeft: 8 }}>Delete</Button>
        </>
      )
    }
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <ManagerSidebar />
    <Layout style={{ padding: '24px 24px' }}>
      <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
        <h2>Staff List</h2>
        <Table columns={columns} dataSource={staff} rowKey="id" />
      </Content>
    </Layout>
      <Modal
        title="Confirm Delete"
        open={deleteModalVisible}
        onOk={handleDelete}
        onCancel={() => setDeleteModalVisible(false)}
        okText="Delete"
        cancelText="Cancel"
      >
        <p>Are you sure you want to delete this staff member?</p>
      </Modal>
      {/* Edit Staff Modal */}
      <Modal
        title="Edit Staff"
        open={isModalVisible}
        onOk={handleEdit}
        onCancel={() => setIsModalVisible(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter the name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone" rules={[{ required: true, message: 'Please enter the phone number' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="mail" label="Email" rules={[{ required: true, message: 'Please enter the email' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="birthday" label="Birthday" rules={[{ required: true, message: 'Please enter the birthday' }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  );
};

export default ManagerStaffList;
