import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../services/authService';

const Register = () => {
  const [fullName, setFullName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('patient');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await authService.signup(fullName, email, password, role);
      setSuccess('Account created successfully! Redirecting to login...');
      setTimeout(() => navigate('/login'), 2500);
    } catch (err) {
      setError(err.response?.data || 'Registration processing failed.');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-form-side">
        <h2 style={{ marginBottom: '10px', color: '#0d1b2a' }}>Create Your Account</h2>
        <p style={{ color: '#666', marginBottom: '30px' }}>Join HealthBridge and take control of your scheduling</p>
        
        {error && <div style={{ color: '#e63946', marginBottom: '20px', fontWeight: '600' }}>{error}</div>}
        {success && <div style={{ color: '#2ec4b6', marginBottom: '20px', fontWeight: '600' }}>{success}</div>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Full Name</label>
            <input type="text" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
          </div>
          <div className="form-group">
            <label>Email Address</label>
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          </div>
          <div className="form-group">
            <label>Account Profile Classification</label>
            <select value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="patient">Patient Profile</option>
              <option value="doctor">Medical Doctor Profile</option>
            </select>
          </div>
          <button type="submit" className="btn-primary" style={{ marginTop: '10px' }}>Register</button>
        </form>
        <p style={{ marginTop: '25px', color: '#666' }}>
          Already have an account? <Link to="/login" style={{ color: '#1a73e8', textDecoration: 'none', fontWeight: '600' }}>Login</Link>
        </p>
      </div>
      <div className="auth-banner-side">
        <h1 style={{ fontSize: '36px', marginBottom: '20px' }}>Empowering Telehealth</h1>
        <p style={{ fontSize: '16px', maxWidth: '400px', lineHeight: '1.6' }}>
          Gain real-time access to diagnostic tracking fields, scheduling slots, and high-definition video rooms.
        </p>
      </div>
    </div>
  );
};

export default Register;