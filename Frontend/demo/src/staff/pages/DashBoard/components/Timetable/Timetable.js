import React, { useState, useEffect } from 'react';
import dayjs from 'dayjs';
import { Button, Card, Spin } from 'antd';
import { Link } from 'react-router-dom';
import TimetableServices from '../../../../services/TimetableServices/TimetableServices';
import useSWR from 'swr';

const { Meta } = Card;

const fetchTimetableData = async (firstDayOfWeek) => {
  const formattedDate = firstDayOfWeek.format('YYYY-MM-DD');
  const response = await TimetableServices.getAll({
    date: formattedDate,
    numDay: 6,
  });
  console.log(response);
  return response;
};

export const TimeTable = () => {
  const [currentWeek, setCurrentWeek] = useState(dayjs().startOf('week'));
  const [firstDayOfWeek, setFirstDayOfWeek] = useState(
    dayjs().startOf('week')
  );
  const { data, isLoading, mutate } = useSWR('Timetable', () =>
    fetchTimetableData(firstDayOfWeek)
  );

  useEffect(() => {
    mutate();
  }, [firstDayOfWeek]);

  useEffect(() => {
    setFirstDayOfWeek(dayjs(currentWeek).startOf('week'));
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
    const tasksForWeek = [];

    if (!isLoading) {
      weekDates.forEach((date) => {
        const tasksForDay = data[date] || [];

        tasksForWeek.push({
          date: date,
          day: dayjs(date).format('ddd'),
          tasks: tasksForDay.map((task) => ({
            time: dayjs(task.time, 'HH:mm:ss').format('HH:mm'),
            dentistName: task.dentistName,
            customerName: task.customerName || 'N/A',
            serviceName: task.serviceName || 'N/A',
            status: task.status === 1 ? 'arranged' : 'On appointment',
          })),
        });
      });
    }

    return tasksForWeek;
  };

  const weekDates = getWeekDates(currentWeek);
  const tasksForWeek = generateTasksForWeek(weekDates);

  return (
    <div style={{ padding: '20px' }}>
      <h1
        style={{
          textAlign: 'center',
          backgroundColor: '#1890ff',
          padding: '20px',
          color: 'white',
          border: '2px',
          borderRadius: '5px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
          marginBottom: '20px',
        }}>
        Weekly Task Manager
      </h1>
      <div style={{ textAlign: 'center', margin: '20px 0' }}>
        <Link to='/staff/schedule'>
          <Button
            style={{
              marginRight: '10px',
              backgroundColor: '#1890ff',
              color: 'white',
              borderColor: '#1890ff',
              borderRadius: '5px',
              boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
            }}>
            Schedule
          </Button>
        </Link>
        <Button
          onClick={previousWeek}
          style={{
            marginRight: '10px',
            backgroundColor: '#1890ff',
            color: 'white',
            borderColor: '#1890ff',
            borderRadius: '5px',
            boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
          }}>
          Previous Week
        </Button>
        <Button
          onClick={nextWeek}
          style={{
            backgroundColor: '#1890ff',
            color: 'white',
            borderColor: '#1890ff',
            borderRadius: '5px',
            boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
          }}>
          Next Week
        </Button>
      </div>
      <div
        style={{
          display: 'flex',
          flexDirection: 'column',
          marginTop: '20px',
          padding: '20px',
          gap: '20px',
          borderRadius: '10px',
          overflow: 'hidden',
          backgroundColor: '#F5F5F5',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        }}>
        <div style={{ display: 'flex' }}>
          {weekDates.map((date, index) => (
            <div
              key={index}
              style={{
                flex: 1,
                textAlign: 'center',
                padding: '10px',
                fontWeight: 'bold',
                background: index % 2 === 0 ? '#E3EFFF' : '#FFFFFF',
                color: '#001F3F',
              }}>
              <h3>{dayjs(date).format('DD/MM')}</h3>
              <p>{dayjs(date).format('ddd')}</p>
            </div>
          ))}
        </div>
        <div
          style={{
            display: 'flex',
            padding: '10px',
            gap: '20px',
            flexWrap: 'wrap',
          }}>
          {isLoading ? (
            <div
              style={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                width: '100%',
              }}>
              <Spin size='large' />
            </div>
          ) : (
            tasksForWeek.map((day, index) => (
              <div key={index} style={{ flex: 1, marginBottom: '20px' }}>
                <h3
                  style={{
                    textAlign: 'center',
                    backgroundColor: '#E3EFFF',
                    padding: '10px',
                    color: '#1890ff',
                  }}>
                  {day.day}
                </h3>
                {day.tasks.length > 0 ? (
                  <div>
                    {day.tasks.map((task, index) => (
                      <Card
                        key={index}
                        style={{
                          marginBottom: '10px',
                          borderColor: '#001F3F',
                          borderRadius: '10px',
                          boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                        }}>
                        <Meta
                          title={`Dentist: ${task.dentistName}`}
                          description={
                            <div style={{ color: '#1890ff' }}>
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
                      padding: '10px',
                      fontWeight: 'bold',
                      background: '#E3EFFF',
                      marginBottom: '0',
                      color: '#1890ff',
                    }}>
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
