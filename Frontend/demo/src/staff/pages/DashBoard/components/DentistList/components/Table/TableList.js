import { Table } from 'antd';
import React from 'react';

export const TableList = ({ dataSource, columns }) => {
  return (
    <div style={{ width: '100%' }}>
      <Table dataSource={dataSource} columns={columns} />
    </div>
  );
};
