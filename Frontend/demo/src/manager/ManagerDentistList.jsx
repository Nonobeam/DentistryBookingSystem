import React, { useState, useEffect } from 'react';
import { Table, Layout, Button, Modal, Form, Input, Select, Dropdown, Menu, message, Spin, DatePicker } from 'antd';
import { DownOutlined } from '@ant-design/icons';
import axios from 'axios';
import dayjs from 'dayjs';
import { useNavigate, useParams } from 'react-router-dom';
import ManagerSidebar from './ManagerSidebar';

const { Header, Content } = Layout;
const { Option } = Select;

const ManagerDentistList = () => {
  const [dentists, setDentists] = useState([]);
  const [staff, setStaff] = useState([]);
  const [editingDentist, setEditingDentist] = useState(null);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [deleteModalVisible, setDeleteModalVisible] = useState(false); 
  const [deletingDentistId, setDeletingDentistId] = useState(null); 
  const [loading, setLoading] = useState(false);
  const [modalLoading, setModalLoading] = useState(false);
  const history = useNavigate();
  const { id } = useParams();

  const fetchAllDentists = async () => {
    const token = localStorage.getItem("token");
    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/v1/manager/all-dentist', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setDentists(response.data);
    } catch (error) {
      console.error('There was an error fetching the dentist data!', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (id) {
      handleStaffFilter(id);
      form.setFieldsValue({ staffId: id });
    } else {
      fetchAllDentists();
    }
  }, [id]);

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
    setEditingDentist(record);
    form.setFieldsValue({
      ...record,
      birthday: dayjs(record.birthday)
    });
    setIsModalVisible(true);
  };

  const handleEdit = async () => {
    const values = await form.validateFields();
    const updatedDentist = {
      id: editingDentist.id,
      name: values.name,
      phone: values.phone,
      mail: values.mail,
      birthday: values.birthday.format('YYYY-MM-DD'),
      status: editingDentist.status
    };
    const token = localStorage.getItem("token");
    setModalLoading(true);
    try {
      await axios.put('http://localhost:8080/api/v1/manager/editWorker', updatedDentist, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setIsModalVisible(false);
      setEditingDentist(null);
      fetchAllDentists();
      message.success("Dentist updated successfully");
    } catch (error) {
      message.error("Failed to update dentist");
    } finally {
      setModalLoading(false);
    }
  };

  const handleStaffFilter = async (staffId) => {
    history(`/manager/dentist/${staffId}`);
    const token = localStorage.getItem("token");
    setLoading(true);
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
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    const token = localStorage.getItem("token");
    setModalLoading(true);
    try {
      await axios.delete(`http://localhost:8080/api/v1/manager/delete-user/${deletingDentistId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success("Dentist deleted successfully");
      fetchAllDentists();
      setDeleteModalVisible(false);
    } catch (error) {
      console.error('There was an error deleting the dentist!', error);
      message.error(error.response?.data || "An error occurred");
    } finally {
      setModalLoading(false);
    }
  };

  const showDeleteConfirmation = (id) => {
    setDeletingDentistId(id);
    setDeleteModalVisible(true);
  };

  const actionsMenu = (record) => (
    <Menu
      items={[
        {
          key: 'edit',
          label: 'Edit',
          onClick: () => showEditModal(record)
        },
        {
          key: 'delete',
          label: 'Delete',
          onClick: () => showDeleteConfirmation(record.id),
          danger: true
        }
      ]}
    />
  );

  const actionsDropdown = (record) => (
    <Dropdown overlay={actionsMenu(record)} placement="bottomLeft" trigger={['click']}>
      <Button>
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
      render: (date) => dayjs(date).format('DD-MM-YYYY'),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: status => (status === 1 ? 'Active' : 'Inactive'),
    },
    {
      title: 'Clinic',
      dataIndex: 'clinicName',
      key: 'clinicName',
    },
    {
      title: '',
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
          <h1>Dentist List</h1>
          <Spin spinning={loading}>
            <Select
              placeholder="Select Staff"
              style={{ width: 200, marginBottom: 16 }}
              onChange={handleStaffFilter}
              value={id || undefined}
            >
              {staff.map(staffMember => (
                <Option key={staffMember.id} value={staffMember.id}>
                  {staffMember.name}
                </Option>
              ))}
            </Select>
            <Table columns={columns} dataSource={dentists} rowKey="id" />
          </Spin>
        </Content>
      </Layout>
      <Modal
        title="Edit Dentist"
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
      <Modal
        title="Confirm Delete"
        open={deleteModalVisible}
        onOk={handleDelete}
        onCancel={() => setDeleteModalVisible(false)}
        okText="Delete"
        cancelText="Cancel"
        confirmLoading={modalLoading}
      >
        <p>Are you sure you want to delete this dentist?</p>
      </Modal>
    </Layout>
  );
};

export default ManagerDentistList;
