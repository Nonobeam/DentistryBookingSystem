import React, { useEffect, useRef } from 'react';

const Map = ({ locations }) => {
  const mapRef = useRef(null);

  useEffect(() => {
    const google = window.google;

    const map = new google.maps.Map(mapRef.current, {
      center: { lat: 10.762622, lng: 106.660172 }, // Default center location (Ho Chi Minh City)
      zoom: 12,
    });

    locations.forEach(location => {
      new google.maps.Marker({
        position: location,
        map,
        title: location.name,
      });
    });
  }, [locations]);

  return <div ref={mapRef} style={{ width: '100%', height: '400px' }} />;
};

export default Map;
