import React, { useContext } from 'react';
import { NavLink } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Sidebar = () => {
  const { user } = useContext(AuthContext);
  const isDoctor = user?.roles?.includes('ROLE_DOCTOR');

  return (
    <div className="sidebar" style={{ minHeight: 'calc(100vh - 64px)' }}>
      <div style={{ display: 'flex', flexDirection: 'column', gap: '15px', marginTop: '20px' }}>
        <NavLink to="/dashboard" style={({ isActive }) => ({
          color: 'white', textDecoration: 'none', padding: '12px', borderRadius: '4px',
          backgroundColor: isActive ? '#1a73e8' : 'transparent', display: 'block'
        })}>
          Dashboard
        </NavLink>
        <NavLink to="/profile" style={({ isActive }) => ({
          color: 'white', textDecoration: 'none', padding: '12px', borderRadius: '4px',
          backgroundColor: isActive ? '#1a73e8' : 'transparent', display: 'block'
        })}>
          My Profile
        </NavLink>
        {!isDoctor && (
          <NavLink to="/search-doctors" style={({ isActive }) => ({
            color: 'white', textDecoration: 'none', padding: '12px', borderRadius: '4px',
            backgroundColor: isActive ? '#1a73e8' : 'transparent', display: 'block'
          })}>
            Search Doctors
          </NavLink>
        )}
      </div>
    </div>
  );
};

export default Sidebar;