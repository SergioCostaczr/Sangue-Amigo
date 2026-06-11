import { api } from "@/lib/api";
import type {
  Agendamento,
  AtualizarHemocentroPayload,
  Campanha,
  CampanhaPayload,
  Doacao,
  Hemocentro,
  HorarioDisponivel,
  HorarioPayload,
  ValidacaoQrCode,
} from "@/types/hemocentro";

export const hemocentroService = {
  buscarPerfil: async () => (await api.get<Hemocentro>("/hemocentros/perfil")).data,
  atualizarPerfil: async (payload: AtualizarHemocentroPayload) =>
    (await api.put<Hemocentro>("/hemocentros/perfil", payload)).data,

  listarHorarios: async (inicio: string, fim: string) =>
    (await api.get<HorarioDisponivel[]>("/hemocentros/horarios", { params: { inicio, fim } })).data,
  criarHorario: async (payload: HorarioPayload) =>
    (await api.post<HorarioDisponivel>("/hemocentros/horarios", payload)).data,
  atualizarHorario: async (id: number, payload: Required<HorarioPayload>) =>
    (await api.put<HorarioDisponivel>(`/hemocentros/horarios/${id}`, payload)).data,
  removerHorario: (id: number) => api.delete(`/hemocentros/horarios/${id}`),

  listarCampanhas: async () => (await api.get<Campanha[]>("/campanhas/minhas")).data,
  criarCampanha: async (payload: CampanhaPayload) =>
    (await api.post<Campanha>("/campanhas", payload)).data,
  atualizarCampanha: async (id: number, payload: CampanhaPayload & { status: string }) =>
    (await api.put<Campanha>(`/campanhas/${id}`, payload)).data,
  removerCampanha: (id: number) => api.delete(`/campanhas/${id}`),

  listarAgendamentos: async (data: string) =>
    (await api.get<Agendamento[]>("/agendamentos/hemocentro", { params: { data } })).data,
  validarQrCode: async (qrCodeToken: string) =>
    (await api.post<ValidacaoQrCode>("/agendamentos/validar-qrcode", { qrCodeToken })).data,
  listarDoacoes: async () => (await api.get<Doacao[]>("/doacoes/hemocentro")).data,
};
