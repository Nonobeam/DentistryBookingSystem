import React from 'react';
import { Table } from 'antd';
const columns = [
  {
    title: 'Topic Code',
    dataIndex: 'topicCode',
    showSorterTooltip: {
      target: 'full-header',
    },
    sorter: (a, b) => a.topicCode - b.topicCode,
  },
  {
    title: 'Topic Name',
    dataIndex: 'topicName',
    defaultSortOrder: 'descend',
    sorter: (a, b) => a.topicName.length - b.topicName.length,
  },
  {
    title: 'Create Date',
    dataIndex: 'createdDate',
  },
];
const data = [
  {
    key: '1',
    name: 'John Brown',
    age: 32,
    address: 'New York No. 1 Lake Park',
  },
  {
    key: '2',
    name: 'Jim Green',
    age: 42,
    address: 'London No. 1 Lake Park',
  },
  {
    key: '3',
    name: 'Joe Black',
    age: 32,
    address: 'Sydney No. 1 Lake Park',
  },
  {
    key: '4',
    name: 'Jim Red',
    age: 32,
    address: 'London No. 2 Lake Park',
  },
];
const onChange = (pagination, filters, sorter, extra) => {};
const DataTable = ({ apiData }) => (
  <Table
    columns={columns}
    dataSource={apiData}
    onChange={onChange}
    showSorterTooltip={{
      target: 'sorter-icon',
    }}
  />
);
export default DataTable;
