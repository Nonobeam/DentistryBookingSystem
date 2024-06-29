import React, { useState } from 'react';

export default function CustomerListAdmin() {
  // Sample customer data (replace with actual data fetching)
  const [customers, setCustomers] = useState([
    { id: 1, name: 'John Doe', email: 'john.doe@example.com', phone: '123-456-7890' },
    { id: 2, name: 'Jane Smith', email: 'jane.smith@example.com', phone: '987-654-3210' }
    // Add more customers as needed
  ]);

  const [editCustomer, setEditCustomer] = useState(null);

  const handleEditClick = (customer) => {
    setEditCustomer({ ...customer });
  };

  const handleSaveEdit = () => {
    const updatedCustomers = customers.map(cust =>
      cust.id === editCustomer.id ? editCustomer : cust
    );
    setCustomers(updatedCustomers);
    setEditCustomer(null);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditCustomer(prevState => ({
      ...prevState,
      [name]: value
    }));
  };

  return (
    <div style={{ fontFamily: 'Arial, sans-serif', padding: '20px' }}>
      <h1 style={{ textAlign: 'center' }}>Customer List</h1>
      <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
        <thead>
          <tr>
            <th style={{ border: '1px solid #ddd', padding: '8px', backgroundColor: '#f2f2f2' }}>ID</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', backgroundColor: '#f2f2f2' }}>Name</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', backgroundColor: '#f2f2f2' }}>Email</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', backgroundColor: '#f2f2f2' }}>Phone</th>
            <th style={{ border: '1px solid #ddd', padding: '8px', backgroundColor: '#f2f2f2' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(customer =>
            <tr key={customer.id}>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>{customer.id}</td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                {editCustomer && editCustomer.id === customer.id ? (
                  <input
                    type="text"
                    name="name"
                    value={editCustomer.name}
                    onChange={handleInputChange}
                    style={{ width: '100%', padding: '4px' }}
                  />
                ) : (
                  customer.name
                )}
              </td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                {editCustomer && editCustomer.id === customer.id ? (
                  <input
                    type="text"
                    name="email"
                    value={editCustomer.email}
                    onChange={handleInputChange}
                    style={{ width: '100%', padding: '4px' }}
                  />
                ) : (
                  customer.email
                )}
              </td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                {editCustomer && editCustomer.id === customer.id ? (
                  <input
                    type="text"
                    name="phone"
                    value={editCustomer.phone}
                    onChange={handleInputChange}
                    style={{ width: '100%', padding: '4px' }}
                  />
                ) : (
                  customer.phone
                )}
              </td>
              <td style={{ border: '1px solid #ddd', padding: '8px' }}>
                {editCustomer && editCustomer.id === customer.id ? (
                  <div>
                    <button onClick={handleSaveEdit} style={{ marginRight: '4px' }}>Save</button>
                    <button onClick={() => setEditCustomer(null)}>Cancel</button>
                  </div>
                ) : (
                  <button onClick={() => handleEditClick(customer)}>Edit</button>
                )}
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
