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

  useEffect(() => {
    // Load scheduled tasks from local storage on component mount
    const storedTasks =
      JSON.parse(localStorage.getItem('scheduledTasks')) || [];
    setScheduledTasks(storedTasks);

    // Load dentist list from API on component mount
    fetchDentistList();
  }, []);

  useEffect(() => {
    // Save scheduled tasks to local storage whenever scheduledTasks state changes
    localStorage.setItem('scheduledTasks', JSON.stringify(scheduledTasks));
  }, [scheduledTasks]);

  const fetchDentistList = async () => {
    try {
      const dentistsList = await TimetableServices.getAllDentists();
      console.log(dentistsList);
      setDentistList(dentistsList.dentistList);
      setTimeSlotList(dentistsList.timeSlotList);
    } catch (error) {
      console.error('Error fetching dentist list:', error);
    }
  };

  const handleDateRangeChange = (dates) => {
    console.log(dates);
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
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Mail',
      dataIndex: 'mail',
      key: 'mail',
    },
  ];

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
        />
        <Select
          style={{ width: '200px', marginRight: '10px' }}
          placeholder='Select or Name'
          onChange={handleDentistChange}
          allowClear>
          {dentistList.map((dentist) => (
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
        pagination={false}
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
