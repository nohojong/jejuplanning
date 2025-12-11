import axios from 'axios';

const api = axios.create({
    baseURL: '/',

    withCredentials: true,

    header: {
        'Content-Type': 'application/json',
    }
});

export default api;