import React, { useEffect, useState } from "react";
import axios from "axios";
import dayjs from "dayjs";
import {
  Form,
  Input,
  Button,
  Typography,
  Select,
  Checkbox,
  DatePicker,
  Modal,
  Radio,
  Skeleton,
} from "antd";
import { useNavigate } from "react-router-dom";
import NavBar from "./Nav";
import styled from "styled-components";
import { IoAddCircleOutline } from "react-icons/io5";
import {
  UserOutlined,
  HomeOutlined,
  CalendarOutlined,
  AppstoreOutlined,
  ClockCircleOutlined,
  UserSwitchOutlined,
} from "@ant-design/icons";

const { Title } = Typography;
const { Option } = Select;

const BookingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
`;

const BookingFormWrapper = styled.div`
  width: 450px;
  background-color: #fcfdff;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 5px 10px 18px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
`;

const Booking = () => {
  const [form] = Form.useForm();
  const [clinics, setClinics] = useState([]);
  const [services, setServices] = useState([]);
  const [timeSlots, setTimeSlots] = useState([]);
  const [dentists, setDentists] = useState([]);
  const [patients, setPatients] = useState([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [newDependentName, setNewDependentName] = useState("");
  const [newDependentBirthday, setNewDependentBirthday] = useState(null);

  const [loadingClinics, setLoadingClinics] = useState(true);
  const [loadingServices, setLoadingServices] = useState(false);
  const [loadingTimeSlots, setLoadingTimeSlots] = useState(false);
  const [loadingPatients, setLoadingPatients] = useState(false);
  const [loadingForm, setLoadingForm] = useState(false);

  const [selectedBranch, setSelectedBranch] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedFor, setSelectedFor] = useState("self");
  const [selectedService, setSelectedService] = useState(null);
  const [selectedTimeSlot, setSelectedTimeSlot] = useState(null);
  const [selectedDentist, setSelectedDentist] = useState("random");
  const [selectedDependant, setSelectedDependant] = useState(null);

  const [isFormDisabled, setIsFormDisabled] = useState(false);
  const [isDateDisabled, setIsDateDisabled] = useState(true);
  const [isServiceDisabled, setIsServiceDisabled] = useState(true);
  const [isDentistDisabled, setIsDentistDisabled] = useState(true);
  const [isTimeSlotDisabled, setIsTimeSlotDisabled] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const checkBookingLimit = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get('http://localhost:8080/user/booking', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data) {
          setIsFormDisabled(true);
          Modal.error({
            title: "Booking Limit Exceeded",
            content: "You have reached the maximum number of bookings allowed (5/5). Please cancel the existing booking(s) before making a new one.",
          });
        }
      } catch (error) {
        console.error('Error fetching booking limit:', error);
      }
    };

    checkBookingLimit();
  }, []);

  useEffect(() => {
    const fetchClinics = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          "http://localhost:8080/user/all-clinic",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setClinics(response.data);
      } catch (error) {
        console.error("Failed to fetch clinics:", error);
      } finally {
        setLoadingClinics(false);
      }
    };
    fetchClinics();
  }, []);

  useEffect(() => {
    const fetchServices = async () => {
      if (selectedBranch && selectedDate) {
        setLoadingServices(true);
        try {
          const token = localStorage.getItem("token");
          const formattedDate = selectedDate.format("YYYY-MM-DD");
          const response = await axios.get(
            `http://localhost:8080/user/all-service/${selectedBranch}?bookDate=${formattedDate}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          const servicesArray = Object.values(response.data); // Transform the response object into an array
          setServices(servicesArray);
          setIsServiceDisabled(false);
        } catch (error) {
          console.error("Failed to fetch services:", error);
        } finally {
          setLoadingServices(false);
        }
      }
    };
    fetchServices();
  }, [selectedBranch, selectedDate]);

  useEffect(() => {
    const fetchTimeSlots = async () => {
      if (selectedBranch && selectedDate && selectedService) {
        setLoadingTimeSlots(true);
        try {
          const token = localStorage.getItem("token");
          const formattedDate = selectedDate.format("YYYY-MM-DD");
          const response = await axios.get(
            `http://localhost:8080/user/${selectedBranch}/available-schedules?workDate=${formattedDate}&servicesId=${selectedService}`,
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          const schedules = response.data;
          const timeSlotsMap = new Map();
          schedules.forEach((schedule) => {
            if (!timeSlotsMap.has(schedule.startTime+'-'+schedule.endTime)) {
              timeSlotsMap.set(schedule.startTime+'-'+schedule.endTime, []);
            }
            timeSlotsMap.get(schedule.startTime+'-'+schedule.endTime).push({
              dentistName: schedule.dentistName,
              dentistScheduleID: schedule.dentistScheduleID,
            });
          });
          setTimeSlots(
            Array.from(timeSlotsMap, ([time, dentists]) => ({ time, dentists }))
          );
          setIsTimeSlotDisabled(false);
        } catch (error) {
          console.error("Failed to fetch time slots:", error);
        } finally {
          setLoadingTimeSlots(false);
        }
      }
    };
    fetchTimeSlots();
  }, [selectedBranch, selectedDate, selectedService]);

  useEffect(() => {
    if (selectedFor === "others") {
      const fetchPatients = async () => {
        setLoadingPatients(true);
        try {
          const token = localStorage.getItem("token");
          const response = await axios.get(
            "http://localhost:8080/user/dependentList",
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          );
          setPatients(response.data);
        } catch (error) {
          console.error("Failed to fetch patients:", error);
        } finally {
          setLoadingPatients(false);
        }
      };
      fetchPatients();
    }
  }, [selectedFor]);

  const handleBranchChange = (value) => {
    setSelectedBranch(value);
    setSelectedService(null);
    setSelectedTimeSlot(null);
    setSelectedDentist(null);

    setServices([]);
    setTimeSlots([]);
    setDentists([]);

    setIsDateDisabled(false);
    setIsTimeSlotDisabled(true);
    setIsDentistDisabled(true);
    setIsServiceDisabled(true);

    form.setFieldsValue({ service: null, time: null, dentist: null });
  };

  const handleDateChange = (date) => {
    setSelectedDate(date);
    setSelectedService(null);
    setSelectedTimeSlot(null);
    setSelectedDentist(null);

    setServices([]);
    setTimeSlots([]);
    setDentists([]);

    form.setFieldsValue({ service: null, time: null, dentist: null });
    setIsTimeSlotDisabled(true);
    setIsDentistDisabled(true);
  };

  const handleServiceChange = (value) => {
    setSelectedService(value);
    setSelectedTimeSlot(null);
    setSelectedDentist(null);

    setTimeSlots([]);
    setDentists([]);

    form.setFieldsValue({ time: null, dentist: null });
    setIsDentistDisabled(true);
  };

  const handleDependantChange = (value) => {
    setSelectedDependant(value);
  };

  const handleTimeSlotChange = (time) => {
    const selectedSlot = timeSlots.find((slot) => slot.time === time);
    setSelectedTimeSlot(time);
    setSelectedDentist(null);

    setDentists([]);

    setDentists(selectedSlot.dentists);

    form.setFieldsValue({ dentist: null });
    setIsDentistDisabled(false);
  };

  const handleDentistChange = (value) => {
    setSelectedDentist(value);
  };

  const handleNewDependent = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/user/dependentNew",
        {
          name: newDependentName,
          birthday: newDependentBirthday.format("YYYY-MM-DD"),
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        Modal.success({
          title: "Dependent Added",
          content: "The new dependent has been added successfully.",
          onOk: () => {
            setModalVisible(false);
            window.location.reload();
          },
        });
      }
    } catch (error) {
      console.error("Failed to add dependent:", error);
      Modal.error({
        title: "Add Dependent Failed",
        content:
          "There was an issue adding the dependent. Please try again later.",
      });
    }
  };

  const onFinish = async (values) => {
    setLoadingForm(true);
    try {
      const token = localStorage.getItem("token");
      const selectedSlot = timeSlots.find(
        (slot) => slot.time === selectedTimeSlot
      );
      const selectedDentistObj = selectedSlot.dentists.find(
        (dentist) => dentist.dentistName === selectedDentist
      );
      const dentistScheduleID =
        selectedDentist === "random"
          ? selectedSlot.dentists[
            Math.floor(Math.random() * selectedSlot.dentists.length)
          ].dentistScheduleID
          : selectedDentistObj.dentistScheduleID;
      const url =
        `http://localhost:8080/user/booking/${dentistScheduleID}?serviceId=${selectedService}` +
        (selectedFor === "others" ? `&dependentID=${selectedDependant}` : "");

      const response = await axios.post(
        url,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        Modal.success({
          title: "Booking Successful",
          content: "Your booking has been reserved.",
          onOk: () => navigate("/History"),
        });
      }
    } catch (error) {
      console.error("Booking failed:", error);
      Modal.error({
        title: "Booking Failed",
        content:
          error.response?.data?.message ||
          "An error occurred. Please try again later.",
        onOk: () => window.location.reload(),
      });
    }
    finally {
      setLoadingForm(false);
    }
  };

  const disabledDate = (current) => {
    const today = new Date();
    const maxDate = new Date(today);
    maxDate.setMonth(maxDate.getMonth() + 2);

    return (
      current < today.setHours(0, 0, 0, 0) ||
      current > maxDate.setHours(23, 59, 59, 999)
    );
  };

  const disableDatesAfterToday = (current) => {
    const today = new Date();
    today.setHours(23, 59, 59, 999);
    return current > today;
  };

  return (
    <>
      <NavBar />
      <BookingContainer>
        <BookingFormWrapper>
          <Title level={2}>Reserve your appointment!</Title>
          <Form form={form} name="booking" onFinish={onFinish} disabled={isFormDisabled} layout="vertical">
            <Form.Item name="forWhom" initialValue="self">
              <Radio.Group onChange={(e) => setSelectedFor(e.target.value)}>
                <Radio.Button value="self">For yourself</Radio.Button>
                <Radio.Button value="others">For others</Radio.Button>
              </Radio.Group>
            </Form.Item>

            {selectedFor === "others" && (
        <>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <div style={{ padding: '4px' }}>
            <span style={{color: 'red'}}>*</span><UserOutlined style={{ marginLeft: 4, marginRight: 8 }} />
              Patient:
            </div>
            <Button type="link" onClick={() => setModalVisible(true)}>
              <IoAddCircleOutline /> Create new
            </Button>
          </div>
          <Form.Item
            name="patient"
            rules={[
              { required: true, message: "Please select a patient!" },
            ]}
          >
            {loadingPatients ? (
              <Skeleton.Input style={{ width: '244%' }} active />
            ) : (
              <Select
                placeholder="Select patient"
                onChange={handleDependantChange}
                notFoundContent="You don't have any dependent."
              >
                {patients.map((patient) => (
                  <Option
                    key={patient.dependentID}
                    value={patient.dependentID}
                  >
                    {patient.name} ({dayjs(patient.birthday).format('DD-MM-YYYY')})
                  </Option>
                ))}
              </Select>
            )}
          </Form.Item>
        </>
      )}

            <Form.Item
              name="clinic"
              rules={[{ required: true, message: "Please select a branch!" }]}
              label={
                <div style={{ padding: '4px' }}>
                  <HomeOutlined style={{ marginRight: 8 }} />
                  Branch:
                </div>}
            >
              {loadingClinics ? (
                <Skeleton.Input style={{ width: '244%' }} active />
              ) : (
                <Select
                  loading={loadingClinics}
                  placeholder="Choose branch"
                  onChange={handleBranchChange}
                  dropdownRender={(menu) => (
                    <>
                      {menu}
                    </>
                  )}
                >
                  {clinics.map((clinic) => (
                    <Option key={clinic.clinicID} value={clinic.clinicID}>
                      {clinic.name} ({clinic.address})
                    </Option>
                  ))}

                </Select>
              )}
            </Form.Item>


            <Form.Item
              name="date"
              rules={[{ required: true, message: "Please choose a date!" }]}
              label={
                <div style={{ padding: '4px' }}>
                  <CalendarOutlined style={{ marginRight: 8 }} />
                  Date:
                </div>
              }
            >
              <DatePicker
                placeholder="Select Date"
                style={{ width: "100%" }}
                onChange={handleDateChange}
                format="DD-MM-YYYY"
                disabledDate={disabledDate}
                disabled={isDateDisabled}

              />
            </Form.Item>


            <Form.Item
              name="service"
              rules={[{ required: true, message: "Please select a service!" }]}
              label={<div style={{ padding: '4px' }}>
                <AppstoreOutlined style={{ marginRight: 8 }} />
                Service:
              </div>}
            >
              {loadingServices ? (
                <Skeleton.Input style={{ width: '244%' }} active />
              ) : (
                <Select
                  placeholder="Choose service"
                  disabled={isServiceDisabled}
                  onChange={handleServiceChange}
                  notFoundContent="No services available on this date."
                  dropdownRender={(menu) => (
                    <>

                      {menu}
                    </>
                  )}
                >


                  {services.map((service) => (
                    <Option key={service.serviceID} value={service.serviceID}>
                      {service.name}
                    </Option>
                  ))}
                  )

                </Select>
              )}
            </Form.Item>


            <Form.Item
              name="time"
              rules={[
                { required: true, message: "Please choose a time slot!" },
              ]}
              label={
                <div style={{ padding: '4px' }}>
                  <ClockCircleOutlined style={{ marginRight: 8 }} />
                  Time Slot:
                </div>}
            >
              {loadingTimeSlots ? (
                <Skeleton.Input style={{ width: '244%' }} active />
              ) : (
                <Select
                  placeholder="Choose timeslot"
                  disabled={isTimeSlotDisabled}
                  onChange={handleTimeSlotChange}
                  dropdownRender={(menu) => (
                    <>

                      {menu}
                    </>
                  )}
                >

                  {timeSlots.map((slot, index) => (
                    <Option key={index} value={slot.time}>
                      {slot.time}
                    </Option>
                  ))}

                </Select>
              )}
            </Form.Item>


            <Form.Item name="dentist"
              label={<div style={{ padding: '4px', display: 'flex', alignItems: 'center' }}>
                <UserSwitchOutlined style={{ marginRight: 8 }} />
                Dentist:
              </div>}
              rules={[
                { required: true, message: "Please choose a Dentist!" },
              ]}>
              <Select
                placeholder="Choose dentist"
                disabled={isDentistDisabled}
                onChange={handleDentistChange}
                dropdownRender={(menu) => (
                  <>

                    {menu}
                  </>
                )}
              >
                {/* <Option value="random">Random</Option> */}
                {dentists.map((dentist, index) => (
                  <Option key={index} value={dentist.dentistName}>
                    {dentist.dentistName}
                  </Option>
                ))}
              </Select>
            </Form.Item>

            <Form.Item
              name="agreement"
              valuePropName="checked"
              rules={[
                {
                  validator: (_, value) =>
                    value
                      ? Promise.resolve()
                      : Promise.reject("Should accept agreement"),
                },
              ]}
            >
              <Checkbox>I've checked everything and it cannot be change.</Checkbox>
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: "100%" }}
                loading={loadingForm}
              >
                Book your appointment
              </Button>
            </Form.Item>
          </Form>
        </BookingFormWrapper>
      </BookingContainer>

      <Modal
        title="Create New Dependent"
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={[
          <Button key="back" onClick={() => setModalVisible(false)}>
            Cancel
          </Button>,
          <Button key="submit" type="primary" onClick={handleNewDependent}>
            Submit
          </Button>,
        ]}
      >
        <Form layout="vertical">
          <Form.Item label="Name" required>
            <Input
              value={newDependentName}
              onChange={(e) => setNewDependentName(e.target.value)}
            />
          </Form.Item>
          <Form.Item label="Birthday" required>
            <DatePicker
              style={{ width: "100%" }}
              disabledDate={disableDatesAfterToday}
              onChange={(date) => setNewDependentBirthday(date)}
              format="DD-MM-YYYY"
            />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default Booking;
