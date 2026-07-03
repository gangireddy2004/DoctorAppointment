import api from './api';

const authService = {
  signin: async (email, password) => {
    const response = await api.post('/auth/signin', { email, password });
    return response.data;
  },
  signup: async (fullName, email, password, role) => {
    const response = await api.post('/auth/signup', { 
      fullName, 
      email, 
      password, 
      role: [role] 
    });
    return response.data;
  }
};

export default authService;