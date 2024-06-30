import React, { useEffect, useState } from 'react';
import { PersonalServices } from '../../../../services/PersonalServices/PersonalServices';

export const Profile = () => {
  const [profileData, setProfileData] = useState({});
  const [editMode, setEditMode] = useState(false);
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
    const response = await PersonalServices.updatePersonalInfo(profileData);
    if (response.status === 200) {
    setProfileData({ ...profileData, [name]: value });
    }
  };  

  const handleEditClick = () => {
    setEditMode(true);
  };

  const handleSaveClick = () => {
    // Here you can handle saving the updated profile data
    setEditMode(false);
    // For demo purposes, let's log the updated data
    console.log('Updated Profile Data:', profileData);
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Profile</h2>
      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        <div>
          <img
            src='https://scontent.fsgn5-12.fna.fbcdn.net/v/t39.30808-6/422851969_122093528078209283_1563823667936309736_n.jpg?stp=dst-jpg_p526x296&_nc_cat=103&ccb=1-7&_nc_sid=5f2048&_nc_ohc=ZmcFG5-dxFQQ7kNvgHyKPMH&_nc_ht=scontent.fsgn5-12.fna&oh=00_AYDjkUA-WAFjo0TS8y4UH_ZYkG5BZTWAJO0aRjMJbClgLg&oe=666CCB4F'
            alt='Profile'
            style={{ width: '100px', height: '100px', borderRadius: '50%' }}
          />
        </div>
        <div>
          {editMode ? (
            <>
              <input
                type='text'
                name='name'
                value={profileData.name}
                onChange={handleInputChange}
                style={{ width: '100%', marginBottom: '10px', padding: '5px' }}
              />
              <input
                type='email'
                name='mail'
                value={profileData.mail}
                onChange={handleInputChange}
                style={{ width: '100%', marginBottom: '10px', padding: '5px' }}
              />
              <input
                type='text'
                name='birthday'
                value={profileData.birthday}
                onChange={handleInputChange}
                style={{ width: '100%', marginBottom: '10px', padding: '5px' }}
              />
              <input
                type='tel'
                name='phone'
                value={profileData.phone}
                onChange={handleInputChange}
                style={{ width: '100%', marginBottom: '10px', padding: '5px' }}
              />
            </>
          ) : (
            <>
              <p>
                <strong>Name:</strong> {profileData.name}
              </p>
              <p>
                <strong>Email:</strong> {profileData.mail}
              </p>
              <p>
                <strong>birthday:</strong> {profileData.birthday}
              </p>
              <p>
                <strong>Phone:</strong> {profileData.phone}
              </p>
            </>
          )}
        </div>
      </div>
      <div>
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
            }}>
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
            }}>
            Edit
          </button>
        )}
      </div>
    </div>
  );
};
