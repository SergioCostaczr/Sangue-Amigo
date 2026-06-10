import { api } from "@/lib/api";
import type {
  Agendamento,
  AtualizarUsuarioPayload,
  Doacao,
  HorarioDisponivel,
  Usuario,
} from "@/types/usuario";

export const usuarioService = {
  buscarPerfil: async () => {
    const { data } = await api.get<Usuario>("/usuarios/perfil");
    return data;
  },
  atualizarPerfil: async (payload: AtualizarUsuarioPayload) => {
    const { data } = await api.put<Usuario>("/usuarios/perfil", payload);
    return data;
  },
  listarHorarios: async (hemocentroId: number, data: string) => {
    const response = await api.get<HorarioDisponivel[]>(
      `/hemocentros/${hemocentroId}/horarios`,
      { params: { data } },
    );
    return response.data;
  },
  criarAgendamento: async (horarioId: number) => {
    const { data } = await api.post<Agendamento>("/agendamentos", { horarioId });
    return data;
  },
  listarAgendamentos: async () => {
    const { data } = await api.get<Agendamento[]>("/agendamentos");
    return data;
  },
  confirmarAgendamento: async (id: number) => {
    const { data } = await api.patch<Agendamento>(`/agendamentos/${id}/confirmar`);
    return data;
  },
  cancelarAgendamento: (id: number) =>
    api.patch(`/agendamentos/${id}/cancelar`),
  buscarQrCode: async (id: number) => {
    const { data } = await api.get<string>(`/agendamentos/${id}/qrcode`);
    return data;
  },
  listarDoacoes: async () => {
    const { data } = await api.get<Doacao[]>("/doacoes/historico");
    return data;
  },
};
