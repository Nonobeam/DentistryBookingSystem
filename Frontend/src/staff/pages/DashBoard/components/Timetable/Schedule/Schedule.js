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
  import dayjs from 'dayjs';
  import { Link } from 'react-router-dom';
  import { LeftOutlined } from '@ant-design/icons'; 
  import TimetableServices from '../../../../../services/TimetableServices/TimetableServices';

  const { RangePicker: DateRangePicker } = DatePicker;
  const { Option } = Select;

  const Schedule = () => {
    const [selectedDateRange, setSelectedDateRange] = useState([]);
    const [selectedTimeRange, setSelectedTimeRange] = useState(undefined);
    const [timeSlotList, setTimeSlotList] = useState([]);
    const [dentistName, setDentistName] = useState("");
    const [modalVisible, setModalVisible] = useState(false);
    const [form] = Form.useForm();
    const [dentistList, setDentistList] = useState([]);
    const [dentistListSchedule, setDentistListSchedule] = useState([]);
    const [loadingDentistList, setLoadingDentistList] = useState(false);
    const [loadingTimeSlotList, setLoadingTimeSlotList] = useState(false);

    const fetchDentistList = async () => {
      try {
        setLoadingDentistList(true);
        const dentistsList = await TimetableServices.getAllDentists();
        setDentistList(dentistsList);
      } catch (error) {
        console.error("Error fetching dentist list:", error);
      } finally {
        setLoadingDentistList(false);
      }
    };

    const fetchDentistListAndTimeSlot = async () => {
      try {
        setLoadingDentistList(true);
        setLoadingTimeSlotList(true);
        const startDate = selectedDateRange[0].format("YYYY-MM-DD");
        const endDate = selectedDateRange[1].format("YYYY-MM-DD");
        const dentistsList = await TimetableServices.getAllDentistsAndTimeSlot(startDate, endDate);
        setDentistListSchedule(dentistsList.dentistList);
        setTimeSlotList(dentistsList.timeSlotList);
      } catch (error) {
        console.error("Error fetching dentist list:", error);
      } finally {
        setLoadingDentistList(false);
        setLoadingTimeSlotList(false);
      }
    };

    useEffect(() => {
      fetchDentistList();
    }, []);

    useEffect(() => {
      if (selectedDateRange?.length === 2) {
        fetchDentistListAndTimeSlot();
      }
    }, [selectedDateRange]);

    const handleDateRangeChange = (dates) => {
      setSelectedDateRange(dates);
    };

    const handleTimeRangeChange = (times) => {
      setSelectedTimeRange(times);
    };

    const handleDentistChange = (value) => {
      setDentistName(value);
    };

    const handleSchedule = async () => {
      if (!selectedDateRange || selectedDateRange.length !== 2 || !selectedTimeRange || !dentistName) {
        alert("Please select date range, time range, and enter/select task name.");
        return;
      }

      try {
        const response = await TimetableServices.setSchedule({
          dentistMail: dentistName,
          startDate: selectedDateRange[0].format("YYYY-MM-DD"),
          endDate: selectedDateRange[1].format("YYYY-MM-DD"),
          slotNumber: selectedTimeRange,
        });
        if (response) {
          notification.success({
            message: "Schedule Successfully",
            description: "Your schedule has been set successfully.",
          });
          setModalVisible(false);
          form.resetFields();
        }
      } catch (error) {
        notification.error({
          message: "Failed to set schedule",
          description: error.message,
        });
      }

      setSelectedDateRange([]);
      setSelectedTimeRange(undefined);
      setDentistName("");
    };

    const handleCancel = () => {
      setModalVisible(false);
      form.resetFields();
    };

    const handleDelete = async (record) => {
      const { scheduleID } = record;

      try {
        const response = await TimetableServices.deleteSchedule(scheduleID);
        if (response.code === "400") {
          notification.error({
            message: response.message,
          });
        } else {
          setDentistList(dentistList.filter((item) => item.scheduleID !== scheduleID));
          notification.success({
            message: "Delete Successful",
            description: "The schedule has been deleted successfully.",
          });
        }
      } catch (error) {
        notification.error({
          message: "Failed to delete schedule",
          description: error.message,
        });
      }
    };

    const columns = [
      {
        title: "Name",
        dataIndex: "dentist",
        key: "name",
        render: (dentist) => dentist.user?.name,
      },
      {
        title: "Mail",
        dataIndex: "dentist",
        key: "mail",
        render: (dentist) => dentist.user?.mail,
      },
      {
        title: "Phone",
        dataIndex: "dentist",
        key: "phone",
        render: (dentist) => dentist.user?.phone,
      },
      {
        title: "Work Date",
        dataIndex: "workDate",
        key: "workDate",
      },
      {
        title: "Time Slot",
        dataIndex: "timeslot",
        key: "timeslot",
        render: (timeslot) =>
          `${timeslot.startTime} - slot number: ${timeslot.slotNumber}`,
      },
      {
        title: "Action",
        key: "action",
        render: (text, record) => (
          <Button type="link" danger onClick={() => handleDelete(record)}>
            Delete
          </Button>
        ),
      },
    ];

    const disabledDate = (current) => {
      // Allow dates up to two months from today
      return (
        current &&
        (current < dayjs().startOf("day") ||
          current > dayjs().add(2, "months").endOf("day"))
      );
    };

    return (
      <div style={{ padding: "20px" }}>
        <h1
          style={{
            fontSize: "40px",
            color: "white",
            fontWeight: "bold",
            textAlign: "center",
            alignItems: "center",
            border: "2px solid #ccc", // Đường viền 2px, màu xám nhạt
            padding: "10px", // Khoảng cách nội bộ 10px
            borderRadius: "5px",
            background: "#1976d2",
            height: "100px",
            lineHeight: "80px",
            // Đường viền cong góc 5px
          }}
        >
          Schedule Dentist's Schedule
        </h1>
        <Link
          to="/staff/timetable"
          style={{ marginBottom: "20px", display: "block" }}
        >
          <Button type="primary" icon={<LeftOutlined />}>
            Back to Timetable
          </Button>
        </Link>
        <div style={{ marginBottom: "20px" }}>
          <DateRangePicker
            value={selectedDateRange}
            onChange={handleDateRangeChange}
            style={{ marginRight: "10px" }}
            format="DD/MM/YYYY"
            placeholder={["Start Date", "End Date"]}
            disabledDate={disabledDate}
            loading={loadingDentistList || loadingTimeSlotList} // Thêm loading ở đây
          />
          <Select
            style={{ width: "200px", marginRight: "10px" }}
            placeholder="Select or Name"
            onChange={handleDentistChange}
            allowClear
            loading={loadingDentistList} // Thêm loading ở đây
          >
            {dentistListSchedule.map((dentist) => (
              <Option key={dentist.mail} value={dentist.mail}>
                {dentist.name}
              </Option>
            ))}
          </Select>
          <Select
            style={{ width: "200px", marginRight: "10px" }}
            placeholder="Select or enter Task Name"
            onChange={handleTimeRangeChange}
            allowClear
            loading={loadingTimeSlotList} // Thêm loading ở đây
          >
            {timeSlotList.map((timeSlot, index) => (
              <Option
                key={timeSlot.slotNumber + index}
                value={timeSlot.slotNumber}
              >
                {timeSlot.startTime}
              </Option>
            ))}
          </Select>
          <Button onClick={handleSchedule} type="primary">
            Schedule Task
          </Button>
        </div>

        <Table
          dataSource={dentistList}
          columns={columns}
          style={{ marginBottom: "20px" }}
        />

        <Modal
          title="Edit Task"
          visible={modalVisible}
          onCancel={handleCancel}
          footer={[
            <Button key="cancel" onClick={handleCancel}>
              Cancel
            </Button>,
          ]}
        >
          <Form form={form} layout="vertical">
            <Form.Item
              name="date"
              label="Date"
              rules={[{ required: true, message: "Please select date!" }]}
            >
              <DatePicker format="DD/MM/YYYY" />
            </Form.Item>
            <Form.Item
              name="time"
              label="Time"
              rules={[{ required: true, message: "Please select time!" }]}
            >
              <TimePicker format="HH:mm" />
            </Form.Item>
            <Form.Item
              name="task"
              label="Task"
              rules={[{ required: true, message: "Please enter task!" }]}
            >
              <Input />
            </Form.Item>
          </Form>
        </Modal>
      </div>
    );
  };

  export default Schedule;
