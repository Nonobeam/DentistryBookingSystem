import React, { useEffect, useState } from "react";
import axios from "axios";
import { Form, Input, Button, DatePicker, message, Spin, Card, Row, Col } from "antd";
import { UserOutlined, PhoneOutlined, MailOutlined, CalendarOutlined } from '@ant-design/icons';
import dayjs from "dayjs"; 
import NavBar from "../homepage/Nav";

const UserProfile = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [form] = Form.useForm();

  useEffect(() => {
    fetchUserInfo();
  }, []);

  const fetchUserInfo = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8080/user/info", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setUser(response.data);
      setLoading(false);
      form.setFieldsValue({
        name: response.data.name,
        phone: response.data.phone,
        mail: response.data.mail,
        birthday: dayjs(response.data.birthday)
      });
    } catch (error) {
      message.error("Failed to fetch user information");
      setLoading(false);
    }
  };

  const handleUpdate = async (values) => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(
        "http://localhost:8080/user/info/update",
        {
          name: values.name,
          phone: values.phone,
          mail: user.mail,
          birthday: values.birthday.format("YYYY-MM-DD"),
          id: user.id,
          status: user.status
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      setUser(response.data);
      message.success("Profile updated successfully");
    } catch (error) {
      message.error("Failed to update profile");
    }
  };

  return (
    <>
      <NavBar />
      <div style={{ maxWidth: "600px", margin: "0 auto", padding: "20px" }}>
        <Card
          title="Profile"
          bordered={false}
          style={{ textAlign: "center" }}
        >
          {loading ? (
            <Spin />
          ) : (
            <Form
              form={form}
              layout="vertical"
              onFinish={handleUpdate}
              initialValues={{
                name: user.name,
                phone: user.phone,
                mail: user.mail,
                birthday: dayjs(user.birthday)
              }}
            >
              <Form.Item
                name="name"
                label={<span><UserOutlined /> Name <span style={{ color: 'red' }}>*</span></span>}
                rules={[{ required: true, message: "Please enter your name" }]}
              >
                <Input placeholder="Enter your name" />
              </Form.Item>
              <Form.Item
                name="phone"
                label={<span><PhoneOutlined /> Phone <span style={{ color: 'red' }}>*</span></span>}
                rules={[
                  { required: true, message: "Please enter your phone number" },{
                    pattern: /^\d{10}$/,
                    message: "Phone number must be 10 digits!",
                  },]}
              >
                <Input placeholder="Enter your phone number" />
              </Form.Item>
              <Form.Item
                name="mail"
                label={<span><MailOutlined /> Email <span style={{ color: 'red' }}>*</span></span>}
                rules={[{ required: true, message: "Please enter your email" }]}
              >
                <Input disabled />
              </Form.Item>
              <Form.Item
                name="birthday"
                label={<span><CalendarOutlined /> Birthday <span style={{ color: 'red' }}>*</span></span>}
                rules={[{ required: true, message: "Please select your birthday" }]}
              >
                <DatePicker style={{ width: "100%" }}/>
              </Form.Item>
              <Form.Item>
                <a href="/forgot">Wanna change password?</a>
              </Form.Item>
              <Form.Item>
                <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
                  Update Profile
                </Button>
              </Form.Item>
            </Form>
          )}
        </Card>
      </div>
    </>
  );
};

export default UserProfile;
