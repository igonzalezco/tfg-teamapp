import axios from "axios";
import { authStore } from "@/stores/auth";

const service = axios.create({
    baseURL: process.env.TEAMAPP_BASE_URL,
    timeout: 0
});

export default service;