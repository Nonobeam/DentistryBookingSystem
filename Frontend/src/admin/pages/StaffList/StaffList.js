import React, { useEffect, useState } from 'react';
import { Card, Table, Spin } from 'antd';
import { staffListServices } from '../../services/StaffSever/StaffSever';
import { Action } from './components/Action/Action';

const AppointmentHistory = () => {
  const [apiData, setApiData] = useState([]);
  const [loading, setLoading] = useState(true); // State for loading indicator

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
      render: (status) => (
        <span>{status === 1 ? 'Active' : 'Inactive'}</span>
      ),
    },
    {
      title: 'Action',
      dataIndex: 'x',
      key: 'x',
      render: (_, record) => <Action record={record} />,
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
        const response = await staffListServices.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false); // Turn off loading indicator regardless of success or failure
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <Card title="Staff List" style={cardStyle}>
        {loading ? ( // Show Spin component when loading is true
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '300px' }}>
            <Spin size="large" />
          </div>
        ) : (
          <Table
            dataSource={apiData}
            columns={columns}
            pagination={false}
            bordered
            size="small"
            style={{ backgroundColor: 'white' }} // Background color for the table
          />
        )}
      </Card>
    </div>
  );
};

export default AppointmentHistory;
