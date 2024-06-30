import React, { useState } from 'react';
import moment from 'moment';
import { Button } from 'antd';
import { Link } from 'react-router-dom'; // Thêm import cho Link
import { CardTask } from './Card/CardTask';

const taskTime = [
  { time: '8:00', date: '10/10/2021', task: 'Task 1' },
  { time: '10:00', date: '10/10/2021', task: 'Task 2' },
  { time: '12:00', date: '29/05/2021', task: 'Task 3' },
  { time: '14:00', date: '28/05/2024', task: 'Task 4' },
  { time: '16:00', date: '26/05/2024', task: 'Task 5' },
  { time: '18:31', date: '30/05/2024', task: 'Task 6' },
  { time: '20:00', date: '30/05/2024', task: 'Task 7' },
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
    const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
    const tasksForWeek = [];

    weekDates.forEach((date, index) => {
      const tasksForDay = taskTime
        .filter((task) => moment(task.date, 'DD/MM/YYYY').isSame(date, 'day'))
        .map((task) => ({
          time: task.time,
          task: task.task,
        }));

      tasksForWeek.push({
        day: daysOfWeek[index],
        date: date.format('DD/MM'),
        tasks: tasksForDay,
      });
    });

    return tasksForWeek;
  };

  const weekDates = getWeekDates(currentWeek);
  const tasksForWeek = generateTasksForWeek(weekDates);

  return (
    <div style={{ padding: '20px' }}>
      <h1>Weekly Task Manager</h1>
      <Link to='/schedule'>
        <Button style={{ marginRight: '10px' }}>Schedule</Button>
      </Link>
      <Button onClick={previousWeek} style={{ marginRight: '10px' }}>
        Previous Week
      </Button>
      <Button onClick={nextWeek}>Next Week</Button>
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
        }}>
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
              }}>
              <h3>{date.format('DD/MM')}</h3>
            </div>
          ))}
        </div>
        <div
          style={{
            display: 'flex',
            padding: '10px',
            gap: '10px',
            flexWrap: 'wrap',
          }}>
          {tasksForWeek.map((day, index) => (
            <div key={index} style={{ flex: 1, borderRight: '1px solid #ccc' }}>
              <h3>{day.day}</h3>
              {day.tasks.length > 0 ? (
                <CardTask data={day.tasks} />
              ) : (
                <p
                  style={{
                    textAlign: 'center',
                    padding: '10px',
                    fontWeight: 'bold',
                    background: '#f0f0f0',
                  }}>
                  No tasks for this day
                </p>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
