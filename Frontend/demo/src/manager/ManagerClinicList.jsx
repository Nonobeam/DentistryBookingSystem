import React, { useState, useEffect } from 'react';
import { Table, Layout } from 'antd';
import axios from 'axios';

const { Content } = Layout;

const ManagerClinicList = () => {
  const [clinics, setClinics] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/api/v1/manager/all-clinic')
      .then(response => {
        setClinics(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the clinic data!', error);
      });
  }, []);

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
      title: 'Slot Duration',
      dataIndex: 'slotDuration',
      key: 'slotDuration',
      render: slot => `${slot.hour}h ${slot.minute}m`,
    },
    {
      title: 'Open Time',
      dataIndex: 'openTime',
      key: 'openTime',
      render: time => `${time.hour}:${time.minute}:${time.second}`,
    },
    {
      title: 'Close Time',
      dataIndex: 'closeTime',
      key: 'closeTime',
      render: time => `${time.hour}:${time.minute}:${time.second}`,
    },
    {
      title: 'Break Start Time',
      dataIndex: 'breakStartTime',
      key: 'breakStartTime',
      render: time => `${time.hour}:${time.minute}:${time.second}`,
    },
    {
      title: 'Break End Time',
      dataIndex: 'breakEndTime',
      key: 'breakEndTime',
      render: time => `${time.hour}:${time.minute}:${time.second}`,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
    },
  ];

  return (
    <Layout style={{ padding: '24px 24px' }}>
      <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
        <h2>Clinic List</h2>
        <Table columns={columns} dataSource={clinics} rowKey="id" />
      </Content>
    </Layout>
  );
};

export default ManagerClinicList;
