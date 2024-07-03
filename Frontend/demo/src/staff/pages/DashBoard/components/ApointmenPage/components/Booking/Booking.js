// BookingForm.js
import React, { useState, useEffect } from 'react';
import { Form, Select, Button, notification, DatePicker, Input } from 'antd';
import { AppointmentHistoryServices } from '../../../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';

export const Booking = () => {
  const [services, setServices] = useState([]);
  const [selectedService, setSelectedService] = useState(null);
  const [loading, setLoading] = useState(false);
  const [selectedDate, setSelectedDate] = useState(null); // State to hold selected date
  const [schedules, setSchedules] = useState([]);
  const [selectedSchedule, setSelectedSchedule] = useState(null); // State to hold selected schedule

  useEffect(() => {
    const fetchServices = async () => {
      setLoading(true);
      try {
        if (selectedDate) {
          const formattedDate = selectedDate.format('YYYY-MM-DD');
          const data = await AppointmentHistoryServices.getAllServices(formattedDate);
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
          const data = await AppointmentHistoryServices.getAllAvailableSchedule({
            workDate: formattedDate,
            servicesID: selectedService,
          });
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

  const onFinish = async (values) => {
    console.log('Success:', values);
    setLoading(true);
    try {
      const { dependentID, customerMail } = values;
      const serviceId = selectedService;
      const scheduleId = selectedSchedule ? selectedSchedule.scheduleID : null; // Get schedule ID from selected schedule
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
    const selected = schedules.find(schedule => schedule.scheduleID === value);
    setSelectedSchedule(selected);
  };

  return (
    <Form layout='vertical' onFinish={onFinish}>
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
        <Select onChange={handleScheduleChange} loading={loading} disabled={!selectedService || !selectedDate}>
          {schedules.map((schedule) => (
            <Select.Option key={schedule.scheduleID} value={schedule.scheduleID}>
              {`${schedule.timeslot.startTime} - Time Slot: ${schedule.timeslot.slotNumber}`} Example format assuming timeslot properties
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item
        name='customerMail'
        label='Customer Email'
        rules={[{ required: true, message: 'Please enter customer email' }]}>
        <Input />
      </Form.Item>
      <Form.Item
        name='dependentID'
        label='Dependent ID'>
        <Input />
      </Form.Item>
      <Form.Item>
        <Button type='primary' htmlType='submit' loading={loading}>
          Book Service
        </Button>
      </Form.Item>
    </Form>
  );
};

