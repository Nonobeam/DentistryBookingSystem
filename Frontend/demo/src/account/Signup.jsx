import React, { useState } from "react";
import axios from "axios";
import { Form, Input, Button, Checkbox, Typography, DatePicker, Alert } from "antd";

const { Title } = Typography;

const API_URL = "http://localhost:8080/api/v1/auth/register";

const Signup = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const onFinish = async (values) => {
    console.log("Success:", values);
    try {
      const response = await axios.post(API_URL, {
        name: values.firstName,
        phone: values.phoneNumber,
        mail: values.email,
        password: values.password,
        birthday: values.birthdate.format("YYYY-MM-DD"),
      });

      if (response.status === 200) { 
        setErrorMessage(""); 
        setSuccessMessage("Registration successful. Please check your email to confirm your account. It may in your spam folder.");
      }
    } catch (error) {
      setErrorMessage("Your email or phone is already registered. Please login or use a different email or phone number.");
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const disabledDate = (current) => {
    const today = new Date();
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
        <div style={{ padding: "30px", paddingTop: "70px" }}>
          <Title>Sign Up</Title>
          <Form
            name="signup"
            initialValues={{ remember: true }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            style={{ minWidth: "270px" }}
          >
            {successMessage && (
              <Form.Item>
                <Alert message={successMessage} type="success" showIcon />
              </Form.Item>
            )}

            {errorMessage && (
              <Form.Item>
                <Alert message={errorMessage} type="error" showIcon />
              </Form.Item>
            )}
            <Form.Item
              name="firstName"
              rules={[
                { required: true, message: "Please input your name!" },
                {
                  pattern: /^[A-Za-z]+$/,
                  message: "Name must contain only letters!",
                },
              ]}
            >
              <Input placeholder="Name" />
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
                format="YYYY-MM-DD" ///////////Date format////////////
                disabledDate={disabledDate}
              />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[
                { required: true, message: "Please input your password!" },
                { min: 8, message: "Password must be at least 8 characters!" },
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
