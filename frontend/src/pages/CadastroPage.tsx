import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { Building2, CheckCircle2, UserRound } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link } from "react-router-dom";
import { ApiFeedback } from "@/components/ApiFeedback";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { Select } from "@/components/ui/select";
import { onlyDigits } from "@/lib/formatters";
import {
  cadastroHemocentroSchema,
  cadastroUsuarioSchema,
  type CadastroHemocentroForm,
  type CadastroUsuarioForm,
} from "@/schemas/public-schema";
import { publicService } from "@/services/public-service";
import type { ApiError } from "@/types/auth";

type AccountType = "usuario" | "hemocentro";

function errorMessage(error: unknown) {
  if (axios.isAxiosError<ApiError>(error)) {
    return error.response?.data?.mensagem || "Nao foi possivel concluir o cadastro.";
  }
  return "Nao foi possivel concluir o cadastro.";
}

function UsuarioForm() {
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<CadastroUsuarioForm>({
    resolver: zodResolver(cadastroUsuarioSchema),
    defaultValues: {
      nome: "",
      cpf: "",
      email: "",
      senha: "",
      dataNascimento: "",
      tipoSanguineo: "O_POS",
      sexo: "MASCULINO",
      telefone: "",
    },
  });

  const onSubmit = async (data: CadastroUsuarioForm) => {
    setFeedback(null);
    try {
      await publicService.cadastrarUsuario({
        ...data,
        cpf: onlyDigits(data.cpf),
        telefone: data.telefone ? onlyDigits(data.telefone) : undefined,
      });
      reset();
      setFeedback({ type: "success", message: "Cadastro realizado. Agora voce ja pode entrar." });
    } catch (error) {
      setFeedback({ type: "error", message: errorMessage(error) });
    }
  };

  return (
    <form className="grid gap-4 sm:grid-cols-2" onSubmit={handleSubmit(onSubmit)} noValidate>
      <FormField label="Nome completo" error={errors.nome?.message}>
        <Input autoComplete="name" {...register("nome")} />
      </FormField>
      <FormField label="CPF" error={errors.cpf?.message}>
        <Input inputMode="numeric" placeholder="Somente numeros ou formatado" {...register("cpf")} />
      </FormField>
      <FormField label="E-mail" error={errors.email?.message}>
        <Input type="email" autoComplete="email" {...register("email")} />
      </FormField>
      <FormField label="Senha" error={errors.senha?.message}>
        <Input type="password" autoComplete="new-password" {...register("senha")} />
      </FormField>
      <FormField label="Data de nascimento" error={errors.dataNascimento?.message}>
        <Input type="date" {...register("dataNascimento")} />
      </FormField>
      <FormField label="Telefone" error={errors.telefone?.message}>
        <Input type="tel" autoComplete="tel" {...register("telefone")} />
      </FormField>
      <FormField label="Tipo sanguineo" error={errors.tipoSanguineo?.message}>
        <Select {...register("tipoSanguineo")}>
          <option value="A_POS">A+</option><option value="A_NEG">A-</option>
          <option value="B_POS">B+</option><option value="B_NEG">B-</option>
          <option value="AB_POS">AB+</option><option value="AB_NEG">AB-</option>
          <option value="O_POS">O+</option><option value="O_NEG">O-</option>
        </Select>
      </FormField>
      <FormField label="Sexo" error={errors.sexo?.message}>
        <Select {...register("sexo")}>
          <option value="MASCULINO">Masculino</option>
          <option value="FEMININO">Feminino</option>
        </Select>
      </FormField>
      {feedback && <div className="sm:col-span-2"><ApiFeedback {...feedback} /></div>}
      <div className="sm:col-span-2">
        <Button type="submit" loading={isSubmitting} className="w-full sm:w-auto">
          <CheckCircle2 className="size-4" /> Cadastrar doador
        </Button>
      </div>
    </form>
  );
}

function HemocentroForm() {
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<CadastroHemocentroForm>({
    resolver: zodResolver(cadastroHemocentroSchema),
    defaultValues: {
      nome: "",
      cnpj: "",
      email: "",
      senha: "",
      telefone: "",
      endereco: "",
      cidade: "",
      estado: "",
    },
  });

  const onSubmit = async (data: CadastroHemocentroForm) => {
    setFeedback(null);
    try {
      await publicService.cadastrarHemocentro({
        ...data,
        cnpj: onlyDigits(data.cnpj),
        telefone: onlyDigits(data.telefone),
        estado: data.estado?.toUpperCase(),
      });
      reset();
      setFeedback({ type: "success", message: "Hemocentro cadastrado. A conta ja pode acessar o painel." });
    } catch (error) {
      setFeedback({ type: "error", message: errorMessage(error) });
    }
  };

  return (
    <form className="grid gap-4 sm:grid-cols-2" onSubmit={handleSubmit(onSubmit)} noValidate>
      <FormField label="Nome do hemocentro" error={errors.nome?.message}>
        <Input {...register("nome")} />
      </FormField>
      <FormField label="CNPJ" error={errors.cnpj?.message}>
        <Input inputMode="numeric" placeholder="Somente numeros ou formatado" {...register("cnpj")} />
      </FormField>
      <FormField label="E-mail" error={errors.email?.message}>
        <Input type="email" autoComplete="email" {...register("email")} />
      </FormField>
      <FormField label="Senha" error={errors.senha?.message}>
        <Input type="password" autoComplete="new-password" {...register("senha")} />
      </FormField>
      <FormField label="Telefone" error={errors.telefone?.message}>
        <Input type="tel" {...register("telefone")} />
      </FormField>
      <FormField label="Endereco" error={errors.endereco?.message}>
        <Input {...register("endereco")} />
      </FormField>
      <FormField label="Cidade" error={errors.cidade?.message}>
        <Input {...register("cidade")} />
      </FormField>
      <FormField label="Estado" error={errors.estado?.message}>
        <Input maxLength={2} placeholder="UF" {...register("estado")} />
      </FormField>
      {feedback && <div className="sm:col-span-2"><ApiFeedback {...feedback} /></div>}
      <div className="sm:col-span-2">
        <Button type="submit" loading={isSubmitting} className="w-full sm:w-auto">
          <CheckCircle2 className="size-4" /> Cadastrar hemocentro
        </Button>
      </div>
    </form>
  );
}

export function CadastroPage() {
  const [type, setType] = useState<AccountType>("usuario");

  return (
    <section className="page-container py-12">
      <div className="mx-auto max-w-4xl">
        <header className="mb-7">
          <h1 className="text-3xl font-bold text-foreground">Crie sua conta</h1>
          <p className="mt-2 text-muted-foreground">Escolha o perfil e informe os dados solicitados.</p>
        </header>

        <div className="mb-5 grid grid-cols-2 rounded-lg border border-border bg-muted p-1">
          <button
            type="button"
            onClick={() => setType("usuario")}
            className={`flex items-center justify-center gap-2 rounded-md px-3 py-2.5 text-sm font-semibold transition ${type === "usuario" ? "bg-card text-primary shadow-sm" : "text-muted-foreground"}`}
          >
            <UserRound className="size-4" /> Doador
          </button>
          <button
            type="button"
            onClick={() => setType("hemocentro")}
            className={`flex items-center justify-center gap-2 rounded-md px-3 py-2.5 text-sm font-semibold transition ${type === "hemocentro" ? "bg-card text-primary shadow-sm" : "text-muted-foreground"}`}
          >
            <Building2 className="size-4" /> Hemocentro
          </button>
        </div>

        <Card className="p-5 sm:p-7">
          {type === "usuario" ? <UsuarioForm /> : <HemocentroForm />}
        </Card>

        <p className="mt-5 text-center text-sm text-muted-foreground">
          Ja possui uma conta? <Link to="/login" className="font-semibold text-primary hover:underline">Entrar</Link>
        </p>
      </div>
    </section>
  );
}
