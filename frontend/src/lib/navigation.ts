import type { Role } from "@/types/auth";

export function roleHome(role: Role | null) {
  return role === "ROLE_HEMOCENTRO"
    ? "/hemocentro/painel"
    : "/usuario/agendamentos";
}
