import React, { useState, useEffect } from 'react';
import { Form, Select, Button, notification, DatePicker, Input } from 'antd';
import { AppointmentHistoryServices } from '../../../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';
import { CustomerServicess } from '../../../../../../services/CustomerServicess/CustomerServicess';

export const Booking = () => {
  const [services, setServices] = useState([]);
  const [selectedService, setSelectedService] = useState(null);
  const [loading, setLoading] = useState(false);
  const [selectedDate, setSelectedDate] = useState(null); // State to hold selected date
  const [schedules, setSchedules] = useState([]);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const [dependentID, setDependentID] = useState([]); // State to hold selected schedule
  const [customerMail, setCustomerMail] = useState(''); // State to hold customer email
  const [isValidMail, setIsValidMail] = useState(false); // State to track valid email
  const [form] = Form.useForm(); // Form instance to control form validation

  useEffect(() => {
    const fetchServices = async () => {
      setLoading(true);
      try {
        if (selectedDate) {
          const formattedDate = selectedDate.format('YYYY-MM-DD');
          const data = await AppointmentHistoryServices.getAllServices(
            formattedDate
          );
          setServices(data); // Assuming data is an array of service objects
        }
      } catch (error) {
        notification.error({
          message: 'Error',
          description: error.message,
        });
      } finally {
        setLoading(false);
      }
    };

    fetchServices();
  }, [selectedDate]); // Fetch services whenever selectedDate changes

  useEffect(() => {
    const fetchSchedules = async () => {
      setLoading(true);
      if (selectedService && selectedDate) {
        try {
          const formattedDate = selectedDate.format('YYYY-MM-DD');
          const data = await AppointmentHistoryServices.getAllAvailableSchedule(
            {
              workDate: formattedDate,
              servicesID: selectedService,
            }
          );
          setSchedules(data);
        } catch (error) {
          notification.error({
            message: 'Error',
            description: error.message,
          });
        } finally {
          setLoading(false);
        }
      }
    };

    fetchSchedules();
  }, [selectedService, selectedDate]);

  const fetchDependents = async () => {
    try {
      const response = await AppointmentHistoryServices.getDependents(
        customerMail
      );
      setDependentID(response);
    } catch (error) {
      console.error('Error fetching dependents:', error);
    }
  };

  const handleCustomerMailChange = (e) => {
    const { value } = e.target;
    setCustomerMail(value);
    // Validate email format
    const isValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value); // Updated regex
    setIsValidMail(isValid && value.endsWith('.com'));
  };

  const handleBlur = async () => {
    // Trigger API call when focus leaves email input
    if (isValidMail && customerMail) {
      await fetchDependents();
      setLoading(false);
    }
  };

  const onFinish = async (values) => {
    console.log('Success:', values);
    setLoading(true);
    try {
      const { dependentID, customerMail } = values;
      const serviceId = selectedService;
      const scheduleId = selectedSchedule ? selectedSchedule : null; // Get schedule ID from selected schedule
      console.log('Selected Schedule ID:', scheduleId);
      await AppointmentHistoryServices.makeBooking({
        dependentID: dependentID || null, // Pass null if dependentID is not provided
        customerMail,
        serviceId,
        dentistScheduleId: scheduleId, // Pass dentistScheduleId obtained from selectedSchedule
      });
      notification.success({
        message: 'Booking Successful',
        description: 'Your appointment has been booked successfully.',
      });
    } catch (error) {
      notification.error({
        message: 'Booking Failed',
        description: error.message,
      });
    } finally {
      setLoading(false);
    }
  };

  const handleServicesChange = (value) => {
    setSelectedService(value);
    setSelectedSchedule(null); // Reset selected schedule when service changes
  };

  const handleDateChange = (date) => {
    setSelectedDate(date);
    setSelectedSchedule(null); // Reset selected schedule when date changes
  };

  const handleScheduleChange = (value) => {
    console.log('Selected Schedule:', value);
    setSelectedSchedule(value);
  };

  return (
    <Form
      form={form}
      layout='vertical'
      onFinish={onFinish}
      initialValues={{ customerMail }}>
      <Form.Item
        name='date'
        label='Select Date'
        rules={[{ required: true, message: 'Please select a date' }]}>
        <DatePicker onChange={handleDateChange} />
      </Form.Item>
      <Form.Item
        name='service'
        label='Select Service'
        rules={[{ required: true, message: 'Please select a service' }]}>
        <Select onChange={handleServicesChange} loading={loading}>
          {services.map((service) => (
            <Select.Option key={service.serviceID} value={service.serviceID}>
              {service.name}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item
        name='schedule'
        label='Select Schedule'
        rules={[{ required: true, message: 'Please select a schedule' }]}>
        <Select
          onChange={handleScheduleChange}
          loading={loading}
          disabled={!selectedService || !selectedDate}>
          {schedules.map((schedule) => (
            <Select.Option
              key={schedule.dentistScheduleID}
              value={schedule.dentistScheduleID}>
              {`${schedule.startTime}`}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item
        name='customerMail'
        label='Customer Email'
        rules={[
          { required: true, message: 'Please enter customer email' },
          { type: 'email', message: 'Please enter a valid email' },
        ]}>
        <Input
          onChange={handleCustomerMailChange}
          onBlur={handleBlur}
          value={customerMail}
        />
      </Form.Item>
      <Form.Item
        name='dependentID'
        label='Dependent ID'
        loading={loading}
        rules={[{ required: true, message: 'Please select a dependent' }]}>
        <Select>
          {dependentID.map((dependent) => (
            <Select.Option
              key={dependent.dependentID}
              value={dependent.dependentID}>
              {dependent.name}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item>
        <Button type='primary' htmlType='submit' loading={loading}>
          Book Service
        </Button>
      </Form.Item>
    </Form>
  );
};
