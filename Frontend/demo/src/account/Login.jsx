import React, { useState } from "react";
import axios from "axios";
import { Form, Input, Button, Typography, Alert } from "antd";
import { Navigate, useNavigate } from "react-router-dom";

const { Title } = Typography;

const API_URL = "http://localhost:8080/api/v1/auth/authenticate";

const Login = () => {
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const onFinish = async (values) => {
    console.log("Form values:", values);
    try {
      const response = await axios.post(
        API_URL,
        {
          mail: values.mail,
          password: values.password,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
          withCredentials: true,
        }
      );

      const { token, role } = response.data;
      const expirationTime = new Date().getTime() + 60 * 60 * 1000; // 1 hour expiration

      localStorage.setItem("token", token);
      localStorage.setItem("role", role);
      localStorage.setItem("expirationTime", expirationTime);

      console.log("Login successful:", response);
      console.log("Role:", role);
      setErrorMessage(""); // Clear previous error message
      navigate("/"); //
    } catch (error) {
      console.error("Failed to login:", error);
      if (error.response && error.response.status === 403) {
        setErrorMessage("Wrong email or password. Please try again.");
      } else {
        setErrorMessage("An error occurred. Please try again later.");
      }
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log("Failed:", errorInfo);
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
        <div style={{ padding: "30px" }}>
          <Title>Log In</Title>
          <Form
            name="login"
            initialValues={{ remember: true }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            style={{ minWidth: "270px" }}
          >
            {errorMessage && (
              <Form.Item>
                <Alert message={errorMessage} type="error" showIcon />
              </Form.Item>
            )}
            <Form.Item
              name="mail"
              rules={[
                {
                  required: true,
                  message: "Please input your email or phone number!",
                },
              ]}
            >
              <Input placeholder="Email or phone number" />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[
                { required: true, message: "Please input your password!" },
              ]}
            >
              <Input.Password placeholder="Password" />
            </Form.Item>

            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: "100%" }}
              >
                Log In
              </Button>
            </Form.Item>
            <Form.Item>
              <a href="/forgot">Forgot Password?</a>
            </Form.Item>
            <Form.Item>
              <a href="/signup">No account yet? Sign Up</a>
            </Form.Item>
          </Form>
        </div>
      </div>

      <div
        style={{
          flex: 1,
          backgroundColor: "#f0f2f5",
          maxWidth: "50vw",
          maxHeight: "100vh",
        }}
      >
        <img
          src="https://www.dpinc.net/wp-content/uploads/2021/03/9-scaled.jpg"
          style={{ height: "100%" }}
        ></img>
      </div>
    </div>
  );
};

export default Login;
