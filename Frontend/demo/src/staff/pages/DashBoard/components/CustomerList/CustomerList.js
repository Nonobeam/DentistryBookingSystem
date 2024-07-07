import { Flex } from 'antd';
import React, { useEffect, useState } from 'react';

import { Action } from './components/Action/Action';
import { CustomerServicess } from '../../../../services/CustomerServicess/CustomerServicess';
import { TableList } from './components/TableList/TableList';


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

export const CustomerListDash = () => {
  const [apiData, setApiData] = useState([]);

    useEffect(() => {
      const fetchData = async () => {
        try {
          const response = await CustomerServicess.getAll();
          
          setApiData(response);
        } catch (error) {
          console.error('Error fetching data:', error);
        }
      };
      fetchData();
    }, []);
  return (
    <div>
      <h1>CustomerList</h1>
      <Flex>
        {' '}
        
      </Flex>
      <Flex>
        <TableList dataSource={apiData} columns={columns} />
      </Flex>
    </div>
  );
};
