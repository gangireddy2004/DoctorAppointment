import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav style={{
      display: 'flex', justifyContent: 'space-between', alignItems: 'center',
      padding: '15px 30px', backgroundColor: '#ffffff', borderBottom: '1px solid #e0e0e0'
    }}>
      <Link to="/" style={{ fontSize: '22px', fontWeight: 'bold', color: '#1a73e8', textDecoration: 'none' }}>
        HealthBridge
      </Link>
      <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
        {user ? (
          <>
            <span style={{ fontWeight: '600', color: '#333' }}>Welcome, {user.fullName}</span>
            <button onClick={handleLogout} style={{
              padding: '8px 16px', backgroundColor: '#e63946', color: 'white',
              border: 'none', borderRadius: '4px', cursor: 'pointer'
            }}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login" style={{ textDecoration: 'none', color: '#1a73e8', fontWeight: '600' }}>Login</Link>
            <Link to="/register" style={{
              textDecoration: 'none', backgroundColor: '#1a73e8', color: 'white',
              padding: '8px 16px', borderRadius: '4px', fontWeight: '600'
            }}>Sign Up</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;