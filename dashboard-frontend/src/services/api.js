import axios from 'axios';

// Configure base URL for all requests
// const API = axios.create({
//   baseURL: 'http://localhost:9090',  // Match your backend server URL
//   headers: {
//     'Content-Type': 'application/json',
//   }
// });

// const API = axios.create({
//   baseURL: 'https://dashboard-jee-and-react.onrender.com',  // URL de ton backend sur Render
//   headers: {
//     'Content-Type': 'application/json',
//   }
// });

// // Login function
// export const login = async (username, password) => {
//   try {
//     const response = await API.post('/api/auth/login', { username, password });
    
//     // If request succeeds, return user data
//     return response.data;
//   } catch (error) {
//     console.error('API Error:', error.response || error);
    
//     // Provide more detailed error information
//     if (error.response) {
//       // The server responded with a status code outside the 2xx range
//       console.log('Status:', error.response.status);
//       console.log('Data:', error.response.data);
      
//       if (error.response.status === 401) {
//         throw new Error('Invalid credentials');
//       }
//     }
    
//     // Network error or other issues
//     throw new Error(error.message || 'Login failed. Please try again.');
//   }
// };


const API = axios.create({
  baseURL: 'https://dashboard-jee-and-react.onrender.com',
  headers: { 'Content-Type': 'application/json' },
  withCredentials: true // si tu utilises cookies pour la session
});

export const login = async (username, password) => {
  try {
    const response = await API.post('/api/auth/login', { username, password });
    console.log(response.data);
    return response.data;
  } catch (error) {
    console.error('Login error:', error.response || error);
    throw error;
  }
};

// Get top clients
export const getTopClients = async (limit = 5) => {
  try {
    const response = await API.get(`/clients/top?limit=${limit}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching top clients:', error);
    throw new Error('Failed to fetch top clients');
  }
};

// Get client count
export const getClientCount = async () => {
  try {
    const response = await API.get('/clients/count');
    return response.data.count;
  } catch (error) {
    console.error('Error fetching client count:', error);
    throw new Error('Failed to fetch client count');
  }
};

// Generate clients
export const generateClients = async (count = 10000) => {
  try {
    const response = await API.post(`/clients/generate?count=${count}`);
    return response.data;
  } catch (error) {
    console.error('Error generating clients:', error);
    throw new Error('Failed to generate clients');
  }
};

// Reset clients
export const resetClients = async () => {
  try {
    const response = await API.delete('/clients');
    return response.data;
  } catch (error) {
    console.error('Error resetting clients:', error);
    throw new Error('Failed to reset clients');
  }
};

// Get client transactions - FIXED

export const getClientTransactions = async (clientId) => {
  try {
    // Vérifier si clientId existe
    if (!clientId) {
      throw new Error("Client ID is missing or undefined");
    }
    
    // Nettoyer l'ID et s'assurer qu'il s'agit d'une chaîne
    const cleanId = clientId.toString().trim();
    
    // Vérifier si l'ID est valide (24 caractères hexadécimaux)
    if (!cleanId.match(/^[0-9a-fA-F]{24}$/)) {
      console.error("Invalid ID format:", cleanId);
      throw new Error(`Invalid client ID format: ${cleanId} - Must be a valid MongoDB ObjectId`);
    }
    
    console.log("Requesting transactions for client ID:", cleanId);
    
    const response = await API.get(`/transactions/client/${cleanId}`);
    return response.data;
  } catch (error) {
    console.error("Error in getClientTransactions:", error);
    throw error;
  }
};

// Get top clients with their transactions
export const getTopClientsTransactions = async (limit = 5) => {
  try {
    const response = await API.get(`/transactions/top-clients?limit=${limit}`);
    return response.data;
  } catch (error) {
    console.error('Error fetching top clients transactions:', error);
    throw new Error('Failed to fetch top clients transactions');
  }
};