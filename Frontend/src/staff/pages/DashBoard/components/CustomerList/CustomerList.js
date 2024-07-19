import React, { useState } from 'react';
import { Flex, Spin, Input } from 'antd'; // Import Input từ Ant Design
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
  const { data, isValidating } = useSWR('Customer', fetchData);
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
      <h1>Customer List</h1>
      <Flex justify="space-between" style={{ marginBottom: '16px' }}>
        <Input.Search
          placeholder="Search by name"
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
