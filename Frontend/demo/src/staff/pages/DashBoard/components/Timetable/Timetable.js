import React, { useState, useEffect } from 'react';
import moment from 'moment';
import { Button, Card, Spin } from 'antd';
import { Link } from 'react-router-dom';
import TimetableServices from '../../../../services/TimetableServices/TimetableServices';

const { Meta } = Card;

export const TimeTable = () => {
  const [currentWeek, setCurrentWeek] = useState(moment().startOf('week'));
  const [firstDayOfWeek, setFirstDayOfWeek] = useState(moment().startOf('week'));
  const [tasksFromApi, setTasksFromApi] = useState({});
  const [loading, setLoading] = useState(false); // State để theo dõi trạng thái loading

  useEffect(() => {
    fetchTimetableData();
  }, [firstDayOfWeek]);

  const fetchTimetableData = async () => {
    try {
      setLoading(true); // Bắt đầu fetch dữ liệu, hiển thị biểu tượng loading
      const formattedDate = firstDayOfWeek.format('YYYY-MM-DD');
      const response = await TimetableServices.getAll({
        date: formattedDate,
        numDay: 6,
      });
      setTasksFromApi(response);
    } catch (error) {
      console.error('Error fetching timetable data:', error);
    } finally {
      setLoading(false); // Kết thúc fetch dữ liệu, ẩn biểu tượng loading
    }
  };

  useEffect(() => {
    setFirstDayOfWeek(moment(currentWeek).startOf('week'));
  }, [currentWeek]);

  const nextWeek = () => {
    setCurrentWeek(currentWeek.clone().add(1, 'week'));
  };

  const previousWeek = () => {
    setCurrentWeek(currentWeek.clone().subtract(1, 'week'));
  };

  const getWeekDates = (startOfWeek) => {
    const weekDates = [];
    for (let i = 0; i < 7; i++) {
      weekDates.push(startOfWeek.clone().add(i, 'day').format('YYYY-MM-DD'));
    }
    return weekDates;
  };

  const generateTasksForWeek = (weekDates) => {
    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const tasksForWeek = [];

    weekDates.forEach((date) => {
      const tasksForDay = tasksFromApi[date] || [];

      tasksForWeek.push({
        date: date,
        day: moment(date).format('ddd'),
        tasks: tasksForDay.map((task) => ({
          time: moment(task.time, 'HH:mm:ss').format('HH:mm'),
          dentistName: task.dentistName,
          customerName: task.customerName || 'N/A',
          serviceName: task.serviceName || 'N/A',
          status: task.status === 1 ? 'arranged' : 'On appointment',
        })),
      });
    });

    return tasksForWeek;
  };

  const weekDates = getWeekDates(currentWeek);
  const tasksForWeek = generateTasksForWeek(weekDates);

  return (
    <div style={{ padding: '20px' }}>
      <h1 style={{ textAlign: 'center', backgroundColor: '#f0f0f0', padding: '10px' }}>Weekly Task Manager</h1>
      <div style={{ textAlign: 'center', margin: '20px 0' }}>
        <Link to='/schedule'>
          <Button style={{ marginRight: '10px' }}>Schedule</Button>
        </Link>
        <Button onClick={previousWeek} style={{ marginRight: '10px' }}>
          Previous Week
        </Button>
        <Button onClick={nextWeek}>Next Week</Button>
      </div>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          border: '1px solid #ccc',
          marginTop: '20px',
          padding: '10px',
          gap: '10px',
          borderRadius: '10px',
          overflow: 'hidden',
        }}
      >
        <div style={{ display: 'flex', borderBottom: '1px solid #ccc' }}>
          {weekDates.map((date, index) => (
            <div
              key={index}
              style={{
                flex: 1,
                textAlign: 'center',
                padding: '10px',
                borderRight: '1px solid #ccc',
                borderBottom: '1px solid #ccc',
                fontWeight: 'bold',
                background: index % 2 === 0 ? '#f0f0f0' : '#fff',
              }}
            >
              <h3>{moment(date).format('DD/MM')}</h3>
              <p>{moment(date).format('ddd')}</p>
            </div>
          ))}
        </div>
        <div
          style={{
            display: 'flex',
            padding: '10px',
            gap: '10px',
            flexWrap: 'wrap',
          }}
        >
          {loading ? ( // Kiểm tra nếu đang loading thì hiển thị Spin (biểu tượng loading)
            <Spin size="large" />
          ) : (
            tasksForWeek.map((day, index) => (
              <div key={index} style={{ flex: 1, borderRight: '1px solid #ccc', marginBottom: '10px' }}>
                <h3 style={{ textAlign: 'center', backgroundColor: '#f0f0f0', padding: '10px' }}>{day.day}</h3>
                {day.tasks.length > 0 ? (
                  <div>
                    {day.tasks.map((task, index) => (
                      <Card key={index}>
                        <Meta
                          title={`Dentist: ${task.dentistName}`}
                          description={
                            <div>
                              <p>Customer: {task.customerName}</p>
                              <p>Service: {task.serviceName}</p>
                              <p>Status: {task.status}</p>
                              <p>Start Time: {task.time}</p>
                            </div>
                          }
                        />
                      </Card>
                    ))}
                  </div>
                ) : (
                  <p
                    style={{
                      textAlign: 'center',
                      padding: '1px',
                      fontWeight: 'bold',
                      background: '#f0f0f0',
                      marginBottom: '0',
                    }}
                  >
                    No tasks for this day
                  </p>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
};
