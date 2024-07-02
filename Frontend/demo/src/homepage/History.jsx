import React from 'react';

const History = () => {
    // Replace this with your actual appointment data
    const appointments = [
        { id: 1, date: '2022-01-01', time: '10:00 AM', patient: 'John Doe' },
        { id: 2, date: '2022-01-02', time: '11:00 AM', patient: 'Jane Smith' },
        { id: 3, date: '2022-01-03', time: '02:00 PM', patient: 'Alice Johnson' },
    ];

    return (
        <div>
            <h1>Appointment History</h1>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Patient</th>
                    </tr>
                </thead>
                <tbody>
                    {appointments.map(appointment => (
                        <tr key={appointment.id}>
                            <td>{appointment.id}</td>
                            <td>{appointment.date}</td>
                            <td>{appointment.time}</td>
                            <td>{appointment.patient}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default History;