import React, { useState, useEffect } from 'react';
import { Table, Layout } from 'antd';
import axios from 'axios';

const { Content } = Layout;

const ManagerDentistList = () => {
  const [dentists, setDentists] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/api/v1/manager/all-dentist')
      .then(response => {
        setDentists(response.data);
      })
      .catch(error => {
        console.error('There was an error fetching the dentist data!', error);
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
  ];

  return (
    <Layout style={{ padding: '24px 24px' }}>
      <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
        <h2>Dentist List</h2>
        <Table columns={columns} dataSource={dentists} rowKey="id" />
      </Content>
    </Layout>
  );
};

export default ManagerDentistList;
