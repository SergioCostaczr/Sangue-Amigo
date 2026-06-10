import type { Sexo, TipoSanguineo } from "@/types/public";

export type StatusAgendamento = "PENDENTE" | "CONFIRMADO" | "CANCELADO" | "CONCLUIDO";

export interface Usuario {
  id: number;
  nome: string;
  cpf: string;
  email: string;
  telefone?: string;
  dataNascimento: string;
  tipoSanguineo: TipoSanguineo;
  sexo: Sexo;
}

export interface AtualizarUsuarioPayload {
  nome: string;
  telefone?: string;
  dataNascimento: string;
  tipoSanguineo: TipoSanguineo;
  sexo: Sexo;
}

export interface HorarioDisponivel {
  id: number;
  data: string;
  hora: string;
  vagas: number;
  disponivel: boolean;
  hemocentroId: number;
  nomeHemocentro: string;
}

export interface Agendamento {
  id: number;
  data: string;
  horario: string;
  status: StatusAgendamento;
  qrCodeToken?: string;
  nomeHemocentro: string;
  enderecoHemocentro: string;
}

export interface Doacao {
  id: number;
  dataDoacao: string;
  observacoes?: string;
  agendamentoId: number;
  nomeUsuario: string;
  nomeHemocentro: string;
}
