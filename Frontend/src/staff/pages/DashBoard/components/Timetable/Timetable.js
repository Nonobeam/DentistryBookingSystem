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
            status: task.status === 1 ? 'Arranged' : 'On appointment',
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
          backgroundColor: '#1976d2',
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
              backgroundColor: '#1976d2',
              color: 'white',
              borderColor: '#1976d2',
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
            backgroundColor: '#1976d2',
            color: 'white',
            borderColor: '#1976d2',
            borderRadius: '5px',
            boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
          }}>
          Previous Week
        </Button>
        <Button
          onClick={nextWeek}
          style={{
            backgroundColor: '#1976d2',
            color: 'white',
            borderColor: '#1976d2',
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
        <div style={{ display: 'flex', marginBottom: '10px' }}>
  {weekDates.map((date, index) => (
    <div
      key={index}
      style={{
        flex: 1,
        textAlign: 'center',
        padding: '5px',
        fontWeight: 'bold',
        background: index % 2 === 0 ? '#E3EFFF' : '#FFFFFF',
        color: '#001F3F',
      }}>
      <div style={{ fontSize: '14px' }}>{dayjs(date).format('DD/MM')}</div>
      <div style={{ fontSize: '12px' }}>{dayjs(date).format('ddd')}</div>
    </div>
  ))}
</div>
<div
  style={{
    display: 'flex',
    gap: '10px',
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
      <div key={index} style={{ flex: 1, minWidth: 0 }}>
        <h3
          style={{
            textAlign: 'center',
            backgroundColor: '#E3EFFF',
            padding: '5px',
            color: '#1976d2',
            fontSize: '14px',
            marginBottom: '8px'
          }}>
          {day.day}
        </h3>
        <div style={{ maxHeight: 'calc(100vh - 250px)', overflowY: 'auto' }}>
          {day.tasks.length > 0 ? (
            <div>
              {day.tasks.map((task, index) => (
                <Card
                  key={index}
                  size="small"
                  style={{
                    marginBottom: '8px',
                    borderColor: 'blue',
                    borderRadius: '5px',
                    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.1)',
                  }}>
                  <Meta
                    title={<div style={{ fontSize: '12px' }}>{`Dentist: ${task.dentistName}`}</div>}
                    description={
                      <div style={{ color: 'darkblue',fontWeight:'bold', fontSize: '11px' }}>
                        <p style={{ margin: '2px 0' }}>Customer: {task.customerName}</p>
                        <p style={{ margin: '2px 0' }}>Service: {task.serviceName}</p>
                        <p style={{ margin: '2px 0' }}>Status: {task.status}</p>
                        <p style={{ margin: '2px 0' }}>Start Time: {task.time}</p>
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
                padding: '5px',
                fontWeight: 'bold',
                background: '#E3EFFF',
                marginBottom: '0',
                color: '#1976d2',
                fontSize: '12px',
              }}>
              No tasks for this day
            </p>
          )}
        </div>
      </div>
    ))
  )}
</div>
      </div>
    </div>
  );
};
