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
    sorter: true,
  },
  {
    title: 'Birthday',
    dataIndex: 'birthday',
    key: 'birthday',
    sorter: true,
  },
  {
    title: 'Number',
    dataIndex: 'phone',
    key: 'phone',
    sorter: true,
  },
  {
    title: 'Email',
    dataIndex: 'mail',
    key: 'mail',
    sorter: true,
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

  const handleTableChange = (pagination, filters, sorter) => {
    // Handle table sort change
    const { field, order } = sorter;
    const sortedData = [...apiData];
    sortedData.sort((a, b) => {
      if (order === 'ascend') {
        return a[field].toString().localeCompare(b[field].toString());
      } else {
        return b[field].toString().localeCompare(a[field].toString());
      }
    });
    setApiData(sortedData);
  };

  return (
    <div>
      <h1>DashBoard Dentist</h1>
      <Flex>
        <FeatureAction />
      </Flex>
      <Flex>
        <TableList dataSource={apiData} columns={columns} onChange={handleTableChange} />
      </Flex>
    </div>
  );
};
