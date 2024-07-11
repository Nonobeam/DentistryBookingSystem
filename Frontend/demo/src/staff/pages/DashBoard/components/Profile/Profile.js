import React, { useEffect, useState } from 'react';
import { PersonalServices } from '../../../../services/PersonalServices/PersonalServices';

export const Profile = () => {
  const [profileData, setProfileData] = useState({});
  const [editMode, setEditMode] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await PersonalServices.getPersonalInfo();
        setProfileData(response);
      } catch (error) {
        console.log(error);
      }
    };
    fetchData();
  }, []);

  const handleInputChange = async (e) => {
    const { name, value } = e.target;
    setProfileData({ ...profileData, [name]: value });
  };

  const handleEditClick = () => {
    setEditMode(true);
    setSuccessMessage(''); // Reset success message when entering edit mode
  };

  const handleSaveClick = async () => {
    setEditMode(false);

    const response = await PersonalServices.updatePersonalInfo(profileData);
    if (response.status === 200) {
      setProfileData(response.data);
      setSuccessMessage('Profile updated successfully!');
    }
    console.log('Updated Profile Data:', profileData);
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
      <h2 style={{ marginBottom: '20px', borderBottom: '2px solid #007bff', paddingBottom: '10px' }}>Profile</h2>
      {successMessage && <h3 style={{ color: 'green', marginBottom: '10px' }}>{successMessage}</h3>}
      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        <div style={{ flex: 1 }}>
          {editMode ? (
            <>
              <div style={{ marginBottom: '10px' }}>
                <label htmlFor='name' style={{ marginRight: '10px', fontWeight: 'bold' }}>Name:</label>
                <input
                  type='text'
                  id='name'
                  name='name'
                  value={profileData.name}
                  onChange={handleInputChange}
                  style={{ width: '100%', padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
                />
              </div>
              <div style={{ marginBottom: '10px' }}>
                <label htmlFor='mail' style={{ marginRight: '10px', fontWeight: 'bold' }}>Email:</label>
                <input
                  type='text'
                  id='mail'
                  name='mail'
                  value={profileData.mail}
                  onChange={handleInputChange}
                  style={{ width: '100%', padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
                  disabled // Disable editing for email field
                />
              </div>
              <div style={{ marginBottom: '10px' }}>
                <label htmlFor='birthday' style={{ marginRight: '10px', fontWeight: 'bold' }}>Birthday:</label>
                <input
                  type='text'
                  id='birthday'
                  name='birthday'
                  value={profileData.birthday}
                  onChange={handleInputChange}
                  style={{ width: '100%', padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
                />
              </div>
              <div style={{ marginBottom: '10px' }}>
                <label htmlFor='phone' style={{ marginRight: '10px', fontWeight: 'bold' }}>Phone:</label>
                <input
                  type='text'
                  id='phone'
                  name='phone'
                  value={profileData.phone}
                  onChange={handleInputChange}
                  style={{ width: '100%', padding: '8px', borderRadius: '5px', border: '1px solid #ccc' }}
                />
              </div>
            </>
          ) : (
            <>
              <p><strong>Name:</strong> {profileData.name}</p>
              <p><strong>Email:</strong> {profileData.mail}</p>
              <p><strong>Birthday:</strong> {profileData.birthday}</p>
              <p><strong>Phone:</strong> {profileData.phone}</p>
            </>
          )}
        </div>
      </div>
      <div style={{ marginTop: '20px' }}>
        {editMode ? (
          <button
            onClick={handleSaveClick}
            style={{
              padding: '10px 20px',
              backgroundColor: '#007bff',
              color: '#fff',
              border: 'none',
              borderRadius: '5px',
              cursor: 'pointer',
              transition: 'background-color 0.3s ease',
            }}
          >
            Save
          </button>
        ) : (
          <button
            onClick={handleEditClick}
            style={{
              padding: '10px 20px',
              backgroundColor: '#007bff',
              color: '#fff',
              border: 'none',
              borderRadius: '5px',
              cursor: 'pointer',
              transition: 'background-color 0.3s ease',
            }}
          >
            Edit
          </button>
        )}
      </div>
    </div>
  );
};
