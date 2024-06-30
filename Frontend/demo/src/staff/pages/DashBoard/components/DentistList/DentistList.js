import { Flex } from 'antd';
import React, { useEffect, useState } from 'react';
import { TableList } from './components/Table/TableList';
import { FeatureAction } from './components/FeatureAction/FeatureAction';
import { Action } from './components/Action/Action';
import { DentistServices } from '../../../../services/DentistServices/DentistServices';

const columns = [
  {
    title: 'Full Name',
    dataIndex: `name`,
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
      <h1>DashBoardDentist</h1>
      <Flex>
        {' '}
        <FeatureAction />
      </Flex>
      <Flex>
        <TableList dataSource={apiData} columns={columns} />
      </Flex>
    </div>
  );
};
