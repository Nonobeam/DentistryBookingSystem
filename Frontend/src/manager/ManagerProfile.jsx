import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { Form, Input, Button, DatePicker, message, Spin, Layout } from "antd";
import dayjs from "dayjs";
import ManagerSidebar from "./ManagerSidebar";

const { Header, Content } = Layout;


const ManagerProfile = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [form] = Form.useForm();



  const fetchUserInfo = useCallback(async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8080/api/v1/manager/info", {
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
  }, [form]);

  useEffect(() => {
    fetchUserInfo();
  }, [fetchUserInfo]);

  const handleUpdate = async (values) => {
    try {
        const token = localStorage.getItem("token");
        
        const response = await axios.get(
            "http://localhost:8080/api/v1/manager/info/update",
            {
                params: {
                    id: user.id,
                    name: values.name,
                    phone: values.phone,
                    mail: user.mail,
                    birthday: values.birthday.format("YYYY-MM-DD"),
                    status: user.status
                },
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );
        
        setUser(response.data);
        message.success("Profile updated successfully");
    } catch (error) {
        console.error("Failed to update profile", error);
        message.error("Failed to update profile");
    }
};


  return (
    <Layout style={{ minHeight: "100vh" }}>
    <ManagerSidebar />
    <Layout style={{ margin: "10 auto", textAlign: 'left'}}>
    <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />
        <Content style={{ margin: '20px 30px 20px 20px', padding: '24px' }}>
      
      <h1>Profile</h1>
      {loading ? (
        <Spin />
      ) : (
        <Form
          form={form}
          style={{  margin: "0 auto" }}
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
            label="Name"
            rules={[{ required: true, message: "Please enter your name" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="phone"
            label="Phone"
            rules={[{ required: true, message: "Please enter your phone number" }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="mail"
            label="Email"
            rules={[{ required: true, message: "Please enter your email" }]}
          >
            <Input disabled />
          </Form.Item>

          <Form.Item
            name="birthday"
            label="Birthday"
            rules={[{ required: true, message: "Please select your birthday" }]}
          >
            <DatePicker format="YYYY-MM-DD" style={{ width: "100%" }} />
          </Form.Item>

        <Form.Item>
          <a href="/forgot">Wanna change password?</a>
         </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              Update Profile
            </Button>
          </Form.Item>
        </Form>
      )}
    </Content>
    </Layout>
    </Layout>
  );
};

export default ManagerProfile;
