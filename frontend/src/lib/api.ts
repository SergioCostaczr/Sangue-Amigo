import axios, { AxiosError, type InternalAxiosRequestConfig } from "axios";
import { clearSession, getStoredSession, updateAccessToken } from "@/lib/auth-storage";
import type { AuthResponse } from "@/types/auth";

const baseURL = import.meta.env.VITE_API_URL || "http://localhost:8080";

export const api = axios.create({ baseURL });
const refreshApi = axios.create({ baseURL });

api.interceptors.request.use((config) => {
  const { accessToken } = getStoredSession();

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

let refreshPromise: Promise<string> | null = null;

api.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as
      | (InternalAxiosRequestConfig & { _retry?: boolean })
      | undefined;

    if (
      error.response?.status !== 401
      || !originalRequest
      || originalRequest._retry
      || originalRequest.url?.startsWith("/auth/")
    ) {
      return Promise.reject(error);
    }

    const { refreshToken } = getStoredSession();

    if (!refreshToken) {
      clearSession();
      window.dispatchEvent(new Event("auth:expired"));
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    try {
      refreshPromise ??= refreshApi
        .post<AuthResponse>("/auth/refresh", { refreshToken })
        .then(({ data }) => {
          updateAccessToken(data.accessToken);
          return data.accessToken;
        })
        .finally(() => {
          refreshPromise = null;
        });

      const newAccessToken = await refreshPromise;
      originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;

      return api(originalRequest);
    } catch (refreshError) {
      clearSession();
      window.dispatchEvent(new Event("auth:expired"));
      return Promise.reject(refreshError);
    }
  },
);
