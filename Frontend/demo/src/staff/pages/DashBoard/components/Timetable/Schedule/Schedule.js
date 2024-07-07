// Schedule.js

import React, { useState, useEffect } from 'react';
import {
  Button,
  DatePicker,
  Input,
  TimePicker,
  Table,
  Modal,
  Form,
  Select,
  notification,
} from 'antd';
import moment from 'moment';
import { v4 as uuidv4 } from 'uuid';
import TimetableServices from '../../../../../services/TimetableServices/TimetableServices';

const { RangePicker: DateRangePicker } = DatePicker;
const { RangePicker: TimeRangePicker } = TimePicker;
const { Option } = Select;

const Schedule = () => {
  const [selectedDateRange, setSelectedDateRange] = useState([]);
  const [selectedTimeRange, setSelectedTimeRange] = useState(undefined);
  const [timeSlotList, setTimeSlotList] = useState([]);
  const [dentistName, setDentistName] = useState('');
  const [scheduledTasks, setScheduledTasks] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [dentistList, setDentistList] = useState([]);
  const [dentistListSchedule, setDentistListSchedule] = useState([]);

  const fetchDentistList = async () => {
    try {
      const dentistsList = await TimetableServices.getAllDentists();
      console.log(dentistsList);
      setDentistList(dentistsList);
      // setTimeSlotList(dentistsList.timeSlotList);
    } catch (error) {
      console.error('Error fetching dentist list:', error);
    }
  };

  const fetchDentistListAndTimeSlot = async () => {
    try {
      console.log(
        selectedDateRange[0].format('YYYY-MM-DD'),
        selectedDateRange[1].format('YYYY-MM-DD')
      );
      const dentistsList = await TimetableServices.getAllDentistsAndTimeSlot(
        selectedDateRange[0].format('YYYY-MM-DD'),
        selectedDateRange[1].format('YYYY-MM-DD')
      );
      console.log(dentistsList.dentistList);
      setDentistListSchedule(dentistsList.dentistList);
      setTimeSlotList(dentistsList.timeSlotList);
    } catch (error) {
      console.error('Error fetching dentist list:', error);
    }
  };

  useEffect(() => {
    fetchDentistList();
  }, []);

  useEffect(() => {
    fetchDentistListAndTimeSlot();
  }, [selectedDateRange]);

  const handleDateRangeChange = (dates) => {
    if (dates === null || dates.length === 0) {
      setSelectedDateRange([]);
    } else {
      setSelectedDateRange(dates);
    }
  };

  const handleTimeRangeChange = (times) => {
    console.log(times);
    setSelectedTimeRange(times);
  };

  const handleDentistChange = (value) => {
    console.log(value);
    setDentistName(value);
  };

  const handleSchedule = async () => {
    if (
      selectedDateRange.length === 0 ||
      selectedTimeRange === undefined ||
      !dentistName
    ) {
      alert(
        'Please select date range, time range, and enter/select task name.'
      );
      return;
    }

    try {
      const response = await TimetableServices.setSchedule({
        dentistMail: dentistName,
        startDate: selectedDateRange[0].format('YYYY-MM-DD'),
        endDate: selectedDateRange[1].format('YYYY-MM-DD'),
        slotNumber: selectedTimeRange,
      });
      console.log(response);
      if (response) {
        notification.success({
          message: 'Schedule Successfully',
          description: 'Your schedule has been set successfully.',
        });
        setModalVisible(false);
        form.resetFields();
      }
    } catch (error) {
      notification.error({
        message: 'Failed to set schedule',
        description: error.message,
      });
    }

    // const updatedTasks = [...scheduledTasks, ...newTasks];
    // setScheduledTasks(updatedTasks);

    // Clear input fields after scheduling
    setSelectedDateRange([]);
    setSelectedTimeRange([]);
    setDentistName('');
  };

  const handleCancel = () => {
    setModalVisible(false);
    form.resetFields();
  };

  const columns = [
    {
      title: 'Name',
      dataIndex: 'dentist',
      key: 'name',
      render: (dentist) => dentist.user?.name,
    },
    {
      title: 'Mail',
      dataIndex: 'dentist',
      key: 'mail',
      render: (dentist) => dentist.user?.mail,
    },
    {
      title: 'Phone',
      dataIndex: 'dentist',
      key: 'phone',
      render: (dentist) => dentist.user?.phone,
    },
    {
      title: 'Work Date',
      dataIndex: 'workDate',
      key: 'workDate',
      
    },
    {
      title: 'Time Slot',
      dataIndex: 'timeslot',
      key: 'timeslot',
      render: (timeslot) =>
        `${timeslot.startTime} - slot number: ${timeslot.slotNumber}`,
    },
  ];

  const disabledDate = (current) => {
    // Allow dates up to two months from today
    return (
      current &&
      (current < moment().startOf('day') ||
        current > moment().add(2, 'months').endOf('day'))
    );
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>Schedule Doctor's Work</h1>
      <div style={{ marginBottom: '20px' }}>
        <DateRangePicker
          value={selectedDateRange}
          onChange={handleDateRangeChange}
          style={{ marginRight: '10px' }}
          format='DD/MM/YYYY'
          placeholder={['Start Date', 'End Date']}
          disabledDate={disabledDate}
        />
        <Select
          style={{ width: '200px', marginRight: '10px' }}
          placeholder='Select or Name'
          onChange={handleDentistChange}
          allowClear>
          {dentistListSchedule.map((dentist) => (
            <Option key={dentist.mail} value={dentist.mail}>
              {dentist.name}
            </Option>
          ))}
        </Select>
        <Select
          style={{ width: '200px', marginRight: '10px' }}
          placeholder='Select or enter Task Name'
          onChange={handleTimeRangeChange}
          allowClear>
          {timeSlotList.map((timeSlot, index) => (
            <Option
              key={timeSlot.slotNumber + index}
              value={timeSlot.slotNumber}>
              {timeSlot.startTime}
            </Option>
          ))}
        </Select>
        <Button onClick={handleSchedule} type='primary'>
          Schedule Task
        </Button>
      </div>

      <Table
        dataSource={dentistList}
        columns={columns}
        style={{ marginBottom: '20px' }}
      />

      <Modal
        title='Edit Task'
        visible={modalVisible}
        onCancel={handleCancel}
        footer={[
          <Button key='cancel' onClick={handleCancel}>
            Cancel
          </Button>,
          // <Button key='update' type='primary' onClick={handleUpdate}>
          //   Update
          // </Button>,
        ]}>
        <Form form={form} layout='vertical'>
          <Form.Item
            name='date'
            label='Date'
            rules={[{ required: true, message: 'Please select date!' }]}>
            <DatePicker format='DD/MM/YYYY' />
          </Form.Item>
          <Form.Item
            name='time'
            label='Time'
            rules={[{ required: true, message: 'Please select time!' }]}>
            <TimePicker format='HH:mm' />
          </Form.Item>
          <Form.Item
            name='task'
            label='Task'
            rules={[{ required: true, message: 'Please enter task!' }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Schedule;
