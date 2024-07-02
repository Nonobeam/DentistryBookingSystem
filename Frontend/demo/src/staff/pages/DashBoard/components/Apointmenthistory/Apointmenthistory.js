import React, { useEffect, useState } from 'react';
import { Card, Table, Dropdown, Menu, Button, Modal, message } from 'antd';
import { AppointmentHistoryServices } from '../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';

const AppointmentHistory = () => {
  const [apiData, setApiData] = useState([]);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      // const response = await AppointmentHistoryServices.getAll(date, search);
      const response = await AppointmentHistoryServices.getAll();
      setApiData(response);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  

  const handleUpdateStatus = (record, status) => {
    // Implement your logic to update treatment status here
    // For example, you might show a confirmation dialog and then update the status
    message.success(
      `Updated status to ${status} for treatment ID ${record.key}`
    );
    // Assuming you have updated the status, you can then update the table data
    fetchData(); // Refresh data after update
  };

  const handleDelete = (record) => {
    // Implement your logic to delete the appointment here
    // For example, show a confirmation dialog before deletion
    Modal.confirm({
      title: 'Confirm Delete',
      content: `Are you sure you want to delete treatment ID ${record.key}?`,
      okText: 'Delete',
      okType: 'danger',
      cancelText: 'Cancel',
      onOk() {
        // Perform deletion
        message.success(`Deleted treatment ID ${record.key}`);
        // Assuming you have deleted the record, update the table data
        fetchData(); // Refresh data after deletion
      },
      onCancel() {
        console.log('Cancel');
      },
    });
  };

  const handleMenuClick = (record, e) => {
    const action = e.key;
    if (action === '1' || action === '2') {
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
    treatment: {
      backgroundColor: '#f0f0f0',
      padding: '5px 10px',
      borderRadius: '5px',
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
