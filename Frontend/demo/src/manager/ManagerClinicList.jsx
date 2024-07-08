import React, { useState, useEffect } from 'react';
import { Table, Layout, Modal, Form, Input, TimePicker, Button, Switch, Dropdown, Menu, message, Select, DatePicker } from 'antd';
import axios from 'axios';
import moment from 'moment';
import ManagerSidebar from './ManagerSidebar';
import { DownOutlined } from '@ant-design/icons';

const { Content } = Layout;
const { Option } = Select;

const ManagerClinicList = () => {
  const [clinics, setClinics] = useState([]);
  const [editingClinic, setEditingClinic] = useState(null);
  const [isEditModalVisible, setIsEditModalVisible] = useState(false);
  const [isAddStaffModalVisible, setIsAddStaffModalVisible] = useState(false);
  const [isAddDentistModalVisible, setIsAddDentistModalVisible] = useState(false);
  const [isSetStaffModalVisible, setIsSetStaffModalVisible] = useState(false);
  const [staffList, setStaffList] = useState([]);
  const [dentistList, setDentistList] = useState([]);
  const [selectedClinic, setSelectedClinic] = useState(null);
  const [form] = Form.useForm();
  const [staffForm] = Form.useForm();
  const [dentistForm] = Form.useForm();
  const [setStaffForm] = Form.useForm();

  useEffect(() => {
    fetchClinics();
  }, []);

  const fetchClinics = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get('http://localhost:8080/api/v1/manager/all-clinic', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setClinics(response.data);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const formatTime = (timeString) => {
    const [hours, minutes] = timeString.split(':');
    return `${hours}:${minutes}`;
  };

  const formatSlot = (timeString) => {
    const [hours, minutes, seconds] = timeString.split(':');
    return `${minutes}:${seconds}`;
  };

  const fetchAllStaff = async (clinic) => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`http://localhost:8080/api/v1/manager/set-staff/${clinic.id}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setStaffList(response.data.staffList);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const handleEdit = (clinic) => {
    setEditingClinic(clinic);
    form.setFieldsValue({
      ...clinic,
      slotDuration: moment(clinic.slotDuration, 'HH:mm:ss'),
      openTime: moment(clinic.openTime, 'HH:mm:ss'),
      closeTime: moment(clinic.closeTime, 'HH:mm:ss'),
      breakStartTime: moment(clinic.breakStartTime, 'HH:mm:ss'),
      breakEndTime: moment(clinic.breakEndTime, 'HH:mm:ss'),
      status: clinic.status === 1,
    });
    setIsEditModalVisible(true);
  };

  const handleCancel = () => {
    setIsEditModalVisible(false);
    setIsAddStaffModalVisible(false);
    setIsAddDentistModalVisible(false);
    setIsSetStaffModalVisible(false);
    setEditingClinic(null);
    setSelectedClinic(null);
    form.resetFields();
    staffForm.resetFields();
    dentistForm.resetFields();
    setStaffForm.resetFields();
  };

  const handleSave = async () => {
    try {
      const values = form.getFieldsValue();
      const token = localStorage.getItem("token");

      const payload = {
        id: editingClinic.id,
        phone: values.phone,
        name: values.name,
        address: values.address,
        slotDuration: values.slotDuration.format('HH:mm:ss'),
        openTime: values.openTime.format('HH:mm:ss'),
        closeTime: values.closeTime.format('HH:mm:ss'),
        breakStartTime: values.breakStartTime.format('HH:mm:ss')  ,
        breakEndTime: values.breakEndTime.format('HH:mm:ss'),
        status: values.status ? 1 : 0
       };


      console.log(payload);
      await axios.put(`http://localhost:8080/api/v1/manager/editClinic`, payload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      fetchClinics();
      handleCancel();
      message.success('Clinic updated successfully');
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const handleAddStaff = (clinic) => {
    setSelectedClinic(clinic);
    setIsAddStaffModalVisible(true);
  };

  const handleSaveStaff = async () => {
    try {
      const values = staffForm.getFieldsValue();
      const token = localStorage.getItem("token");

      const payload = {
        name: values.name,
        phone: values.phone,
        mail: values.mail,
        password: values.password,
        birthday: values.birthday.format('YYYY-MM-DD')
      };

      await axios.post(`http://localhost:8080/api/v1/manager/register/staff?clinicId=${selectedClinic.id}`, payload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      fetchClinics();
      handleCancel();
      message.success('Staff added successfully');
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const handleAddDentist = async (clinic) => {
    setSelectedClinic(clinic);
    setIsAddDentistModalVisible(true);
    await fetchAllStaff(clinic);
  };

  const handleSaveDentist = async () => {
    try {
      const values = dentistForm.getFieldsValue();
      const token = localStorage.getItem("token");

      const payload = {
        name: values.name,
        phone: values.phone,
        mail: values.mail,
        password: values.password,
        birthday: values.birthday.format('YYYY-MM-DD')
      };

      await axios.post(`http://localhost:8080/api/v1/manager/register/dentist?clinicId=${selectedClinic.id}&staffId=${values.staffId}`, payload, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      fetchClinics();
      handleCancel();
      message.success('Dentist added successfully');
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const handleSetStaff = async (clinic) => {
    setSelectedClinic(clinic);
    setIsSetStaffModalVisible(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`http://localhost:8080/api/v1/manager/set-staff/${clinic.id}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setStaffList(response.data.staffList);
      setDentistList(response.data.dentistList);
      message.success('Staff and dentist fetched successfully');
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const handleSaveSetStaff = async () => {
    try {
      const values = setStaffForm.getFieldsValue();
      const token = localStorage.getItem("token");

      await axios.put(`http://localhost:8080/api/v1/manager/set-staff/${values.staffId}/${values.dentistId}`, {}, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      fetchClinics();
      handleCancel();
      message.success('Staff and dentist set successfully');
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const menu = (clinic) => (
    <Menu>
      <Menu.Item key="0" onClick={() => handleEdit(clinic)}>
        Edit 
      </Menu.Item>
      <Menu.Item key="1" onClick={() => handleAddStaff(clinic)}>
        Add Staff
      </Menu.Item>
      <Menu.Item key="2" onClick={() => handleAddDentist(clinic)}>
        Add Dentist
      </Menu.Item>
      <Menu.Item key="3" onClick={() => handleSetStaff(clinic)}>
        Set Staff
      </Menu.Item>
    </Menu>
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
      title: 'Address',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: 'Slot',
      dataIndex: 'slotDuration',
      key: 'slotDuration',
      render: slot => formatSlot(slot),
    },
    {
      title: 'Open',
      dataIndex: 'openTime',
      key: 'openTime',
      render: time => formatTime(time),
    },
    {
      title: 'Close',
      dataIndex: 'closeTime',
      key: 'closeTime',
      render: time => formatTime(time),
    },
    {
      title: 'Break Start',
      dataIndex: 'breakStartTime',
      key: 'breakStartTime',
      render: time => formatTime(time),
    },
    {
      title: 'Break End',
      dataIndex: 'breakEndTime',
      key: 'breakEndTime',
      render: time => formatTime(time),
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: status => (status === 1 ? 'Active' : 'Inactive'),
    },
    {
      title: '',
      key: 'action',
      render: (text, record) => (
        <Dropdown overlay={menu(record)}>
          <Button type="link">
          <DownOutlined />
          </Button>
        </Dropdown>
      ),
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <ManagerSidebar />
      <Layout style={{ padding: '24px 24px' }} className="site-layout">
        <Content style={{ margin: '24px 16px 0', minHeight: 280 }}>
          <h2>Clinic List</h2>
          <Table columns={columns} dataSource={clinics} rowKey="id" />
        </Content>
      </Layout>

      {/* Edit Clinic Modal */}
      <Modal
        title="Edit Clinic"
        open={isEditModalVisible}
        onCancel={handleCancel}
        onOk={handleSave}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="Name">
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone">
            <Input />
          </Form.Item>
          <Form.Item name="address" label="Address">
            <Input />
          </Form.Item>
          <Form.Item name="slotDuration" label="Slot Duration">
            <TimePicker format="HH:mm:ss" />
          </Form.Item>
          <Form.Item name="openTime" label="Open Time">
            <TimePicker format="HH:mm:ss" />
          </Form.Item>
          <Form.Item name="closeTime" label="Close Time">
            <TimePicker format="HH:mm:ss" />
          </Form.Item>
          <Form.Item name="breakStartTime" label="Break Start Time">
            <TimePicker format="HH:mm:ss" />
          </Form.Item>
          <Form.Item name="breakEndTime" label="Break End Time">
            <TimePicker format="HH:mm:ss" />
          </Form.Item>
          <Form.Item name="status" label="Status" valuePropName="checked">
            <Switch checkedChildren="Active" unCheckedChildren="Inactive" />
          </Form.Item>
        </Form>
      </Modal>

      {/* Add Staff Modal */}
      <Modal
        title="Add Staff"
        open={isAddStaffModalVisible}
        onCancel={handleCancel}
        onOk={handleSaveStaff}
      >
        <Form form={staffForm} layout="vertical">
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter the staff name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone" rules={[{ required: true, message: 'Please enter the phone number' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="mail" label="Email" rules={[{ required: true, message: 'Please enter the email' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="password" label="Password" rules={[{ required: true, message: 'Please enter the password' }]}>
            <Input.Password />
          </Form.Item>
          <Form.Item name="birthday" label="Birthday" rules={[{ required: true, message: 'Please select the birthday' }]}>
            <DatePicker />
          </Form.Item>
        </Form>
      </Modal>

      {/* Add Dentist Modal */}
      <Modal
        title="Add Dentist"
        open={isAddDentistModalVisible}
        onCancel={handleCancel}
        onOk={handleSaveDentist}
      >
        <Form form={dentistForm} layout="vertical">
          <Form.Item name="staffId" label="Select Staff" rules={[{ required: true, message: 'Please select a staff member' }]}>
            <Select notFoundContent="There are no staff for this Clinic.">
              {staffList.map(staff => (
                <Option key={staff.id} value={staff.id}>{staff.name}</Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="name" label="Name" rules={[{ required: true, message: 'Please enter the dentist name' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="Phone" rules={[{ required: true, message: 'Please enter the phone number' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="mail" label="Email" rules={[{ required: true, message: 'Please enter the email' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="password" label="Password" rules={[{ required: true, message: 'Please enter the password' }]}>
            <Input.Password />
          </Form.Item>
          <Form.Item name="birthday" label="Birthday" rules={[{ required: true, message: 'Please select the birthday' }]}>
            <DatePicker />
          </Form.Item>
        </Form>
      </Modal>

      {/* Set Staff Modal */}
      <Modal
        title="Set Staff"
        open={isSetStaffModalVisible}
        onCancel={handleCancel}
        onOk={handleSaveSetStaff}
      >
        <Form form={setStaffForm} layout="vertical">
          <Form.Item name="staffId" label="Select Staff" rules={[{ required: true, message: 'Please select a staff member' }]}>
            <Select notFoundContent="There are no staff for this clinic.">
              {staffList.map(staff => (
                <Option key={staff.id} value={staff.id}>{staff.name}</Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="dentistId" label="Select Dentist" rules={[{ required: true, message: 'Please select a dentist' }]}>
            <Select notFoundContent="There are no dentist for this clinic.">
              {dentistList.map(dentist => (
                <Option key={dentist.id} value={dentist.id}>{dentist.name}</Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>
    </Layout>
  );
};

export default ManagerClinicList;
