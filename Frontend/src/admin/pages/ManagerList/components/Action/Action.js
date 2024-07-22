import React from 'react';

import { MdOutlineModeEdit } from 'react-icons/md';
import { FiTrash2 } from 'react-icons/fi';
import { Flex } from 'antd';
import { ModalInfo } from '../ModalInfo/ModalInfo';

import { CustomerServices } from '../../../../services/CustomerServer/CustomerServer';

export const Action = ({ record }) => {
  
  const [info, setInfo] = React.useState(record);
  const [open, setOpen] = React.useState(false);
  const [loading, setLoading] = React.useState(true);
  const handleUpdate = () => {
    showModal();
    const fetchData = async () => {
      try {
        const response = await CustomerServices.getById(record.mail);
        setLoading(false);
        setInfo(response);
      } catch (error) {
        console.log(error);
      }
    };
    fetchData();
  };

  const showModal = () => {
    setOpen(true);
    setLoading(true);
  };


  const handleDelete = async () => {
    try {
     const response = await CustomerServices.deleteCustomer(record.id);
      // setLoading(false);
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      <Flex style={{ width: '40px' }} justify='space-between'>
        <div>
          <MdOutlineModeEdit onClick={handleUpdate} />
        </div>
        <div>
          <FiTrash2 onClick={() => handleDelete(record.id)} />
        </div>
      </Flex>
      <ModalInfo
        open={open}
        setOpen={setOpen}
        loading={loading}
        showModal={showModal}
        info={info}
      />
    </>
  );
};
