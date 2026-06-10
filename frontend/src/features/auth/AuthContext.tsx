import {
  useCallback,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import { useQueryClient } from "@tanstack/react-query";
import { AuthContext, type AuthContextValue } from "@/features/auth/auth-context";
import { clearSession, getStoredSession, saveSession } from "@/lib/auth-storage";
import { authService } from "@/services/auth-service";
import type { Role } from "@/types/auth";

export function AuthProvider({ children }: { children: ReactNode }) {
  const queryClient = useQueryClient();
  const [role, setRole] = useState<Role | null>(() => getStoredSession().role);

  const logout = useCallback(() => {
    clearSession();
    setRole(null);
    queryClient.clear();
  }, [queryClient]);

  useEffect(() => {
    window.addEventListener("auth:expired", logout);
    return () => window.removeEventListener("auth:expired", logout);
  }, [logout]);

  const value = useMemo<AuthContextValue>(
    () => ({
      role,
      authenticated: Boolean(role && getStoredSession().accessToken),
      login: async (payload) => {
        const session = await authService.login(payload);
        saveSession(session);
        setRole(session.role);
        return session.role;
      },
      logout,
    }),
    [logout, role],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
