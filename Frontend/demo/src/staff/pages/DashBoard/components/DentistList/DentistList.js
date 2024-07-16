import React, { useEffect, useState } from 'react';
import { Flex, Spin } from 'antd'; // Spin là component của Ant Design để hiển thị loading
import { TableList } from './components/Table/TableList';
import { Action } from './components/Action/Action';
import { DentistServices } from '../../../../services/DentistServices/DentistServices';
import useSWR from 'swr';

const columns = [
  {
    title: 'Full Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Birthday',
    dataIndex: 'birthday',
    key: 'birthday',
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
    title: 'Action',
    key: 'action',
    render: (_, record) => <Action record={record} />,
  },
];

const fetchData = async () => {
  const response = await DentistServices.getAll();
  return response;
};

export const DentistList = () => {
  const { data, error, isLoading } = useSWR('dentistServices', fetchData);
  
  return (
    <div>
      <h1>Dentist List</h1>
      <Flex justify='center' align='middle' style={{ minHeight: '200px' }}>
        {isLoading ? ( // Kiểm tra nếu đang loading thì hiển thị Spin (biểu tượng loading)
          <Spin size='large' />
        ) : (
          <TableList dataSource={data} columns={columns} />
        )}
      </Flex>
    </div>
  );
};
