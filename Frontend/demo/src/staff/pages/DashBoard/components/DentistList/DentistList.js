import React, { useEffect, useState } from 'react';
import { Flex } from 'antd';
import { TableList } from './components/Table/TableList';
import { FeatureAction } from './components/FeatureAction/FeatureAction';
import { Action } from './components/Action/Action';
import { DentistServices } from '../../../../services/DentistServices/DentistServices';

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

export const DentistList = () => {
  const [apiData, setApiData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await DentistServices.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <h1>DashBoard Dentist</h1>
      <Flex>
        <FeatureAction />
      </Flex>
      <Flex>
        <TableList dataSource={apiData} columns={columns} />
      </Flex>
    </div>
  );
};
