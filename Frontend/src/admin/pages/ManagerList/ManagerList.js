import React, { useEffect, useState } from 'react';
import { Card, Table, Spin } from 'antd';
import { ManagerServices } from '../../services/ManagerSever/ManagerSever';
import { Action } from './components/Action/Action';

const AppointmentHistory = () => {
  const [apiData, setApiData] = useState([]);
  const [loading, setLoading] = useState(true);

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
    position: 'relative', // Ensure relative positioning for spinner
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true); // Set loading to true before fetching data
        const response = await ManagerServices.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false); // Set loading to false after fetching data (including error case)
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <Card title="Manager List" style={cardStyle}>
        {loading && (
          <Spin
            spinning={loading}
            size="large"
            style={{
              position: 'absolute',
              left: '50%',
              top: '50%',
              transform: 'translate(-50%, -50%)',
            }}
          />
        )}
        <Table
          dataSource={apiData}
          columns={columns}
          pagination={false}
          bordered
          size="small"
          style={{ backgroundColor: 'white' }} // Background color for the table
        />
      </Card>
    </div>
  );
};

export default AppointmentHistory;
