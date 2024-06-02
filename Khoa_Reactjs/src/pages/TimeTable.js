import React, { useState } from 'react';
import moment from 'moment';
import { Button, Card, Col, Row, Table } from 'antd';
import { CardTask } from '../components/Card/CardTask';

const taskTime = [
  { time: '8:00', date: '10/10/2021', task: 'Task 1' },
  { time: '10:00', date: '10/10/2021', task: 'Task 2' },
  { time: '12:00', date: '10/10/2021', task: 'Task 3' },
  { time: '14:00', date: '28/05/2024', task: 'Task 4' },
  { time: '16:00', date: '26/05/2024', task: 'Task 5' },
  { time: '18:31', date: '30/05/2024', task: 'Task 6' },
  { time: '20:00', date: '30/05/2024', task: 'Task 7' },
];

export const TimeTable = () => {
  const [currentWeek, setCurrentWeek] = useState(moment().startOf('week'));

  const nextWeek = () => {
    setCurrentWeek(currentWeek.clone().add(1, 'week'));
  };

  const previousWeek = () => {
    setCurrentWeek(currentWeek.clone().subtract(1, 'week'));
  };

  const getWeekDates = (startOfWeek) => {
    const weekDates = [];
    for (let i = 0; i < 7; i++) {
      weekDates.push(startOfWeek.clone().add(i, 'day'));
    }
    return weekDates;
  };

  const generateTasksForWeek = (weekDates) => {
    const daysOfWeek = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const tasksForWeek = [];

    weekDates.slice(0, 6).forEach((date, index) => {
      const tasksForDay = taskTime
        .filter((task) => moment(task.date, 'DD/MM/YYYY').isSame(date, 'day'))
        .map((task) => ({
          time: task.time,
          task: task.task,
        }));

      tasksForWeek.push({ day: daysOfWeek[index], date: date.format('DD/MM'), tasks: tasksForDay });
    });

    return tasksForWeek;
  };

  const weekDates = getWeekDates(currentWeek);
  const tasksForWeek = generateTasksForWeek(weekDates);

  return (
    <div style={{ padding: '20px' }}>
      <h1>Weekly Task Manager</h1>
      <Button onClick={previousWeek} style={{ marginRight: '10px' }}>
        Previous Week
      </Button>
      <Button onClick={nextWeek}>Next Week</Button>
      <Row gutter={[16, 16]} style={{ marginTop: '20px' }}>
        {tasksForWeek.map((day, index) => (
          <Col span={4} key={index}>
            <h3>
              {day.day} <span style={{ float: 'right' }}>{day.date}</span>
            </h3>
            {day.tasks.length > 0 ? <CardTask data={day.tasks} /> : <p>No tasks for this day</p>}
          </Col>
        ))}
      </Row>
    </div>
  );
};