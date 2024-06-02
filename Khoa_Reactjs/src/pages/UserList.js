  import { Flex, Typography } from 'antd';
  import React, { useState } from 'react';
  import DataTable from '../components/DataTable';
  import axios from 'axios';
  const { Title } = Typography;

  export const UserList = () => {
    const [apiData, setApiData] = useState([]);
    axios
      .get('https://6541299af0b8287df1fdf263.mockapi.io/syllabus')
      .then(function (response) {
        // handle success
        setApiData(response.data);
      })
      .catch(function (error) {
        // handle error
        console.log(error);
      });
    return (  
      <Flex vertical>
        <Title>User List</Title>
        <div>
          <DataTable apiData={apiData} />
        </div>
      </Flex>
    );
  };
