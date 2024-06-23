import { Flex } from 'antd';
import React, { useEffect, useState } from 'react';
import { TableList } from './components/Table/TableList';
import { FeatureAction } from './components/FeatureAction/FeatureAction';
import axios from 'axios';

import { Action } from './components/Action/Action';
import { DentistServices } from '../../../../services/DentistServices/DentistServices';

const columns = [
  {
    title: 'Full Name',
    dataIndex: `firstName`,
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
    dataIndex: '',
    key: 'x',
    render: (_, record) => <Action record={record} />,
  },
];

export const DentistList = () => {
  const [apiData, setApiData] = useState([]);
  const [listDentist, setListDentist] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const response = await DentistServices.getAll();
      console.log(response);
      const dataDentist = response.map((item) => {
        return item.user;
      });

      setListDentist(dataDentist);
      setApiData(response);
    };
    fetchData();
  }, []);
  return (
    <div>
      <h1>DashBoard</h1>
      <Flex>
        {' '}
        <FeatureAction />
      </Flex>
      <Flex>
        <TableList dataSource={listDentist} columns={columns} />
      </Flex>
    </div>
  );
};
