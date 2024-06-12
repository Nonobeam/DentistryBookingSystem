import React, { useState, useEffect } from "react";
import {
  Form,
  Input,
  Button,
  Typography,
  Select,
  Radio,
  DatePicker,
  Checkbox,
} from "antd";
import NavBar from "./Nav";
const { Title } = Typography;
const { Option } = Select;

const Booking = () => {
  const [form] = Form.useForm();
  const [patients, setPatients] = useState([]);
  const [selectedFor, setSelectedFor] = useState("self");

  useEffect(() => {
    // Fetch patients data
    fetch("http://localhost:5000/patients")
      .then((response) => response.json())
      .then((data) => setPatients(data));
  }, []);

  const onFinish = (values) => {
    console.log("Success:", values);
    // Here you can make a POST request to your backend to save the booking
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

            {/* If choose for others person */}
            {selectedFor === "others" && (
              <Form.Item name="patient">
                <Select placeholder="Patient">
                  {patients.map((patient) => (
                    <Option key={patient.id} value={patient.id}>
                      {patient.name} ({patient.dob})
                    </Option>
                    
                  ))}
                  <Option value="patient1">Patien1</Option>
                  <Option value="new">Create new</Option>
                </Select>
              </Form.Item>
            )}

            {/* If choose for others person AND NEW */}
            {selectedFor === "others" &&
              form.getFieldValue("patient") === "new" && (
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

            {/* Others */}
            <Form.Item
              name="branch"
              rules={[{ required: true, message: "Please select a branch!" }]}
            >
              <Select placeholder="Choose our branch">
                <Option value="branch1">Branch 1</Option>
                <Option value="branch2">Branch 2</Option>
              </Select>
            </Form.Item>

            <Form.Item
              name="date"
              rules={[{ required: true, message: "Please choose a date!" }]}
            >
              <DatePicker placeholder="Choose date" style={{ width: "100%" }} />
            </Form.Item>

            <Form.Item
              name="time"
              rules={[
                { required: true, message: "Please choose a time slot!" },
              ]}
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
