export type TipoSanguineo =
  | "A_POS"
  | "A_NEG"
  | "B_POS"
  | "B_NEG"
  | "AB_POS"
  | "AB_NEG"
  | "O_POS"
  | "O_NEG";

export type Sexo = "MASCULINO" | "FEMININO";
export type UrgenciaCampanha = "NORMAL" | "ALTA" | "CRITICA";

export interface Hemocentro {
  id: number;
  nome: string;
  cnpj: string;
  email: string;
  telefone: string;
  endereco: string;
  cidade?: string;
  estado?: string;
}

export interface Campanha {
  id: number;
  titulo: string;
  descricao?: string;
  urlImagem?: string;
  tiposSanguineosNecessarios: TipoSanguineo[];
  dataInicio: string;
  dataFim: string;
  endereco?: string;
  cidade?: string;
  estado?: string;
  status: string;
  urgencia: UrgenciaCampanha;
  hemocentroId: number;
  nomeHemocentro: string;
  criadaEm: string;
}

export interface CadastroUsuarioPayload {
  nome: string;
  cpf: string;
  email: string;
  senha: string;
  dataNascimento: string;
  tipoSanguineo: TipoSanguineo;
  sexo: Sexo;
  telefone?: string;
}

export interface CadastroHemocentroPayload {
  nome: string;
  email: string;
  cnpj: string;
  senha: string;
  telefone: string;
  endereco: string;
  cidade?: string;
  estado?: string;
}
