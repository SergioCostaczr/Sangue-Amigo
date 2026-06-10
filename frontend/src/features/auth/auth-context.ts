import { createContext } from "react";
import type { LoginPayload, Role } from "@/types/auth";

export interface AuthContextValue {
  authenticated: boolean;
  role: Role | null;
  login: (payload: LoginPayload) => Promise<Role>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue | null>(null);
