// components/AppointmentHistory.js

import React, { useEffect, useState } from 'react';
import { Card, Table, Dropdown, Button, Modal, message, Menu } from 'antd';
import { AppointmentHistoryServices } from '../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';

const AppointmentHistory = () => {
  const [apiData, setApiData] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const response = await AppointmentHistoryServices.getAll();
      setApiData(response);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const handleUpdateStatus = async (record, status) => {
    try {
      const response = await AppointmentHistoryServices.patchAppointment({
        appointmentId: record.appointmentId,
        status: status,
      });
      message.success(
        `Updated status to ${status} for treatment ID ${record.appointmentId}`
      );
      fetchData(); // Refresh data after update
      console.log('Updated status:', status);
    } catch (error) {
      console.error('Error updating status:', error);
    }
  };

  const handleDelete = async (record) => {
    Modal.confirm({
      title: 'Confirm Delete',
      content: `Are you sure you want to delete treatment ID ${record.appointmentId}?`,
      okText: 'Delete',
      okType: 'danger',
      cancelText: 'Cancel',
      async onOk() {
        try {
          const response =
            await AppointmentHistoryServices.deteleateAppointment({
              appointmentId: record.appointmentId,
            });
          message.success(`Deleted treatment ID ${record.appointmentId}`);
          fetchData(); // Refresh data after deletion
        } catch (error) {
          console.error('Error deleting appointment:', error);
        }
      },
      onCancel() {
        console.log('Cancel');
      },
    });
  };

  const handleMenuClick = (record, e) => {
    const action = e.key;
    if (action === '1' || action === '2') {
      console.log(record);
      handleUpdateStatus(record, action);
    } else if (action === 'delete') {
      handleDelete(record);
    }
  };

  const menu = (record) => (
    <Menu onClick={(e) => handleMenuClick(record, e)}>
      <Menu.Item key='1'>Update to 1</Menu.Item>
      <Menu.Item key='2'>Update to 2</Menu.Item>
      <Menu.Item key='delete'>Delete</Menu.Item>
    </Menu>
  );

  const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'User',
      dataIndex: 'user',
      key: 'user',
    },
    {
      title: 'TimeSlot',
      dataIndex: 'timeSlot',
      key: 'timeSlot',
    },
    {
      title: 'Dentist',
      dataIndex: 'dentist',
      key: 'dentist',
    },
    {
      title: 'Services',
      dataIndex: 'services',
      key: 'services',
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (text, record) => (
        <Dropdown overlay={menu(record)}>
          <Button>Actions</Button>
        </Dropdown>
      ),
    },
  ];

  const styles = {
    card: {
      marginBottom: '20px',
    },
  };

  return (
    <div>
      <Card title='Appointment History' style={styles.card}>
        <Table
          dataSource={apiData}
          columns={columns}
          pagination={false}
          bordered
          size='small'
        />
      </Card>
    </div>
  );
};

export default AppointmentHistory;
