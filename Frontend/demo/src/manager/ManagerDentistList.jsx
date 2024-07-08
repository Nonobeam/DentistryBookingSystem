import React, { useState, useEffect } from 'react';
import { Table, Layout, Button, Modal, Form, Input, Select, message } from 'antd';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import ManagerSidebar from './ManagerSidebar';

const { Content } = Layout;
const { Option } = Select;

const ManagerDentistList = () => {
  const [dentists, setDentists] = useState([]);
  const [staff, setStaff] = useState([]);
  const [editingDentist, setEditingDentist] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [deleteModalVisible, setDeleteModalVisible] = useState(false); // State for delete confirmation modal
  const [deletingDentistId, setDeletingDentistId] = useState(null); // State to store dentist id being deleted
  const history = useNavigate();
  const { id } = useParams();

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

  useEffect(() => {
    if (id) {
      handleStaffFilter(id); // Filter by staff ID if present in the URL
      form.setFieldsValue({ staffId: id });
    } else {
      fetchAllDentists();
    }
  }, [id]); // Run useEffect whenever the ID changes

  const fetchAllDentists = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await axios.get('http://localhost:8080/api/v1/manager/all-dentist', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setDentists(response.data);
    } catch (error) {
      console.error('There was an error fetching the dentist data!', error);
    }
  };

  const showEditModal = (record) => {
    setEditingDentist(record);
    form.setFieldsValue(record);
    setIsModalVisible(true);
  };

  const handleEdit = async () => {
    const values = await form.validateFields();
    const updatedDentist = {
      id: editingDentist.id,
      name: values.name,
      phone: values.phone,
      mail: values.mail,
      birthday: values.birthday,
      status: editingDentist.status
    };
    const token = localStorage.getItem("token");
    try {
      await axios.put('http://localhost:8080/api/v1/manager/editWorker', updatedDentist, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setDentists(dentists.map(item => (item.id === updatedDentist.id ? updatedDentist : item)));
      setIsModalVisible(false);
      setEditingDentist(null);
    } catch (error) {
      console.error('There was an error updating the dentist data!', error);
    }
  };

  const handleStaffFilter = async (staffId) => {
    history(`/manager/dentist/${staffId}`); // Update the URL with the staff ID
    const token = localStorage.getItem("token");
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/manager/${staffId}/all-dentists`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setDentists(response.data);
    } catch (error) {
      if (error.response && error.response.status === 400) {
        setDentists([]);
      } else {
        console.error('There was an error fetching the filtered dentist data!', error);
      }
    }
  };

  const handleDelete = async () => {
    const token = localStorage.getItem("token");
    try {
      await axios.delete(`http://localhost:8080/api/v1/manager/delete-user/${deletingDentistId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success("Dentist deleted successfully");
      fetchAllDentists(); // Refresh the list after deletion
      setDeleteModalVisible(false); // Close the delete confirmation modal
    } catch (error) {
      console.error('There was an error deleting the dentist!', error);
      message.error(error.response?.data || "An error occurred");
    }
  };

  const showDeleteConfirmation = (id) => {
    setDeletingDentistId(id); // Set the dentist id to be deleted
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
          <Button onClick={() => showDeleteConfirmation(record.id)} style={{ marginLeft: 8 }} type="danger">
            Delete
          </Button>
        </>
      )
    }
  ];

  return (
    <>
      <ManagerSidebar />
    <Layout style={{ padding: '24px 24px' }}>
      <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
        <h2>Dentist List</h2>
          <Select
            placeholder="Select Staff"
            style={{ width: 200, marginBottom: 16 }}
            onChange={handleStaffFilter}
            value={id || undefined} // Set the selected value if there is an ID in the URL
          >
            {staff.map(staffMember => (
              <Option key={staffMember.id} value={staffMember.id}>
                {staffMember.name}
              </Option>
            ))}
          </Select>
        <Table columns={columns} dataSource={dentists} rowKey="id" />
      </Content>
    </Layout>
      <Modal
        title="Edit Dentist"
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
      {/* Delete Confirmation Modal */}
      <Modal
        title="Confirm Delete"
        open={deleteModalVisible}
        onOk={handleDelete}
        onCancel={() => setDeleteModalVisible(false)}
        okText="Delete"
        cancelText="Cancel"
      >
        <p>Are you sure you want to delete this dentist?</p>
      </Modal>
    </>
  );
};

export default ManagerDentistList;
