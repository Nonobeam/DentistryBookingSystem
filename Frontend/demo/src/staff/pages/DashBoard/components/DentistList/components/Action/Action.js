import React from 'react';
import { MdOutlineRemoveRedEye } from 'react-icons/md';
import { IoEyeOffOutline } from 'react-icons/io5';
import { MdOutlineModeEdit } from 'react-icons/md';
import { FiTrash2 } from 'react-icons/fi';
import { Flex } from 'antd';
import { ModalInfo } from '../ModalInfo/ModalInfo';
import { StudentServices } from '../../../../../../services/StudentServices/StudentServices';

export const Action = ({ record }) => {
  const [info, setInfo] = React.useState(record);
  const [open, setOpen] = React.useState(false);
  const [loading, setLoading] = React.useState(true);
  const handleUpdate = () => {
    showModal();
    const fetchData = async () => {
      try {
        const response = await StudentServices.getById(record.id);
        setLoading(false);
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

  const handleDelete = () => {};

  return (
    <>
      <Flex style={{ width: '80px' }} justify='space-between'>
        <MdOutlineRemoveRedEye onClick={handleUpdate} />
        <MdOutlineModeEdit onClick={handleUpdate} />
        <FiTrash2 onClick={handleDelete} />
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
