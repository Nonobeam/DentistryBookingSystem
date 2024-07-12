import React, { useState } from "react";
import axios from "axios";
import { Form, Input, Button, Checkbox, Typography, DatePicker, Alert, Spin } from "antd";
import styled from "styled-components";

const { Title } = Typography;

const API_URL = "http://localhost:8080/api/v1/auth/register";

const SignupContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
`;

const SignupBox = styled.div`
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
  text-align: center;
`;

const StyledButton = styled(Button)`
  width: 100%;
  background-color: #1890ff;
  border: none;
  &:hover {
    background-color: #167acb;
  }
`;

const ExtraLink = styled.div`
  margin-top: 20px;
  text-align: center;
  a {
    color: #1890ff;
    &:hover {
      text-decoration: underline;
    }
  }
`;

const Signup = () => {
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const onFinish = async (values) => {
    setLoading(true);
    setErrorMessage("");
    setSuccessMessage("");
    try {
      const response = await axios.post(API_URL, {
        name: values.firstName,
        phone: values.phoneNumber,
        mail: values.email,
        password: values.password,
        birthday: values.birthdate.format("YYYY-MM-DD"),
      });

      if (response.status === 200) {
        
        setSuccessMessage("Registration successful. Please check your email to confirm your account.");
      }
    } catch (error) {
      setErrorMessage("Your email or phone is already registered. Please login or use a different email or phone number.");
    }
    setLoading(false);
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
  };

  const disabledDate = (current) => {
    const today = new Date();
    return current && current > today.setHours(23, 59, 59, 999);
  };

  return (
    <SignupContainer>
      <SignupBox>
        <Title level={2} style={{ color: '#1890ff' }}>Sign Up</Title>
        <Form
          name="signup"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
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
                pattern: /([a-zA-Z\s]+)/,
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
              format="YYYY-MM-DD"
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
              {
                validator: (_, value) =>
                  value ? Promise.resolve() : Promise.reject("Check this box to continue"),
              },
            ]}
          >
            <Checkbox>I confirm that all my information is correct</Checkbox>
          </Form.Item>

          <Form.Item>
            <StyledButton type="primary" htmlType="submit" loading={loading}>
              Sign Up
            </StyledButton>
          </Form.Item>
          <ExtraLink>
            <a href="/login">Already have an account?</a>
          </ExtraLink>
        </Form>
      </SignupBox>
    </SignupContainer>
  );
};

export default Signup;
