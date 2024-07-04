import React from 'react';
import { Button, message, Flex } from 'antd';
import { CiCirclePlus } from 'react-icons/ci';

import { useNavigate } from 'react-router-dom';



const handleMenuClick = (e) => {
  message.info('Click on menu item.');
  console.log('click', e);
};



export const FeatureAction = () => {
  const navigate = useNavigate();
  const handleNavigate = (link) => {
    navigate(link);
  };
  return (
    <Flex style={{ width: '100%', justifyContent: 'space-between' }}>
      
        <Button
          onClick={() => handleNavigate('/editform')}
          style={{ marginRight: '10px', backgroundColor: '#1890ff', color: '#fff' }}
          type='primary'
          icon={<CiCirclePlus />}
          
        >
          Thêm form
        </Button>
    </Flex>
  );
};
