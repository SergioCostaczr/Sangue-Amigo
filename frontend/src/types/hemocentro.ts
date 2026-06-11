import type { Campanha, Hemocentro, TipoSanguineo, UrgenciaCampanha } from "@/types/public";
import type { Agendamento, Doacao, HorarioDisponivel } from "@/types/usuario";

export type StatusCampanha = "AGENDADA" | "ATIVA" | "ENCERRADA";

export interface AtualizarHemocentroPayload {
  nome: string;
  telefone: string;
  endereco: string;
  cidade?: string;
  estado?: string;
}

export interface HorarioPayload {
  data: string;
  hora: string;
  vagas: number;
  disponivel?: boolean;
}

export interface CampanhaPayload {
  titulo: string;
  descricao?: string;
  urlImagem?: string;
  tiposSanguineosNecessarios: TipoSanguineo[];
  dataInicio: string;
  dataFim: string;
  endereco?: string;
  cidade?: string;
  estado?: string;
  urgencia: UrgenciaCampanha;
  status?: StatusCampanha;
}

export interface ValidacaoQrCode {
  doacaoId: number;
  nomeUsuario: string;
  tipoSanguineo: string;
  dataDoacao: string;
}

export type {
  Agendamento,
  Campanha,
  Doacao,
  Hemocentro,
  HorarioDisponivel,
};
