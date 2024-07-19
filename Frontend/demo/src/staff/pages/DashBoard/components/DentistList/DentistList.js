import React, { useState } from 'react';
import { Flex, Input, Spin } from 'antd';
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
  const { data, isValidating } = useSWR('dentistServices', fetchData);
  const [searchText, setSearchText] = useState('');

  const handleSearch = value => {
    setSearchText(value);
  };

  // Lọc dữ liệu dựa trên searchText
  const filteredData = searchText
    ? data.filter(item =>
        item.name.toLowerCase().includes(searchText.toLowerCase())
      )
    : data;

  return (
    <div>
      <h1>Dentist List</h1>
      <Flex justify='space-between' style={{ marginBottom: '16px' }}>
        <Input.Search
          placeholder='Search by name'
          onSearch={handleSearch}
          loading={isValidating} // Sử dụng isValidating để chỉ ra khi đang tải dữ liệu
          style={{ width: 200 }}
        />
      </Flex>
      <Spin spinning={!data}>
        <TableList dataSource={filteredData} columns={columns} />
      </Spin>
    </div>
  );
};
