import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import patientService from '../services/patientService';
import doctorService from '../services/doctorService';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';

const Profile = () => {
  const { user } = useContext(AuthContext);
  const isDoctor = user?.roles?.includes('ROLE_DOCTOR');
  
  // Shared structural inputs
  const [fullName, setFullName] = useState(user?.fullName || '');
  const [phone, setPhone] = useState('');
  const [address, setAddress] = useState('');
  
  // Doctor domain metrics
  const [specialization, setSpecialization] = useState('');
  const [qualification, setQualification] = useState('');
  const [experience, setExperience] = useState(1);
  const [hospital, setHospital] = useState('');
  const [fee, setFee] = useState(500);
  const [languages, setLanguages] = useState('English');

  useEffect(() => {
    loadProfileData();
  }, []);

  const loadProfileData = async () => {
    try {
      if (isDoctor) {
        const data = await doctorService.getProfile();
        setSpecialization(data.specialization || '');
        setQualification(data.qualification || '');
        setExperience(data.experienceYears || 1);
        setHospital(data.hospitalName || '');
        setAddress(data.clinicAddress || '');
        setFee(data.consultationFee || 500);
        setLanguages(data.languages || 'English');
      } else {
        const data = await patientService.getProfile();
        setPhone(data.phoneNumber || '');
        setAddress(data.address || '');
      }
    } catch (err) {
      console.log('Profile context not yet populated on backend store.');
    }
  };

  const handleSave = async (e) => {
    e.preventDefault();
    try {
      if (isDoctor) {
        await doctorService.updateProfile({
          fullName, specialization, qualification, experienceYears: experience,
          hospitalName: hospital, clinicAddress: address, languages, consultationFee: fee
        });
      } else {
        await patientService.updateProfile({
          fullName, phoneNumber: phone, dateOfBirth: '2000-01-01', address
        });
      }
      alert('Demographic profile updated successfully!');
    } catch (err) {
      alert('Execution failed while updating your profile fields.');
    }
  };

  return (
    <div>
      <Navbar />
      <div className="dashboard-layout">
        <Sidebar />
        <div className="main-content">
          <div className="card" style={{ maxWidth: '600px' }}>
            <h2>Edit Core Demographic Data Profile</h2>
            <form onSubmit={handleSave} style={{ marginTop: '20px' }}>
              <div className="form-group">
                <label>Display Full Name</label>
                <input type="text" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
              </div>
              {isDoctor ? (
                <>
                  <div className="form-group">
                    <label>Medical Specialization</label>
                    <input type="text" value={specialization} onChange={(e) => setSpecialization(e.target.value)} required />
                  </div>
                  <div className="form-group">
                    <label>Clinical Experience (Years)</label>
                    <input type="number" value={experience} onChange={(e) => setExperience(e.target.value)} required />
                  </div>
                  <div className="form-group">
                    <label>Hospital Affiliation</label>
                    <input type="text" value={hospital} onChange={(e) => setHospital(e.target.value)} required />
                  </div>
                  <div className="form-group">
                    <label>Consultation Session Fee ($)</label>
                    <input type="number" value={fee} onChange={(e) => setFee(e.target.value)} required />
                  </div>
                </>
              ) : (
                <div className="form-group">
                  <label>Contact Phone Number</label>
                  <input type="text" value={phone} onChange={(e) => setPhone(e.target.value)} required />
                </div>
              )}
              <div className="form-group">
                <label>{isDoctor ? 'Clinic Station Address' : 'Home Mailing Address'}</label>
                <input type="text" value={address} onChange={(e) => setAddress(e.target.value)} required />
              </div>
              <button type="submit" className="btn-primary">Save Changes</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;