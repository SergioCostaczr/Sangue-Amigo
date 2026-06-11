import { z } from "zod";

export const perfilHemocentroSchema = z.object({
  nome: z.string().trim().min(3, "Informe o nome."),
  telefone: z.string().min(8, "Informe o telefone."),
  endereco: z.string().trim().min(5, "Informe o endereco."),
  cidade: z.string().optional(),
  estado: z.string().max(2, "Use a sigla do estado.").optional(),
});

export const horarioSchema = z.object({
  data: z.string().min(1, "Informe a data."),
  hora: z.string().min(1, "Informe a hora."),
  vagas: z.number().int().min(1, "Informe ao menos uma vaga."),
});

export const campanhaSchema = z.object({
  titulo: z.string().trim().min(3, "Informe o titulo."),
  descricao: z.string().max(500, "Use no maximo 500 caracteres.").optional(),
  urlImagem: z.string().optional(),
  dataInicio: z.string().min(1, "Informe a data inicial."),
  dataFim: z.string().min(1, "Informe a data final."),
  endereco: z.string().optional(),
  cidade: z.string().optional(),
  estado: z.string().max(2, "Use a sigla do estado.").optional(),
  urgencia: z.enum(["NORMAL", "ALTA", "CRITICA"]),
  tiposSanguineosNecessarios: z.array(
    z.enum(["A_POS", "A_NEG", "B_POS", "B_NEG", "AB_POS", "AB_NEG", "O_POS", "O_NEG"]),
  ).min(1, "Selecione ao menos um tipo sanguineo."),
}).refine((data) => data.dataFim >= data.dataInicio, {
  message: "A data final deve ser igual ou posterior a inicial.",
  path: ["dataFim"],
});

export type PerfilHemocentroForm = z.infer<typeof perfilHemocentroSchema>;
export type HorarioForm = z.infer<typeof horarioSchema>;
export type CampanhaForm = z.infer<typeof campanhaSchema>;
