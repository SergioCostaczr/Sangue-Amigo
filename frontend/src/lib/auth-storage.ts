import type { AuthResponse, Role } from "@/types/auth";

const ACCESS_TOKEN_KEY = "sangue-amigo:access-token";
const REFRESH_TOKEN_KEY = "sangue-amigo:refresh-token";
const ROLE_KEY = "sangue-amigo:role";

export interface StoredSession {
  accessToken: string | null;
  refreshToken: string | null;
  role: Role | null;
}

export function getStoredSession(): StoredSession {
  return {
    accessToken: localStorage.getItem(ACCESS_TOKEN_KEY),
    refreshToken: localStorage.getItem(REFRESH_TOKEN_KEY),
    role: localStorage.getItem(ROLE_KEY) as Role | null,
  };
}

export function saveSession(session: AuthResponse) {
  localStorage.setItem(ACCESS_TOKEN_KEY, session.accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY, session.refreshToken);
  localStorage.setItem(ROLE_KEY, session.role);
}

export function updateAccessToken(accessToken: string) {
  localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
}

export function clearSession() {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
  localStorage.removeItem(ROLE_KEY);
}
