import React, { useState, useEffect } from "react";
import axios from "axios";
import { Form, Input, Button, Typography, Select, Radio, DatePicker, Checkbox } from "antd";
import NavBar from "./Nav";

const { Title } = Typography;
const { Option } = Select;

const Booking = () => {
  const [form] = Form.useForm();
  const [patients, setPatients] = useState([]);
  const [selectedFor, setSelectedFor] = useState("self");
  const [isNewPatient, setIsNewPatient] = useState(false);
  const [clinics, setClinics] = useState([]);

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
      }
      
      catch (error) {
        console.error("Failed to fetch clinics:", error);
      }
    };

    fetchClinics();
  }, []);

  const onFinish = (values) => {
    console.log("Success:", values);
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
                <Radio value="self">For Yourself</Radio>
                <Radio value="others">For Others</Radio>
              </Radio.Group>
            </Form.Item>

            {selectedFor === "others" && (
              <Form.Item name="patient">
                <Select placeholder="Patient" onChange={value => setIsNewPatient(value === 'new')}>
                  {patients.map((patient) => (
                    <Option key={patient.id} value={patient.id}>
                      {patient.name} ({patient.dob})
                    </Option>
                  ))}
                  <Option value="new">Create new</Option>
                </Select>
              </Form.Item>
            )}

            {selectedFor === "others" && isNewPatient && (
              <>
                <Form.Item
                  name="firstName"
                  rules={[
                    { required: true, message: "Please input first name!" },
                  ]}
                >
                  <Input placeholder="First Name" />
                </Form.Item>
                <Form.Item
                  name="lastName"
                  rules={[
                    { required: true, message: "Please input last name!" },
                  ]}
                >
                  <Input placeholder="Last Name" />
                </Form.Item>
                <Form.Item
                  name="dob"
                  rules={[
                    { required: true, message: "Please input birthdate!" },
                  ]}
                >
                  <DatePicker
                    placeholder="Patient's birthdate"
                    style={{ width: "100%" }}
                  />
                </Form.Item>
              </>
            )}

            <Form.Item
              name="branch"
              rules={[{ required: true, message: "Please select a branch!" }]}
            >
              <Select placeholder="Choose our branch">
                {clinics.map((clinic) => (
                  <Option key={clinic.clinicID} value={clinic.clinicID}>
                    {clinic.address}
                  </Option>
                ))}
              </Select>
            </Form.Item>

            <Form.Item
              name="date"
              rules={[{ required: true, message: "Please choose a date!" }]}
            >
              <DatePicker placeholder="Select Date"
                style={{ width: "100%" }}
                format="YYYY-MM-DD" ///////////Date format////////////
                disabledDate={disabledDate} />
            </Form.Item>

            <Form.Item
              name="time"
              rules={[{ required: true, message: "Please choose a time slot!" }]}
            >
              <Select placeholder="Choose timeslot">
                <Option value="9:00">9:00 AM</Option>
                <Option value="10:00">10:00 AM</Option>
              </Select>
            </Form.Item>

            <Form.Item
              name="service"
              rules={[{ required: true, message: "Please select a service!" }]}
            >
              <Select placeholder="Choose service">
                <Option value="service1">Service 1</Option>
                <Option value="service2">Service 2</Option>
              </Select>
            </Form.Item>

            <Form.Item name="dentist">
              <Select placeholder="Choose dentist (Optional)">
                <Option value="dentist1">Dentist 1</Option>
                <Option value="dentist2">Dentist 2</Option>
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
    </>
  );
};

export default Booking;
