import React, { useEffect, useState } from 'react';
import { Flex, Spin } from 'antd'; // Spin là component của Ant Design để hiển thị loading
import { TableList } from './components/Table/TableList';
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
  const [loading, setLoading] = useState(true); // State để theo dõi trạng thái loading

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await DentistServices.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setLoading(false); // Kết thúc fetch data, đánh dấu là không còn loading nữa
      }
    };
    fetchData();
  }, []);

  return (
    <div>
      <h1>Dentist List</h1>
      <Flex>
        {loading ? ( // Kiểm tra nếu đang loading thì hiển thị Spin (biểu tượng loading)
          <Spin size="large" />
        ) : (
          <TableList dataSource={apiData} columns={columns} />
        )}
      </Flex>
    </div>
  );
};
