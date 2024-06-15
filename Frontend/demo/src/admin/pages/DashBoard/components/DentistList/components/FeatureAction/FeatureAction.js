import React from 'react';
import { Button, Dropdown, message, Flex, Space } from 'antd';
import { CiCirclePlus } from 'react-icons/ci';
import { DownOutlined } from '@ant-design/icons';
import { FiDownload } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';

const items = [];

const handleMenuClick = (e) => {
  message.info('Click on menu item.');
  console.log('click', e);
};

const menuProps = {
  items,
  onClick: handleMenuClick,
};

export const FeatureAction = () => {
  const navigate = useNavigate();
  const handleNavigate = (link) => {
    navigate(link);
  };
  return (
    <Flex style={{ width: '100%', justifyContent: 'space-between' }}>
      <Flex>
        <Button
          onClick={() => handleNavigate('/editform')}
          style={{ marginRight: '10px', backgroundColor: '#1890ff', color: '#fff' }}
          type='primary'
          icon={<CiCirclePlus />}
          
        >
          Thêm form
        </Button>
        <Button
          style={{ backgroundColor: '#1890ff', color: '#fff' }}
          type='primary'
          icon={<FiDownload />}
        >
          Tải về
        </Button>
      </Flex>
      <Flex>
        <Dropdown menu={menuProps}>
          <Button style={{ backgroundColor: '#1890ff', color: '#fff' }}>
            <Space>
              Button <DownOutlined />
            </Space>
          </Button>
        </Dropdown>
        <Dropdown menu={menuProps}>
          <Button style={{ backgroundColor: '#1890ff', color: '#fff' }}>
            <Space>
              Button <DownOutlined />
            </Space>
          </Button>
        </Dropdown>
      </Flex>
    </Flex>
  );
};
