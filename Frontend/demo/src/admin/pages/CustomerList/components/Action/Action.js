import React, { useState, useEffect } from 'react';
import { MdOutlineRemoveRedEye } from 'react-icons/md';
import { MdOutlineModeEdit } from 'react-icons/md';
import { FiTrash2 } from 'react-icons/fi';
import { Flex, message } from 'antd';
import { ModalInfo } from '../ModalInfo/ModalInfo';
import { useNavigate } from 'react-router-dom';
import { CustomerServices } from '../../../../services/CustomerServer/CustomerServer';

export const Action = ({ record, data, setApiData }) => {
  const navigator = useNavigate();
  const [info, setInfo] = useState(record);
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  // Function to fetch customer data by email
  const fetchData = async () => {
    try {
      const response = await CustomerServices.getById(record.mail);
      setInfo(response); // Update info state with the latest data
      setLoading(false);
    } catch (error) {
      console.log(error);
    }
  };

  // Handle opening modal and fetching data
  const handleUpdate = () => {
    showModal();
    fetchData(); // Fetch data when updating
  };

  const showModal = () => {
    setOpen(true);
    setLoading(true);
  };

  // Handle delete action
  const handleDelete = async () => {
    try {
      const response = await CustomerServices.deleteCustomer(record.id);
      message.success('Deleted successfully');
      // Refresh or update the list of customers if needed
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  // useEffect to fetch data when modal opens or open state changes
  useEffect(() => {
    if (open) {
      fetchData();
    }
  }, [open]);

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
        data={data}
        setApiData={setApiData}
      />
    </>
  );
};
