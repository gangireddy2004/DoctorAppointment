import api from './api';

const doctorService = {
  getProfile: async () => {
    const response = await api.get('/doctors/profile');
    return response.data;
  },
  updateProfile: async (profileData) => {
    const response = await api.put('/doctors/profile', profileData);
    return response.data;
  },
  getDirectory: async () => {
    const response = await api.get('/doctors/directory');
    return response.data;
  },
  search: async (query) => {
    const response = await api.get(`/doctors/search?query=${query}`);
    return response.data;
  },
  addSlot: async (slotData) => {
    const response = await api.post('/doctors/availability', slotData);
    return response.data;
  },
  getSlots: async (doctorId) => {
    const response = await api.get(`/doctors/${doctorId}/slots`);
    return response.data;
  }
};

export default doctorService;