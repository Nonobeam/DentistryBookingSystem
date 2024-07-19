import React, { useEffect, useState } from 'react';
import { Card, Table } from 'antd';
import { CustomerServices } from '../../services/CustomerServer/CustomerServer';
import { Action } from './components/Action/Action';

export const CustomerList = () => {
  const [apiData, setApiData] = useState([]);

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Number',
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
      render: (status) => <span>{status === 1 ? 'Active' : 'Inactive'}</span>,
    },
    {
      title: 'Action',
      dataIndex: 'x',
      key: 'x',
      render: (_, record) => (
        <Action data={apiData} setApiData={setApiData} record={record} />
      ),
    },
  ];

  const cardStyle = {
    marginBottom: '20px',
    border: '1px solid #e8e8e8',
    borderRadius: '5px',
    padding: '10px',
    boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await CustomerServices.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <Card title='Customer List' style={cardStyle}>
        <Table
          dataSource={apiData}
          columns={columns}
          pagination={false}
          bordered
          size='small'
          style={{ backgroundColor: 'white' }} // Background color for the table
        />
      </Card>
    </div>
  );
};
