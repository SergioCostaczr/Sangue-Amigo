import { z } from "zod";
import { isValidCnpj, isValidCpf } from "@/lib/formatters";

const password = z.string().min(6, "A senha deve ter pelo menos 6 caracteres.");

export const cadastroUsuarioSchema = z.object({
  nome: z.string().trim().min(3, "Informe seu nome completo."),
  cpf: z.string().refine(isValidCpf, "Informe um CPF valido."),
  email: z.email("Informe um e-mail valido."),
  senha: password,
  dataNascimento: z.string().min(1, "Informe a data de nascimento."),
  tipoSanguineo: z.enum(["A_POS", "A_NEG", "B_POS", "B_NEG", "AB_POS", "AB_NEG", "O_POS", "O_NEG"]),
  sexo: z.enum(["MASCULINO", "FEMININO"]),
  telefone: z.string().optional(),
});

export const cadastroHemocentroSchema = z.object({
  nome: z.string().trim().min(3, "Informe o nome do hemocentro."),
  cnpj: z.string().refine(isValidCnpj, "Informe um CNPJ valido."),
  email: z.email("Informe um e-mail valido."),
  senha: password,
  telefone: z.string().min(8, "Informe um telefone valido."),
  endereco: z.string().trim().min(5, "Informe o endereco."),
  cidade: z.string().optional(),
  estado: z.string().max(2, "Use a sigla do estado.").optional(),
});

export const recuperarSenhaSchema = z.object({
  email: z.email("Informe um e-mail valido."),
});

export const redefinirSenhaSchema = z.object({
  token: z.string().min(1, "Informe o token recebido por e-mail."),
  novaSenha: password,
  confirmarSenha: z.string(),
}).refine((data) => data.novaSenha === data.confirmarSenha, {
  message: "As senhas devem ser iguais.",
  path: ["confirmarSenha"],
});

export type CadastroUsuarioForm = z.infer<typeof cadastroUsuarioSchema>;
export type CadastroHemocentroForm = z.infer<typeof cadastroHemocentroSchema>;
export type RecuperarSenhaForm = z.infer<typeof recuperarSenhaSchema>;
export type RedefinirSenhaForm = z.infer<typeof redefinirSenhaSchema>;
