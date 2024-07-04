import { Card } from 'antd';
import React from 'react';

export const CardTask = ({ data }) => {
  return (
    <Card bordered={false}>
      {data.map((task, idx) => (
        <Card key={idx} type='inner' title={task.time}>
          {task.task}  {task.dentistName}
        </Card>
      ))}
    </Card>
  );
};