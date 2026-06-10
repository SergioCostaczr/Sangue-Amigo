export type Role = "ROLE_USUARIO" | "ROLE_HEMOCENTRO";

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  role: Role;
}

export interface LoginPayload {
  email: string;
  senha: string;
}

export interface ApiError {
  status: number;
  erro: string;
  mensagem: string;
  timestamp: string;
}
