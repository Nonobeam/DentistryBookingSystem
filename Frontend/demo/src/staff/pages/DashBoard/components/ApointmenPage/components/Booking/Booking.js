import React, { useState, useEffect } from 'react';
import {
  Form,
  Select,
  Button,
  notification,
  DatePicker,
  Input,
  Row,
  Col,
  Typography,
} from 'antd';
import { 
  CalendarOutlined, 
  MedicineBoxOutlined, 
  ClockCircleOutlined, 
  MailOutlined, 
  TeamOutlined,
  BookOutlined
} from '@ant-design/icons';
import { AppointmentHistoryServices } from '../../../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';
import dayjs from 'dayjs';
import { DentistServices } from '../../../../../../services/CustomerServices/CustomerServices';

const { Title } = Typography;

export const Booking = () => {
  const [services, setServices] = useState([]);
  const [selectedService, setSelectedService] = useState(null);
  const [loading, setLoading] = useState(false);
  const [selectedDate, setSelectedDate] = useState(null);
  const [schedules, setSchedules] = useState([]);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const [dependentID, setDependentID] = useState([]);
  const [customerMail, setCustomerMail] = useState('');
  const [isValidMail, setIsValidMail] = useState(false);
  const [isValidAppointment, setIsValidAppointment] = useState(true);
  const [form] = Form.useForm();

  useEffect(() => {
    const fetchServices = async () => {
      setLoading(true);
      try {
        if (selectedDate) {
          const formattedDate = selectedDate.format('YYYY-MM-DD');
          const data = await AppointmentHistoryServices.getAllServices(
            formattedDate
          );
          setServices(data);
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
  }, [selectedDate]);

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
      if (response) {
        setDependentID(response);
      } else {
        setDependentID([]);
      }
    } catch (error) {
      console.error('Error fetching dependents:', error);
    }
  };

  const handleCustomerMailChange = (e) => {
    const { value } = e.target;
    setCustomerMail(value);
    const isValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
    setIsValidMail(isValid);
  };

  const handleBlur = async () => {
    if (isValidMail && customerMail) {
      const res = await DentistServices.getDentistById(customerMail);
      setIsValidAppointment(res.appointment.length < 5);
      if (isValidAppointment) {
        await fetchDependents();
        setLoading(false);
      }
    }
  };

  const onFinish = async (values) => {
    setLoading(true);
    try {
      let { dependentID, customerMail } = values;
      if (dependentID === '') {
        dependentID = undefined;
      }
      const serviceId = selectedService;
      const scheduleId = selectedSchedule || null;
      await AppointmentHistoryServices.makeBooking({
        dependentID,
        customerMail,
        serviceId,
        dentistScheduleId: scheduleId,
      });
      notification.success({
        message: 'Booking Successful',
        description: 'Your appointment has been booked successfully.',
      });
    } catch (error) {
      console.error('Error booking appointment:', error);
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

  // Function to disable dates more than 2 months from today
  const disabledDate = (current) => {
    return current && current > dayjs().add(2, 'months');
  };

  return (
    <Form
      form={form}
      layout='vertical'
      onFinish={onFinish}
      initialValues={{ customerMail }}
      style={{
        padding: '30px',
        fontFamily: 'Arial, sans-serif',
        maxWidth: '800px',
        margin: '50px Auto',
        backgroundColor: '#f0f8ff',
        borderRadius: '15px',
        boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
      }}>
      <Title level={2} style={{ textAlign: 'center', color: '#1890ff', marginBottom: '30px' }}>
        <BookOutlined style={{ marginRight: '10px' }} />
        Booking For Customer
      </Title>
      <Row gutter={16}>
        <Col span={12}>
          <Form.Item
            name='date'
            label={<span style={{ color: '#1890ff' }}><CalendarOutlined /> Select Date</span>}
            rules={[{ required: true, message: 'Please select a date' }]}>
            <DatePicker
              onChange={handleDateChange}
              disabledDate={disabledDate}
              style={{ width: '100%' }}
            />
          </Form.Item>
        </Col>
        <Col span={12}>
          <Form.Item
            name='service'
            label={<span style={{ color: '#1890ff' }}><MedicineBoxOutlined /> Select Service</span>}
            rules={[{ required: true, message: 'Please select a service' }]}>
            <Select
              onChange={handleServicesChange}
              loading={loading}
              style={{ width: '100%' }}>
              {services.map((service) => (
                <Select.Option
                  key={service.serviceID}
                  value={service.serviceID}>
                  {service.name}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
        </Col>
      </Row>
      <Row gutter={16}>
        <Col span={12}>
          <Form.Item
            name='schedule'
            label={<span style={{ color: '#1890ff' }}><ClockCircleOutlined /> Select Schedule</span>}
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
        </Col>
        <Col span={12}>
          <Form.Item
            name='customerMail'
            label={<span style={{ color: '#1890ff' }}><MailOutlined /> Customer Email</span>}
            rules={[
              { required: true, message: 'Please enter customer email' },
              { type: 'email', message: 'Please enter a valid email' },
            ]}
            help={
              isValidAppointment ? (
                ''
              ) : (
                <span style={{ color: 'red' }}>
                  Maximum 5 appointments allowed
                </span>
              )
            }>
            <Input
              onChange={handleCustomerMailChange}
              onBlur={handleBlur}
              value={customerMail}
              style={{ width: '100%' }}
            />
          </Form.Item>
        </Col>
      </Row>
      <Form.Item
        name='dependentID'
        label={<span style={{ color: '#1890ff' }}><TeamOutlined /> Dependent</span>}
        loading={loading}
        rules={[{ required: false, message: 'Please select a dependent' }]}>
        <Select style={{ width: '100%' }}>
          <Select.Option value=''></Select.Option>
          {dependentID.map((dependent) => (
            <Select.Option
              key={dependent.dependentID}
              value={dependent.dependentID}>
              {dependent.name}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item style={{ textAlign: 'center' }}>
        <Button
          type='primary'
          htmlType='submit'
          disabled={!isValidAppointment}
          loading={loading}
          icon={<BookOutlined />}
          style={{
            backgroundColor: '#1890ff',
            borderColor: '#1890ff',
            padding: '0 30px',
            height: '40px',
            fontSize: '16px',
          }}>
          Book Service
        </Button>
      </Form.Item>
    </Form>
  );
};