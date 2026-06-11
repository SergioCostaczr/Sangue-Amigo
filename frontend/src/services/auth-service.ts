import { api } from "@/lib/api";
import type { AuthResponse, LoginPayload } from "@/types/auth";

export const authService = {
  login: async (payload: LoginPayload) => {
    const { data } = await api.post<AuthResponse>("/auth/login", payload);
    return data;
  },
};
