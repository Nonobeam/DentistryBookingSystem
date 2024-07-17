import { Flex, Spin } from 'antd';
import React from 'react';

import { Action } from './components/Action/Action';
import { TableList } from './components/TableList/TableList';
import useSWR from 'swr';
import { CustomerServicess } from '../../../../services/CustomerServicess/CustomerServicess';


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



  const fetchData = async () => {
    const response = await CustomerServicess.getAll();
    return response;
  };
  
  export const CustomerListDash = () => {
    const { data, error, isLoading } = useSWR('Customer', fetchData);

  return (
    <div>
      <h1>Customer List</h1>
      <Flex justify="center" align="middle" style={{ minHeight: '200px' }}>
        {isLoading ? ( // Kiểm tra nếu đang loading thì hiển thị Spin (biểu tượng loading)
          <Spin size="large" />
        ) : (
          <TableList dataSource={data} columns={columns} />
        )}
      </Flex>
    </div>
  );
};
