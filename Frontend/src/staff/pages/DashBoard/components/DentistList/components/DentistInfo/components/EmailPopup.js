import React, { useEffect, useState } from 'react';
import { Button, Select, Popover, notification } from 'antd';
import { HiOutlineMail } from 'react-icons/hi';
import { DentistServices } from '../../../../../../../services/DentistServices/DentistServices';
import { Option } from 'antd/es/mentions';

export const EmailPopup = ({ user }) => {
  const [selectedService, setSelectedService] = useState(null);
  const [servicesData, setServicesData] = useState([]);

  useEffect(() => {
    const fetchServices = async () => {
      try {
        const response = await DentistServices.getAllServices();
        console.log('API Response:', response);
        setServicesData(response);
      } catch (error) {
        console.error('Error fetching services:', error);
      }
    };
    fetchServices();
  }, []);

  const handleServiceChange = (value) => {
    console.log('Selected Service:', value);
    setSelectedService(value);
  };

  const handleSubmit = async () => {
    if (!user || !selectedService) {
      notification.error({
        message: 'Error',
        description: 'Please select both dentist and service.',
      });
      return;
    }

    try {
      const response = await DentistServices.setService(
        selectedService,
        user.mail
      );
      console.log('API Response:', response);
      setSelectedService(response);
      notification.success({
        message: 'Success',
        description: 'Services set successfully!',
      });
      // Add further logic as needed after successful API call
    } catch (error) {
      console.error('Error setting services:', error);
      notification.error({
        message: 'Error',
        description: error.message,
      });
    }
  };

  return (
    <Popover
      content={
        <div>
          <p>Select Service:</p>
          <Select
            placeholder='Select Service'
            style={{ width: '100%', marginBottom: '10px' }}
            onChange={handleServiceChange}
            value={selectedService}>
            {servicesData.map((service) => (
              <Option key={service.serviceID} value={service.serviceID}>
                {service.name}
              </Option>
            ))}
          </Select>
          <Button
            type='primary'
            onClick={handleSubmit}
            style={{ width: '100%' }}>
            Set Services
          </Button>
        </div>
      }
      trigger='click'
      placement='bottom'
      title={'Set Services for Customer'}>
      <Button
        icon={<HiOutlineMail style={{ marginRight: '5px' }} />}
        style={{
          marginBottom: '10px',
          display: 'block',
          marginLeft: 'auto',
          color: 'white',
          backgroundColor: 'blue',
          borderColor: 'blue',
        }}>
        Set Services
      </Button>
    </Popover>
  );
};
