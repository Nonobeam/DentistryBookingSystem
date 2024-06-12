import React from "react";
import { Form, Input, Button, Typography } from "antd";

const { Title } = Typography;

const Login = () => {
  const onFinish = (values) => {
    console.log("Success:", values);
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
            <Form.Item
              name="email"
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

      <div style={{ flex: 1, backgroundColor: "#f0f2f5", maxWidth: "50vw", maxHeight: "100vh"}}>
        <img src="https://www.dpinc.net/wp-content/uploads/2021/03/9-scaled.jpg" style={{height: "100%"}}></img>
      </div>
    </div>
  );
};

export default Login;
