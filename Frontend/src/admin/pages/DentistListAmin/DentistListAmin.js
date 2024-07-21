import React, { useEffect, useState } from 'react';
import { Card, Table, Spin } from 'antd';
import { DentistServices } from '../../services/DentistServer/DentistSever';
import { Action } from './components/Action/Action';

const AppointmentHistory = () => {
  const [apiData, setApiData] = useState([]);
  const [loading, setLoading] = useState(true); // Thêm state loading để quản lý hiệu ứng loading

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
        setLoading(true); // Đặt loading thành true khi bắt đầu fetch dữ liệu
        const response = await DentistServices.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false); // Đặt loading thành false sau khi fetch dữ liệu xong (bao gồm cả lỗi)
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <Card title='Dentist List' style={cardStyle}>
        {loading ? (
          <div style={{ textAlign: 'center' }}>
            <Spin size="large" />
          </div>
        ) : (
          <Table
            dataSource={apiData}
            columns={columns}
            pagination={false}
            bordered
            size='small'
            style={{ backgroundColor: 'white' }} // Background color for the table
          />
        )}
      </Card>
    </div>
  );
};

export default AppointmentHistory;
