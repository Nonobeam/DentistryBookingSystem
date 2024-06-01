import React from "react";
import { Form, Input, Button, Checkbox, Typography, DatePicker } from "antd";

const { Title } = Typography;

const Signup = () => {
  const onFinish = (values) => {
    console.log("Success:", values);
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const disabledDate = (current) => {
    // Get today's date and time
    const today = new Date();
    // Disable dates after today
    return current && current > today.setHours(23, 59, 59, 999);
  };

  return (
    <div style={{ display: "flex", height: "100vh" }}>
      <div
        style={{
          flex: 1,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <div style={{ padding: "30px", paddingTop: "70px"}}>
          <Title>Sign Up</Title>
          <Form
            name="signup"
            initialValues={{ remember: true }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            style={{ minWidth: "270px" }}
          >
            <Form.Item
              name="firstName"
              rules={[
                { required: true, message: "Please input your first name!" },
                {
                  pattern: /^[A-Za-z]+$/,
                  message: "First name must contain only letters!",
                },
              ]}
            >
              <Input placeholder="First Name" />
            </Form.Item>

            <Form.Item
              name="lastName"
              rules={[
                { required: true, message: "Please input your last name!" },
                {
                  pattern: /^[A-Za-z]+$/,
                  message: "Last name must contain only letters!",
                },
              ]}
            >
              <Input placeholder="Last Name" />
            </Form.Item>

            <Form.Item
              name="email"
              rules={[
                { required: true, message: "Please input your email!" },
                { type: "email", message: "The input is not valid E-mail!" },
              ]}
            >
              <Input placeholder="Email" />
            </Form.Item>

            <Form.Item
              name="phoneNumber"
              rules={[
                { required: true, message: "Please input your phone number!" },
                {
                  pattern: /^\d{10}$/,
                  message: "Phone number must be 10 digits!",
                },
              ]}
            >
              <Input placeholder="Your phone number" />
            </Form.Item>

            <Form.Item
              name="birthdate"
              rules={[
                { required: true, message: "Please input your birthdate!" },
                { type: "object", message: "Please select a valid date!" },
              ]}
            >
              <DatePicker
                placeholder="Select Date"
                style={{ width: "100%" }}
                format="DD-MM-YYYY" ///////////Date format////////////
                disabledDate={disabledDate}
              />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[
                { required: true, message: "Please input your password!" },
                { min: 8, message: "Password must be at least 8 characters!" },
                // { pattern: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/, message: 'Password must contain at least one letter and one number!' }
              ]}
            >
              <Input.Password placeholder="Password" />
            </Form.Item>

            <Form.Item
              name="confirm"
              valuePropName="checked"
              rules={[
                { required: true, message: "Check this box to continue" },
              ]}
            >
              <Checkbox>I confirm that all my information is correct</Checkbox>
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: "100%" }}
              >
                Sign Up
              </Button>
            </Form.Item>
            <Form.Item>
              <a href="/login">Already have an account?</a>
            </Form.Item>
          </Form>
        </div>
      </div>

      <div style={{ flex: 1, backgroundColor: "#f0f2f5" }}>
        {/* Placeholder for additional content or image */}
      </div>
    </div>
  );
};

export default Signup;
