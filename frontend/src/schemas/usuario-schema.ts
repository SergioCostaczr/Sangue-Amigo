import { z } from "zod";

export const atualizarUsuarioSchema = z.object({
  nome: z.string().trim().min(3, "Informe seu nome completo."),
  telefone: z.string().optional(),
  dataNascimento: z.string().min(1, "Informe a data de nascimento."),
  tipoSanguineo: z.enum(["A_POS", "A_NEG", "B_POS", "B_NEG", "AB_POS", "AB_NEG", "O_POS", "O_NEG"]),
  sexo: z.enum(["MASCULINO", "FEMININO"]),
});

export type AtualizarUsuarioForm = z.infer<typeof atualizarUsuarioSchema>;
