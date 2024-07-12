import { Flex, Spin } from 'antd';
import React, { useEffect, useState } from 'react';

import { Action } from './components/Action/Action';
import { CustomerServicess } from '../../../../services/CustomerServicess/CustomerServicess';
import { TableList } from './components/TableList/TableList';

const columns = [
  {
    title: 'Full Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Birthday',
    dataIndex: 'birthday',
    key: 'dateofbirth',
  },
  {
    title: 'Number',
    dataIndex: 'phone',
    key: 'class',
  },
  {
    title: 'Email',
    dataIndex: 'mail',
    key: 'class',
  },
  {
    title: 'Action',
    dataIndex: 'x',
    key: 'x',
    render: (_, record) => <Action record={record} />,
  },
];

export const CustomerListDash = () => {
  const [apiData, setApiData] = useState([]);
  const [loading, setLoading] = useState(true); // State để theo dõi trạng thái loading

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await CustomerServicess.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false); // Kết thúc fetch data, đánh dấu là không còn loading nữa
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <h1>Customer List</h1>
      <Flex justify="center" align="middle" style={{ minHeight: '200px' }}>
        {loading ? ( // Kiểm tra nếu đang loading thì hiển thị Spin (biểu tượng loading)
          <Spin size="large" />
        ) : (
          <TableList dataSource={apiData} columns={columns} />
        )}
      </Flex>
    </div>
  );
};
