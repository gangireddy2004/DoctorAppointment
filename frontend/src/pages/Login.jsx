import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import authService from '../services/authService';
import { AuthContext } from '../context/AuthContext';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const data = await authService.signin(email, password);
      login(data);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Authentication failed. Verify credentials.');
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-form-side">
        <h2 style={{ marginBottom: '10px', color: '#0d1b2a' }}>Welcome Back!</h2>
        <p style={{ color: '#666', marginBottom: '30px' }}>Login to continue to your account</p>
        
        {error && <div style={{ color: '#e63946', marginBottom: '20px', fontWeight: '600' }}>{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email Address</label>
            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          </div>
          <button type="submit" className="btn-primary" style={{ marginTop: '10px' }}>Login</button>
        </form>
        <p style={{ marginTop: '25px', color: '#666' }}>
          Don't have an account? <Link to="/register" style={{ color: '#1a73e8', textDecoration: 'none', fontWeight: '600' }}>Sign up</Link>
        </p>
      </div>
      <div className="auth-banner-side">
        <h1 style={{ fontSize: '36px', marginBottom: '20px' }}>Your Health, Our Priority</h1>
        <p style={{ fontSize: '16px', maxWidth: '400px', lineHeight: '1.6' }}>
          HealthBridge connects you natively with top medical providers and virtual health channels instantly.
        </p>
      </div>
    </div>
  );
};

export default Login;