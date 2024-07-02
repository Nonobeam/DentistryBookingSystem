import React, { useState, useEffect } from "react";
import axios from "axios";
import { Form, Input, Button, Typography, Select, Radio, DatePicker, Checkbox, Spin, Modal } from "antd";
import { useNavigate } from "react-router-dom"; // Import useHistory for redirection
import NavBar from "./Nav";

const { Title } = Typography;
const { Option } = Select;

const Booking = () => {
  const [form] = Form.useForm();
  const [patients, setPatients] = useState([]);
  const [selectedFor, setSelectedFor] = useState("self");
  const [clinics, setClinics] = useState([]);
  const [services, setServices] = useState([]);
  const [selectedBranch, setSelectedBranch] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const [isDateDisabled, setIsDateDisabled] = useState(true);
  const [isServiceDisabled, setIsServiceDisabled] = useState(true);
  const [loadingClinics, setLoadingClinics] = useState(true);
  const [loadingServices, setLoadingServices] = useState(false);
  const [timeSlots, setTimeSlots] = useState([]);
  const [selectedService, setSelectedService] = useState(null);
  const [dentists, setDentists] = useState([]);
  const [loadingTimeSlots, setLoadingTimeSlots] = useState(false);
  const [selectedTimeSlot, setSelectedTimeSlot] = useState(null);
  const [selectedDentist, setSelectedDentist] = useState("random");
  const [selectedDependant, setSelectedDependant] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [newDependentName, setNewDependentName] = useState("");
  const [newDependentBirthday, setNewDependentBirthday] = useState(null);
  const [loadingPatients, setLoadingPatients] = useState(false);
  const history = useNavigate();

  useEffect(() => {
    const fetchClinics = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/user/all-clinic", {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
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
          const formattedDate = selectedDate.format('YYYY-MM-DD');
          const response = await axios.get(`http://localhost:8080/user/all-service/${selectedBranch}?bookDate=${formattedDate}`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
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

  const fetchTimeSlots = async () => {
    if (selectedBranch && selectedDate && selectedService) {
      setLoadingTimeSlots(true);
      try {
        const token = localStorage.getItem("token");
        const formattedDate = selectedDate.format('YYYY-MM-DD');
        const response = await axios.get(`http://localhost:8080/user/${selectedBranch}/available-schedules?workDate=${formattedDate}&servicesId=${selectedService}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });

        const schedules = response.data;
        const timeSlotsMap = new Map();

        schedules.forEach(schedule => {
          if (!timeSlotsMap.has(schedule.startTime)) {
            timeSlotsMap.set(schedule.startTime, []);
          }
          timeSlotsMap.get(schedule.startTime).push({ dentistName: schedule.dentistName, dentistScheduleID: schedule.dentistScheduleID });
        });

        const timeSlotsArray = Array.from(timeSlotsMap, ([time, dentists]) => ({ time, dentists }));
        setTimeSlots(timeSlotsArray);
      } catch (error) {
        console.error("Failed to fetch time slots:", error);
      } finally {
        setLoadingTimeSlots(false);
      }
    }
  };

  useEffect(() => {
    fetchTimeSlots();
  }, [selectedBranch, selectedDate, selectedService]);

  const fetchPatients = async () => {
    setLoadingPatients(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8080/user/dependentList", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setPatients(response.data);
    } catch (error) {
      console.error("Failed to fetch patients:", error);
    } finally {
      setLoadingPatients(false);
    }
  };

  useEffect(() => {
    if (selectedFor === "others") {
      fetchPatients();
    }
  }, [selectedFor]);

  const handleBranchChange = (value) => {
    setSelectedBranch(value);
    setIsDateDisabled(false); // Enable the date picker after selecting a branch
    setIsServiceDisabled(true); // Disable the service dropdown until a new date is selected
    setSelectedService(null); // Reset the selected service
    setServices([]);
    setTimeSlots([]);
  };

  const handleDateChange = (date) => {
    setSelectedDate(date);
    setSelectedService(null); // Reset the selected service
    setTimeSlots([]);
  };

  const handleServiceChange = (value) => {
    setSelectedService(value);
  };

  const handleDependantChange = (value) => {
    setSelectedDependant(value);
  };

  const handleTimeSlotChange = (time) => {
    const selectedSlot = timeSlots.find(slot => slot.time === time);
    setDentists(selectedSlot.dentists);
    setSelectedTimeSlot(time);
    setSelectedDentist("random");
  };

  const handleDentistChange = (value) => {
    setSelectedDentist(value);
  };

  const handleNewDependent = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post("http://localhost:8080/user/dependentNew", {
        name: newDependentName,
        birthday: newDependentBirthday.format('YYYY-MM-DD')
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      if (response.status === 200) {
        Modal.success({
          title: "Dependent Added",
          content: "The new dependent has been added successfully.",
          onOk: () => {
            setModalVisible(false);
            fetchPatients(); // Refresh the patient list
          }
        });
      }
    } catch (error) {
      console.error("Failed to add dependent:", error);
      Modal.error({
        title: "Add Dependent Failed",
        content: "There was an issue adding the dependent. Please try again later."
      });
    }
  };

  const onFinish = async (values) => {
    try {
      const token = localStorage.getItem("token");
      const selectedSlot = timeSlots.find(slot => slot.time === selectedTimeSlot);
      const selectedDentistObj = selectedSlot.dentists.find(dentist => dentist.dentistName === selectedDentist);
      const dentistScheduleID = selectedDentist === "random"
        ? selectedSlot.dentists[Math.floor(Math.random() * selectedSlot.dentists.length)].dentistScheduleID
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
          // onOk: () => history("/history"),
        });
      }
    } catch (error) {
      console.error("Booking failed:", error);
      Modal.error({
        title: "Booking Failed",
        content: error.response?.data?.message || "An error occurred. Please try again later.",
        onOk: () => window.location.reload(),
      });
    }
  };

  const disabledDate = (current) => {
    const today = new Date();
    return current < today.setHours(23, 59, 59, 999);
  };

  return (
    <>
      <NavBar />
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "90vh",
        }}
      >
        <div
          style={{
            width: 450,
            backgroundColor: "ghostwhite",
            padding: "30px",
            borderRadius: "20px",
          }}
        >
          <Title level={2}>Reserve your appointment!</Title>

          <Form form={form} name="booking" onFinish={onFinish}>
            <Form.Item name="forWhom" initialValue="self">
              <Radio.Group onChange={(e) => setSelectedFor(e.target.value)}>
                <Radio.Button value="self">For yourself</Radio.Button>
                <Radio.Button value="others">For others</Radio.Button>
              </Radio.Group>
            </Form.Item>

            {selectedFor === "others" && (
              <>
                <Form.Item
                  name="patient"
                  rules={[{ required: true, message: "Please select a patient!" }]}
                >
                  {loadingPatients ? (
                    <Spin size="small" />
                  ) : (
                    <Select placeholder="Select patient" onChange={handleDependantChange} >
                      {patients.map((patient) => (
                        <Option key={patient.dependentID} value={patient.dependentID}>
                          {patient.name} ({patient.birthday})
                        </Option>
                      ))}
                    </Select>
                  )}
                </Form.Item>
                <Button type="link" onClick={() => setModalVisible(true)}>
                  Create new
                </Button>
              </>
            )}
            
            <Form.Item
              name="clinic"
              rules={[{ required: true, message: "Please select a branch!" }]}
            >
              {loadingClinics ? (
                <Spin size="small" />
              ) : (
                <Select placeholder="Choose branch" onChange={handleBranchChange}>
                  {clinics.map((clinic) => (
                    <Option key={clinic.clinicID} value={clinic.clinicID}>
                      {clinic.name}
                    </Option>
                  ))}
                </Select>
              )}
            </Form.Item>

            <Form.Item
              name="date"
              rules={[{ required: true, message: "Please choose a date!" }]}
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
            >
              {loadingServices ? (
                <Spin size="small" />
              ) : (
                <Select placeholder="Choose service" disabled={isServiceDisabled} onChange={handleServiceChange}>
                  {services.map((service) => (
                    <Option key={service.serviceID} value={service.serviceID}>
                      {service.name}
                    </Option>
                  ))}
                </Select>
              )}
            </Form.Item>

            <Form.Item
              name="time"
              rules={[{ required: true, message: "Please choose a time slot!" }]}
            >
              {loadingTimeSlots ? (
                <Spin size="small" />
              ) : (
                <Select placeholder="Choose timeslot" onChange={handleTimeSlotChange}>
                  {timeSlots.map((slot, index) => (
                    <Option key={index} value={slot.time}>
                      {slot.time}
                    </Option>
                  ))}
                </Select>
              )}
            </Form.Item>

            {dentists.length > 0 && (
              <Form.Item name="dentist" initialValue="random">
                <Select placeholder="Choose dentist" onChange={handleDentistChange}>
                  <Option value="random">Random</Option>
                  {dentists.map((dentist, index) => (
                    <Option key={index} value={dentist.dentistName}>
                      {dentist.dentistName}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            )}

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
              <Checkbox>I have checked everything before submit</Checkbox>
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: "100%" }}
              >
                Book your appointment
              </Button>
            </Form.Item>
          </Form>
        </div>
      </div>

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
            <Input value={newDependentName} onChange={(e) => setNewDependentName(e.target.value)} />
          </Form.Item>
          <Form.Item label="Birthday" required>
            <DatePicker
              style={{ width: "100%" }}
              onChange={(date) => setNewDependentBirthday(date)}
              format="YYYY-MM-DD"
            />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
};

export default Booking;
