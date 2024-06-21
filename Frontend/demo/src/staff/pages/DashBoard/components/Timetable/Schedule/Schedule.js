import React, { useState, useEffect } from 'react';
import { Button, DatePicker, Input, TimePicker, Table, Modal, Form, Select } from 'antd';
import moment from 'moment';
import { v4 as uuidv4 } from 'uuid';

// const { Column } = Table;
const { RangePicker: DateRangePicker } = DatePicker;
const { RangePicker: TimeRangePicker } = TimePicker;
const { Option } = Select;

const taskOptions = ['khoa', 'thư', 'nam', 'phuc', 'tuan'];

const Schedule = () => {
  const [selectedDateRange, setSelectedDateRange] = useState([]);
  const [selectedTimeRange, setSelectedTimeRange] = useState([]);
  const [taskName, setTaskName] = useState('');
  const [scheduledTasks, setScheduledTasks] = useState([]);
  const [editTask, setEditTask] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    // Load scheduled tasks from local storage on component mount
    const storedTasks = JSON.parse(localStorage.getItem('scheduledTasks')) || [];
    setScheduledTasks(storedTasks);
  }, []);

  useEffect(() => {
    // Save scheduled tasks to local storage whenever scheduledTasks state changes
    localStorage.setItem('scheduledTasks', JSON.stringify(scheduledTasks));
  }, [scheduledTasks]);

  const handleDateRangeChange = (dates) => {
    setSelectedDateRange(dates);
  };

  const handleTimeRangeChange = (times) => {
    setSelectedTimeRange(times);
  };

  const handleTaskNameChange = (value) => {
    setTaskName(value);
  };

  const handleSchedule = () => {
    if (selectedDateRange.length === 0 || selectedTimeRange.length === 0 || !taskName) {
      alert('Please select date range, time range, and enter/select task name.');
      return;
    }

    const formattedDateRange = selectedDateRange.map(date => date.format('DD/MM/YYYY'));
    const formattedTimeRange = selectedTimeRange.map(time => time.format('HH:mm'));
    
    // Generate tasks for each date and time combination
    const newTasks = [];
    formattedDateRange.forEach(date => {
      formattedTimeRange.forEach(time => {
        newTasks.push({ id: uuidv4(), time, date, task: taskName });
      });
    });

    const updatedTasks = [...scheduledTasks, ...newTasks];
    setScheduledTasks(updatedTasks);

    // Clear input fields after scheduling
    setSelectedDateRange([]);
    setSelectedTimeRange([]);
    setTaskName('');
  };

  const handleDelete = (task) => {
    const updatedTasks = scheduledTasks.filter((t) => t.id !== task.id);
    setScheduledTasks(updatedTasks);
  };

  const handleEdit = (task) => {
    setEditTask(task);
    setModalVisible(true);
    form.setFieldsValue({ date: moment(task.date, 'DD/MM/YYYY'), time: moment(task.time, 'HH:mm'), task: task.task });
  };

  const handleUpdate = () => {
    form.validateFields().then((values) => {
      const updatedTask = {
        ...editTask,
        date: values.date.format('DD/MM/YYYY'),
        time: values.time.format('HH:mm'),
        task: values.task,
      };

      const updatedTasks = scheduledTasks.map((task) => (task.id === updatedTask.id ? updatedTask : task));
      setScheduledTasks(updatedTasks);
      setModalVisible(false);
      form.resetFields();
      setEditTask(null);
    });
  };

  const handleCancel = () => {
    setModalVisible(false);
    form.resetFields();
    setEditTask(null);
  };

  const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
      render: (text) => moment(text, 'DD/MM/YYYY').format('DD/MM/YYYY'),
    },
    {
      title: 'Time',
      dataIndex: 'time',
      key: 'time',
    },
    {
      title: 'Task',
      dataIndex: 'task',
      key: 'task',
    },
    {
      title: 'Action',
      key: 'action',
      render: (text, record) => (
        <span>
          <Button onClick={() => handleEdit(record)}>Edit</Button>
          <Button type="danger" style={{ marginLeft: '5px' }} onClick={() => handleDelete(record)}>
            Delete
          </Button>
        </span>
      ),
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
          format="DD/MM/YYYY"
          placeholder={['Start Date', 'End Date']}
        />
        <TimeRangePicker
          value={selectedTimeRange}
          onChange={handleTimeRangeChange}
          style={{ marginRight: '10px' }}
          format="HH:mm"
          placeholder={['Start Time', 'End Time']}
        />
        <Select
          style={{ width: '200px', marginRight: '10px' }}
          placeholder="Select or enter Task Name"
          onChange={handleTaskNameChange}
          value={taskName}
          allowClear
        >
          {taskOptions.map((task) => (
            <Option key={task} value={task}>
              {task}
            </Option>
          ))}
        </Select>
        <Button onClick={handleSchedule} type="primary">
          Schedule Task
        </Button>
      </div>

      <Table dataSource={scheduledTasks} columns={columns} pagination={false} style={{ marginBottom: '20px' }} />

      <Modal
        title="Edit Task"
        visible={modalVisible}
        onCancel={handleCancel}
        footer={[
          <Button key="cancel" onClick={handleCancel}>
            Cancel
          </Button>,
          <Button key="update" type="primary" onClick={handleUpdate}>
            Update
          </Button>,
        ]}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="date" label="Date" rules={[{ required: true, message: 'Please select date!' }]}>
            <DatePicker format="DD/MM/YYYY" />
          </Form.Item>
          <Form.Item name="time" label="Time" rules={[{ required: true, message: 'Please select time!' }]}>
            <TimePicker format="HH:mm" />
          </Form.Item>
          <Form.Item name="task" label="Task" rules={[{ required: true, message: 'Please enter task!' }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default Schedule;
