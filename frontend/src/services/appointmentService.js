import api from './api';

const appointmentService = {
  book: async (availabilitySlotId) => {
    const response = await api.post('/appointments/book', { availabilitySlotId });
    return response.data;
  },
  getPatientHistory: async () => {
    const response = await api.get('/appointments/patient');
    return response.data;
  },
  getDoctorSchedule: async () => {
    const response = await api.get('/appointments/doctor');
    return response.data;
  },
  updateStatus: async (id, status) => {
    const response = await api.patch(`/appointments/${id}/status?status=${status}`);
    return response.data;
  }
};

export default appointmentService;