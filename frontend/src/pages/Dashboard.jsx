import React, { useContext, useEffect, useState } from 'react';
import { AuthContext } from '../context/AuthContext';
import appointmentService from '../services/appointmentService';
import Navbar from '../components/Navbar';
import Sidebar from '../components/Sidebar';

const Dashboard = () => {
  const { user } = useContext(AuthContext);
  const [appointments, setAppointments] = useState([]);
  const [slotDate, setSlotDate] = useState('');
  const [slotStart, setSlotStart] = useState('');
  const [slotEnd, setSlotEnd] = useState('');
  const isDoctor = user?.roles?.includes('ROLE_DOCTOR');

  useEffect(() => {
    loadAppointments();
  }, []);

  const loadAppointments = async () => {
    try {
      const data = isDoctor 
        ? await appointmentService.getDoctorSchedule() 
        : await appointmentService.getPatientHistory();
      setAppointments(data);
    } catch (err) {
      console.error('Failed to resolve appointment histories.', err);
    }
  };

  const handleStatusChange = async (id, targetStatus) => {
    try {
      await appointmentService.updateStatus(id, targetStatus);
      loadAppointments();
    } catch (err) {
      alert('Failed to transition status metrics.');
    }
  };

  return (
    <div>
      <Navbar />
      <div className="dashboard-layout">
        <Sidebar />
        <div className="main-content">
          <div className="card">
            <h2>{isDoctor ? 'Clinical Consultation Queue' : 'Your Slotted Consultations'}</h2>
            <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid #e0e0e0', textAlign: 'left' }}>
                  <th style={{ padding: '12px' }}>{isDoctor ? 'Patient Name' : 'Doctor Name'}</th>
                  <th style={{ padding: '12px' }}>Date</th>
                  <th style={{ padding: '12px' }}>Time Frame</th>
                  <th style={{ padding: '12px' }}>Workflow Status</th>
                  <th style={{ padding: '12px' }}>Telehealth Room Access</th>
                </tr>
              </thead>
              <tbody>
                {appointments.map((appt) => (
                  <tr key={appt.id} style={{ borderBottom: '1px solid #e0e0e0' }}>
                    <td style={{ padding: '12px' }}>{isDoctor ? appt.patientName : appt.doctorName}</td>
                    <td style={{ padding: '12px' }}>{appt.appointmentDate}</td>
                    <td style={{ padding: '12px' }}>{`${appt.startTime} - ${appt.endTime}`}</td>
                    <td style={{ padding: '12px', fontWeight: 'bold' }}>{appt.status}</td>
                    <td style={{ padding: '12px' }}>
                      {appt.status === 'CONFIRMED' && appt.virtualMeetingUrl ? (
                        <a href={appt.virtualMeetingUrl} target="_blank" rel="noreferrer" style={{
                          padding: '6px 12px', backgroundColor: '#2ec4b6', color: 'white',
                          borderRadius: '4px', textDecoration: 'none', fontSize: '14px', fontWeight: '600'
                        }}>Join Jitsi Room</a>
                      ) : isDoctor && appt.status === 'PENDING' ? (
                        <div style={{ display: 'flex', gap: '10px' }}>
                          <button onClick={() => handleStatusChange(appt.id, 'CONFIRMED')} style={{
                            backgroundColor: '#1a73e8', color: 'white', border: 'none', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer'
                          }}>Approve</button>
                          <button onClick={() => handleStatusChange(appt.id, 'REJECTED')} style={{
                            backgroundColor: '#e63946', color: 'white', border: 'none', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer'
                          }}>Reject</button>
                        </div>
                      ) : (
                        <span style={{ color: '#999', fontSize: '14px' }}>Awaiting Approval</span>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;