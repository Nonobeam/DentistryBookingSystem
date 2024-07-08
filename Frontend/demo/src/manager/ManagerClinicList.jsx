import React, { useState, useEffect } from 'react';
import { Table, Layout, Modal, Form, Input, TimePicker, Button, Switch, Dropdown, Menu, message, Select, DatePicker, Spin } from 'antd';
import axios from 'axios';
import dayjs from 'dayjs'; 
import ManagerSidebar from './ManagerSidebar';
import { DownOutlined } from '@ant-design/icons';

const { Header, Content } = Layout;
const { Option } = Select;

const ManagerClinicList = () => {
  const [clinics, setClinics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingClinic, setEditingClinic] = useState(null);
  const [isEditModalVisible, setIsEditModalVisible] = useState(false);
  const [isAddStaffModalVisible, setIsAddStaffModalVisible] = useState(false);
  const [isAddDentistModalVisible, setIsAddDentistModalVisible] = useState(false);
  const [isSetStaffModalVisible, setIsSetStaffModalVisible] = useState(false);
  const [staffList, setStaffList] = useState([]);
  const [dentistList, setDentistList] = useState([]);
  const [selectedClinic, setSelectedClinic] = useState(null);
  const [isCreateModalVisible, setIsCreateModalVisible] = useState(false);
  const [createForm] = Form.useForm();
  const [form] = Form.useForm();
  const [staffForm] = Form.useForm();
  const [dentistForm] = Form.useForm();
  const [setStaffForm] = Form.useForm();

  useEffect(() => {
    fetchClinics();
  }, []);

  const fetchClinics = async () => {
    setLoading(true);
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
    } finally {
      setLoading(false);
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
    setLoading(true);
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
    } finally {
      setLoading(false);
    }
  };

  const showCreateModal = () => {
    setIsCreateModalVisible(true);
  };
  
  const handleCreateCancel = () => {
    setIsCreateModalVisible(false);
    createForm.resetFields();
  };
  
  const handleCreateClinic = async () => {
    try {
      const values = createForm.getFieldsValue();
      const token = localStorage.getItem("token");
  
      const payload = {
        name: values.name,
        phone: values.phone,
        address: values.address,
        slotDuration: values.slotDuration.format('HH:mm:ss'),
        openTime: values.openTime.format('HH:mm:ss'),
        closeTime: values.closeTime.format('HH:mm:ss'),
        breakStartTime: values.breakStartTime.format('HH:mm:ss'),
        breakEndTime: values.breakEndTime.format('HH:mm:ss'),
        status: values.status ? 1 : 0
      };
  
      await axios.post(`http://localhost:8080/api/v1/manager/create-clinic?name=${payload.name}&phone=${payload.phone}&address=${payload.address}&slotDuration=${payload.slotDuration}&openTime=${payload.openTime}&closeTime=${payload.closeTime}&breakStartTime=${payload.breakStartTime}&breakEndTime=${payload.breakEndTime}&status=${payload.status}`, {}, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
  
      fetchClinics();
      handleCreateCancel();
      message.success('Clinic created successfully');
      fetchClinics();
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const handleEdit = (clinic) => {
    setEditingClinic(clinic);
    form.setFieldsValue({
      ...clinic,
      slotDuration: dayjs(clinic.slotDuration, 'HH:mm:ss'),
      openTime: dayjs(clinic.openTime, 'HH:mm:ss'),
      closeTime: dayjs(clinic.closeTime, 'HH:mm:ss'),
      breakStartTime: dayjs(clinic.breakStartTime, 'HH:mm:ss'),
      breakEndTime: dayjs(clinic.breakEndTime, 'HH:mm:ss'),
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
        breakStartTime: values.breakStartTime.format('HH:mm:ss'),
        breakEndTime: values.breakEndTime.format('HH:mm:ss'),
        status: values.status ? 1 : 0
      };
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
      key: 'actions',
      render: (_, clinic) => (
        <Dropdown overlay={menu(clinic)} trigger={['click']}>
          <Button>
           <DownOutlined />
          </Button>
        </Dropdown>
      ),
    },
  ];

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <ManagerSidebar />

      <Layout style={{ padding: '0px auto' }} className="site-layout">
      <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />

      <Content style={{ margin: '24px 16px', padding: '0 26px'}}>
      <h2>Clinic List</h2>
      <Button type="primary" onClick={showCreateModal} style={{ marginBottom: '16px'}}>
    Create Clinic
      </Button>

        <Spin spinning={loading}>
          <Table columns={columns} dataSource={clinics} rowKey="id" />
        </Spin>

        <Modal title="Create Clinic" open={isCreateModalVisible} onCancel={handleCreateCancel} onOk={handleCreateClinic}>
    <Form form={createForm}>
      <Form.Item label="Name" name="name" rules={[{ required: true, message: 'Please enter clinic name' }]}>
        <Input />
      </Form.Item>
      <Form.Item label="Phone" name="phone" rules={[{ required: true, message: 'Please enter phone number' }]}>
        <Input />
      </Form.Item>
      <Form.Item label="Address" name="address" rules={[{ required: true, message: 'Please enter address' }]}>
        <Input />
      </Form.Item>
      <Form.Item label="Slot Duration" name="slotDuration" rules={[{ required: true, message: 'Please select slot duration' }]}>
        <TimePicker format="HH:mm:ss" />
      </Form.Item>
      <Form.Item label="Open Time" name="openTime" rules={[{ required: true, message: 'Please select open time' }]}>
        <TimePicker format="HH:mm:ss" />
      </Form.Item>
      <Form.Item label="Close Time" name="closeTime" rules={[{ required: true, message: 'Please select close time' }]}>
        <TimePicker format="HH:mm:ss" />
      </Form.Item>
      <Form.Item label="Break Start Time" name="breakStartTime" rules={[{ required: true, message: 'Please select break start time' }]}>
        <TimePicker format="HH:mm:ss" />
      </Form.Item>
      <Form.Item label="Break End Time" name="breakEndTime" rules={[{ required: true, message: 'Please select break end time' }]}>
        <TimePicker format="HH:mm:ss" />
      </Form.Item>
      <Form.Item label="Status" name="status" valuePropName="checked">
        <Switch />
      </Form.Item>
    </Form>
  </Modal>
        
        <Modal title="Edit Clinic" open={isEditModalVisible} onCancel={handleCancel} onOk={handleSave}>
          <Form form={form}>
            <Form.Item label="Name" name="name" rules={[{ required: true, message: 'Please enter clinic name' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Phone" name="phone" rules={[{ required: true, message: 'Please enter phone number' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Address" name="address" rules={[{ required: true, message: 'Please enter address' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Slot Duration" name="slotDuration" rules={[{ required: true, message: 'Please select slot duration' }]}>
              <TimePicker format="mm:ss" />
            </Form.Item>
            <Form.Item label="Open Time" name="openTime" rules={[{ required: true, message: 'Please select open time' }]}>
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item label="Close Time" name="closeTime" rules={[{ required: true, message: 'Please select close time' }]}>
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item label="Break Start Time" name="breakStartTime" rules={[{ required: true, message: 'Please select break start time' }]}>
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item label="Break End Time" name="breakEndTime" rules={[{ required: true, message: 'Please select break end time' }]}>
              <TimePicker format="HH:mm:ss" />
            </Form.Item>
            <Form.Item label="Status" name="status" valuePropName="checked">
              <Switch />
            </Form.Item>
          </Form>
        </Modal>

        <Modal title="Add Staff" open={isAddStaffModalVisible} onCancel={handleCancel} onOk={handleSaveStaff}>
          <Form form={staffForm}>
            <Form.Item label="Name" name="name" rules={[{ required: true, message: 'Please enter staff name' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Phone" name="phone" rules={[{ required: true, message: 'Please enter phone number' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Mail" name="mail" rules={[{ required: true, type: 'email', message: 'Please enter a valid email' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Password" name="password" rules={[{ required: true, message: 'Please enter password' }]}>
              <Input.Password />
            </Form.Item>
            <Form.Item label="Birthday" name="birthday" rules={[{ required: true, message: 'Please select birthday' }]}>
              <DatePicker format="YYYY-MM-DD" />
            </Form.Item>
          </Form>
        </Modal>

        <Modal title="Add Dentist" open={isAddDentistModalVisible} onCancel={handleCancel} onOk={handleSaveDentist}>
          <Form form={dentistForm}>
            <Form.Item label="Name" name="name" rules={[{ required: true, message: 'Please enter dentist name' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Phone" name="phone" rules={[{ required: true, message: 'Please enter phone number' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Mail" name="mail" rules={[{ required: true, type: 'email', message: 'Please enter a valid email' }]}>
              <Input />
            </Form.Item>
            <Form.Item label="Password" name="password" rules={[{ required: true, message: 'Please enter password' }]}>
              <Input.Password />
            </Form.Item>
            <Form.Item label="Birthday" name="birthday" rules={[{ required: true, message: 'Please select birthday' }]}>
              <DatePicker format="YYYY-MM-DD" />
            </Form.Item>
            <Form.Item label="Staff ID" name="staffId" rules={[{ required: true, message: 'Please select a staff' }]}>
              <Select>
                {staffList.map((staff) => (
                  <Option key={staff.id} value={staff.id}>
                    {staff.name}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </Form>
        </Modal>

        <Modal title="Set Staff and Dentist" open={isSetStaffModalVisible} onCancel={handleCancel} onOk={handleSaveSetStaff}>
          <Form form={setStaffForm}>
            <Form.Item label="Staff" name="staffId" rules={[{ required: true, message: 'Please select staff' }]}>
              <Select>
                {staffList.map((staff) => (
                  <Option key={staff.id} value={staff.id}>
                    {staff.name}
                  </Option>
                ))}
              </Select>
            </Form.Item>
            <Form.Item label="Dentist" name="dentistId" rules={[{ required: true, message: 'Please select dentist' }]}>
              <Select>
                {dentistList.map((dentist) => (
                  <Option key={dentist.id} value={dentist.id}>
                    {dentist.name}
                  </Option>
                ))}
              </Select>
            </Form.Item>
          </Form>
        </Modal>
      </Content>
      </Layout>
    </Layout>
  );
};

export default ManagerClinicList;
