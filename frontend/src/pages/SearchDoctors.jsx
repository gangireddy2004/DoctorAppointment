import React, { useState, useEffect } from 'react';
import doctorService from '../services/doctorService';
import appointmentService from '../services/appointmentService';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';

const SearchDoctors = () => {
  const [doctors, setDoctors] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedDoctorSlots, setSelectedDoctorSlots] = useState([]);
  const [activeDoctorId, setActiveDoctorId] = useState(null);

  useEffect(() => {
    fetchInitialDirectory();
  }, []);

  const fetchInitialDirectory = async () => {
    try {
      const data = await doctorService.getDirectory();
      setDoctors(data);
    } catch (err) {
      console.error('Failed to pull provider channels.', err);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const data = await doctorService.search(searchQuery);
      setDoctors(data);
    } catch (err) {
      console.error(err);
    }
  };

  const viewSlots = async (doctorId) => {
    try {
      const data = await doctorService.getSlots(doctorId);
      setSelectedDoctorSlots(data);
      setActiveDoctorId(doctorId);
    } catch (err) {
      alert('Failed to extract unbooked calendar records.');
    }
  };

  const bookSlot = async (slotId) => {
    try {
      await appointmentService.book(slotId);
      alert('Appointment reservation submitted successfully!');
      viewSlots(activeDoctorId);
    } catch (err) {
      alert('Failed to execute target reservation context.');
    }
  };

  return (
    <div>
      <Navbar />
      <div className="dashboard-layout">
        <Sidebar />
        <div className="main-content">
          <div className="card">
            <h2>Find a Healthcare Specialist</h2>
            <form onSubmit={handleSearch} style={{ display: 'flex', gap: '15px', marginTop: '15px' }}>
              <input type="text" placeholder="Search by name, specialization, or clinic location..." 
                     value={searchQuery} onChange={(e) => setSearchQuery(e.target.value)}
                     style={{ flex: 1, padding: '12px', borderRadius: '6px', border: '1px solid #e0e0e0' }} />
              <button type="submit" className="btn-primary" style={{ width: '140px' }}>Search</button>
            </form>
          </div>

          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' }}>
            <div>
              <h3>Available Specialized Practitioners</h3>
              {doctors.map(doc => (
                <div key={doc.id} className="card" style={{ marginTop: '15px' }}>
                  <h4>{doc.fullName}</h4>
                  <p style={{ color: '#1a73e8', fontWeight: '600' }}>{doc.specialization}</p>
                  <p style={{ fontSize: '14px', color: '#666' }}>Facility: {doc.hospitalName}</p>
                  <p style={{ fontSize: '14px', color: '#666' }}>Session Charge: ${doc.consultationFee}</p>
                  <button onClick={() => viewSlots(doc.id)} style={{
                    marginTop: '12px', padding: '8px 16px', backgroundColor: '#0d1b2a', color: 'white',
                    border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: '600'
                  }}>View Open Calendar Slots</button>
                </div>
              ))}
            </div>

            <div>
              <h3>Calendar Allocation Registry</h3>
              {activeDoctorId ? (
                <div className="card" style={{ marginTop: '15px' }}>
                  {selectedDoctorSlots.length === 0 ? (
                    <p style={{ color: '#666' }}>No open time slot blocks currently listed.</p>
                  ) : (
                    selectedDoctorSlots.map(slot => (
                      <div key={slot.id} style={{
                        display: 'flex', justifyContent: 'space-between', alignItems: 'center',
                        padding: '12px', borderBottom: '1px solid #e0e0e0'
                      }}>
                        <div>
                          <strong>{slot.availableDate}</strong>
                          <p style={{ fontSize: '14px', color: '#555' }}>Window: {slot.startTime} - {slot.endTime}</p>
                        </div>
                        <button onClick={() => bookSlot(slot.id)} style={{
                          padding: '6px 12px', backgroundColor: '#2ec4b6', color: 'white',
                          border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: '600'
                        }}>Book Slot</button>
                      </div>
                    ))
                  )}
                </div>
              ) : (
                <p style={{ marginTop: '15px', color: '#666' }}>Select a doctor to inspect open scheduling blocks.</p>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchDoctors;