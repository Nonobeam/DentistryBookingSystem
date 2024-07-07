import React from 'react';
import { MdOutlineRemoveRedEye } from 'react-icons/md';
import { Flex } from 'antd';
import { ModalInfo } from '../ModalInfo/ModalInfo';
import { useNavigate } from 'react-router-dom';
import { DentistServices } from '../../../../../../services/DentistServices/DentistServices';

export const Action = ({ record }) => {
  const navigator = useNavigate();
  const [info, setInfo] = React.useState(record);
  const [open, setOpen] = React.useState(false);
  const [loading, setLoading] = React.useState(true);
  const handleUpdate = () => {
    showModal();
    const fetchData = async () => {
      try {
        const response = await DentistServices.getById(record.mail);
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

  const handleClickEye = () => {
    // console.log(record);
    navigator(`detail/${record.mail}`);
  };
  return (
    <>
      <Flex style={{ width: '80px' }} justify='space-between'>
        <div>
          <MdOutlineRemoveRedEye onClick={handleClickEye} />
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
