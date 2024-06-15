import React, { useState } from 'react';


const EditForm = () => {
 
  const [formData, setFormData] = useState({
    patientName: 'John Doe',
    appointmentDate: '2024-06-15',
    appointmentTime: '10:00 AM',
    dentistName: 'Dr. Smith',
    treatmentType: 'Checkup',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission (e.g., update data on the server)
    console.log('Form data submitted:', formData);
  };

  return (
    <div >
     
      <h2>Edit Appointment</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="patientName">Patient Name:</label>
          <input type="text" id="patientName" name="patientName" value={formData.patientName} onChange={handleChange} />
        </div>
        <div>
          <label htmlFor="appointmentDate">Appointment Date:</label>
          <input type="date" id="appointmentDate" name="appointmentDate" value={formData.appointmentDate} onChange={handleChange} />
        </div>
        <div>
          <label htmlFor="appointmentTime">Appointment Time:</label>
          <input type="time" id="appointmentTime" name="appointmentTime" value={formData.appointmentTime} onChange={handleChange} />
        </div>
        <div>
          <label htmlFor="dentistName">Dentist Name:</label>
          <input type="text" id="dentistName" name="dentistName" value={formData.dentistName} onChange={handleChange} />
        </div>
        <div>
          <label htmlFor="treatmentType">Treatment Type:</label>
          <select id="treatmentType" name="treatmentType" value={formData.treatmentType} onChange={handleChange}>
            <option value="Checkup">Checkup</option>
            <option value="Cleaning">Cleaning</option>
            <option value="Filling">Filling</option>
            <option value="Extraction">Extraction</option>
          </select>
        </div>
        <button type="submit">Save Changes</button>
      </form>
    </div>
  );
};

export default EditForm;
