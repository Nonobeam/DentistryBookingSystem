import React, { useMemo, useState } from 'react';
import {
  Card,
  Table,
  Dropdown,
  Button,
  Modal,
  message,
  Menu,
  Spin,
  Tag,
  DatePicker,
  AutoComplete,
  Input,
} from 'antd';
import { AppointmentHistoryServices } from '../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';
import useSWR from 'swr';
import moment from 'moment';

const { RangePicker } = DatePicker;
import dayjs from "dayjs";


// useEffect(() => {
//   fetchData();
// }, []);

const fetchData = async () => {
  const response = await AppointmentHistoryServices.getAll();
  return response;
};

export const AppointmentHistory = () => {
  const { data, error, isLoading, mutate } = useSWR("appointments", fetchData);
  const [searchText, setSearchText] = useState('');
  const [selectedUser, setSelectedUser] = useState(null);
  const [selectedDentist, setSelectedDentist] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);

  // Extract unique users and dentists for autocomplete options
  const userOptions = useMemo(() => {
    const uniqueUsers = Array.from(new Set(data?.map((item) => item.user)));
    return uniqueUsers.map((user) => ({ value: user }));
  }, [data]);

  const dentistOptions = useMemo(() => {
    const uniqueDentists = Array.from(
      new Set(data?.map((item) => item.dentist))
    );
    return uniqueDentists.map((dentist) => ({ value: dentist }));
  }, [data]);

  const handleUpdateStatus = async (record, status) => {
    try {
      const response = await AppointmentHistoryServices.patchAppointment({
        appointmentId: record.appointmentId,
        status: status,
      });
      message.success(
        `Updated status to ${getStatusName(status)} for treatment ID ${
          record.appointmentId
        }`
      );
      mutate();
    } catch (error) {
      console.error("Error updating status:", error);
    }
  };

  // Function to filter appointments based on current filters
  const filterAppointments = () => {
    let filteredData = data;

    // Filter by searchText
    if (searchText) {
      filteredData = filteredData.filter((item) =>
        item.user.toLowerCase().includes(searchText.toLowerCase())
      );
    }

    // Filter by selectedUser
    if (selectedUser) {
      filteredData = filteredData.filter((item) => item.user === selectedUser);
    }

    // Filter by selectedDentist
    if (selectedDentist) {
      filteredData = filteredData.filter(
        (item) => item.dentist === selectedDentist
      );
    }

    // Filter by selectedDate
    if (selectedDate !== null && selectedDate[0] !== '') {
      console.log(selectedDate[0], selectedDate[1]);
      filteredData = filteredData.filter(
        (item) => item.date >= selectedDate[0] && item.date <= selectedDate[1]
      );
      console.log(filteredData);
    }

    return filteredData;
  };

  const handleDelete = async (record) => {
    Modal.confirm({
      title: "Confirm Delete",
      content: `Are you sure you want to delete treatment ID ${record.appointmentId}?`,
      okText: "Delete",
      okType: "danger",
      cancelText: "Cancel",
      async onOk() {
        try {
          const response =
            await AppointmentHistoryServices.deteleateAppointment({
              appointmentId: record.appointmentId,
            });
          message.success(`Deleted treatment ID ${record.appointmentId}`);
          mutate();
        } catch (error) {
          console.error("Error deleting appointment:", error);
        }
      },
      onCancel() {
        console.log("Cancel");
      },
    });
  };

  const handleMenuClick = (record, e) => {
    const action = e.key;
    if (action === "1" || action === "2") {
      handleUpdateStatus(record, action);
    } else if (action === "delete") {
      handleDelete(record);
    }
  };

  const getStatusName = (status) => {
    switch (parseInt(status)) {
      case 1:
        return <Tag color="blue">Upcoming</Tag>;
      case 0:
        return <Tag color="red">Cancelled</Tag>;
      case 2:
        return <Tag color="green">Finished</Tag>;
      default:
        return <Tag color="gray">Unknown</Tag>;
    }
  };

  const getUserDisplayName = (record) => {
    if (record.dependent) {
      return `Customer: ${record.user} - Dependent: ${record.dependent}`;
    } else {
      return `Customer: ${record.user}`;
    }
  };

  const menu = (record) => {
    const appointmentDateTime = dayjs(`${record.date} ${record.timeSlot}`);
    const currentDateTime = dayjs();

    return (
      <Menu onClick={(e) => handleMenuClick(record, e)}>
        <Menu.Item key="1">Update to Upcoming</Menu.Item>
        {appointmentDateTime.isBefore(currentDateTime) && (
          <Menu.Item key="2">Update to Finished</Menu.Item>
        )}
        <Menu.Item key="delete">Delete</Menu.Item>
      </Menu>
    );
  };

  const columns = [
    {
      title: "Date",
      dataIndex: "date",
      key: "date",
    },
    {
      title: "User",
      dataIndex: "user",
      key: "user",
      render: (text, record) => getUserDisplayName(record),
    },
    {
      title: "TimeSlot",
      dataIndex: "timeSlot",
      key: "timeSlot",
    },
    {
      title: "Dentist",
      dataIndex: "dentist",
      key: "dentist",
    },
    {
      title: "Services",
      dataIndex: "services",
      key: "services",
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (status) => getStatusName(status),
    },
    {
      title: "Actions",
      key: "actions",
      render: (text, record) =>
        record.status !== 0 ? (
          <Dropdown overlay={menu(record)}>
            <Button>Actions</Button>
          </Dropdown>
        ) : null,
    },
  ];

  const cardStyle = {
    marginBottom: "20px",
  };

  const loadingStyle = {
    display: "flex",
    justifyContent: "center",
    alignItems: "middle",
    minHeight: "200px",
  };


  const handleDateChange = (date, dateString) => {
    setSelectedDate(dateString);
  };

  return (
    <div>
      {/* Filters */}
      <div style={{ marginBottom: 16 }}>
        <Input.Search
          placeholder='Search by user'
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          style={{ width: 200, marginRight: 8 }}
        />
        <AutoComplete
          placeholder='Select user'
          value={selectedUser}
          dataSource={userOptions}
          onSelect={(value) => setSelectedUser(value)}
          onSearch={(value) => setSelectedUser(value)}
          style={{ width: 200, marginRight: 8 }}
        />
        <AutoComplete
          placeholder='Select dentist'
          value={selectedDentist}
          dataSource={dentistOptions}
          onSelect={(value) => setSelectedDentist(value)}
          onSearch={(value) => setSelectedDentist(value)}
          style={{ width: 200, marginRight: 8 }}
        />
        <RangePicker
          placeholder={['Start date', 'End date']}
          format='YYYY-MM-DD'
          onChange={handleDateChange}
          style={{ marginRight: 8 }}
        />
      </div>
      <Card title="Appointment History" style={cardStyle}>
        {isLoading ? (
          <div style={loadingStyle}>
            <Spin size="large" />
          </div>
        ) : (
          <Table
            dataSource={filterAppointments()}
            columns={columns}
            pagination={false}
            bordered
            size="small"
          />
        )}
      </Card>
    </div>
  );
};

export default AppointmentHistory;
