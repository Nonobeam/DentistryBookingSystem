import { Flex } from 'antd';
import React, { useEffect, useState } from 'react';
import { TableList } from './components/Table/TableList';
import { FeatureAction } from './components/FeatureAction/FeatureAction';
import axios from 'axios';

import { Action } from './components/Action/Action';
const apiEndPoint = 'https://65459271fe036a2fa95473f5.mockapi.io/api/Student';

const columns = [
  {
    title: 'Họ Tên',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Tuổi',
    dataIndex: 'dateofbirth',
    key: 'dateofbirth',
  },
  {
    title: 'Class',
    dataIndex: 'class',
    key: 'class',
  },
  {
    title: 'Action',
    dataIndex: '',
    key: 'x',
    render: (_, record) => <Action record={record} />,
    
  },
];

export const DentistList = () => {
  const [apiData, setApiData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(apiEndPoint);
        setApiData(response.data);
      } catch (error) {
        console.error('Error fetching data: ', error);
      }
    };
    fetchData();
  }, []);
  return (
    <div>
      <h1>DashBoard</h1>
      <Flex> <FeatureAction /></Flex>
      <Flex>
        <TableList dataSource={apiData} columns={columns} />
      </Flex>
    </div>
  );
};
