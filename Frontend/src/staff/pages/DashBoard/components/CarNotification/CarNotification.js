import React from 'react';

const styles = {
  container: {
    backgroundColor: '#f0f2f5',
    padding: '10px',
    marginBottom: '10px',
    borderRadius: '8px',
    boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.1)', // Add shadow for depth
  },
  content: {
    fontSize: '14px',
    lineHeight: '1.4',
    color: '#000',
    margin: '0',
  },
};

// Define CardNotification as a named function
export const CardNotification = ({ content }) => {
  return (
    <div style={styles.container}>
      <p style={styles.content}>{content}</p>
    </div>
  );
};
