import axios from "axios";
import { authStore } from "@/stores/auth";

const service = axios.create({
    baseURL: import.meta.env.VITE_TEAMAPP_BASE_URL,
    timeout: 0
});

service.interceptors.request.use(
    config => {
        const auth = authStore();
        const token = auth.token;
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        
        return config;
    },

    err => {
        return Promise.reject(err);
    }
)

//TODO interceptor de response para pagina 404, 403, 500, etc.

export default service;