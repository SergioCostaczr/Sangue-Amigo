import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { Save } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { ApiFeedback } from "@/components/ApiFeedback";
import { PageHeader } from "@/components/PageHeader";
import { ErrorState, LoadingState } from "@/components/QueryStates";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { Select } from "@/components/ui/select";
import { onlyDigits } from "@/lib/formatters";
import { atualizarUsuarioSchema, type AtualizarUsuarioForm } from "@/schemas/usuario-schema";
import { usuarioService } from "@/services/usuario-service";
import type { ApiError } from "@/types/auth";

export function PerfilUsuarioPage() {
  const queryClient = useQueryClient();
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const query = useQuery({
    queryKey: ["usuario", "perfil"],
    queryFn: usuarioService.buscarPerfil,
  });
  const { register, handleSubmit, reset, formState: { errors } } = useForm<AtualizarUsuarioForm>({
    resolver: zodResolver(atualizarUsuarioSchema),
  });

  useEffect(() => {
    if (query.data) {
      reset({
        nome: query.data.nome,
        telefone: query.data.telefone || "",
        dataNascimento: query.data.dataNascimento,
        tipoSanguineo: query.data.tipoSanguineo,
        sexo: query.data.sexo,
      });
    }
  }, [query.data, reset]);

  const atualizar = useMutation({
    mutationFn: usuarioService.atualizarPerfil,
    onSuccess: (data) => {
      queryClient.setQueryData(["usuario", "perfil"], data);
      setFeedback({ type: "success", message: "Perfil atualizado com sucesso." });
    },
    onError: (error) => {
      const message = axios.isAxiosError<ApiError>(error) ? error.response?.data?.mensagem : null;
      setFeedback({ type: "error", message: message || "Nao foi possivel atualizar o perfil." });
    },
  });

  const onSubmit = (data: AtualizarUsuarioForm) => {
    setFeedback(null);
    atualizar.mutate({
      ...data,
      telefone: data.telefone ? onlyDigits(data.telefone) : undefined,
    });
  };

  if (query.isLoading) return <LoadingState />;
  if (query.isError || !query.data) return <ErrorState message="Nao foi possivel carregar seu perfil." />;

  return (
    <section>
      <PageHeader title="Meu perfil" description="Mantenha seus dados pessoais atualizados." />
      <Card className="max-w-3xl p-5 sm:p-7">
        <div className="mb-6 grid gap-3 rounded-md bg-muted p-4 text-sm sm:grid-cols-2">
          <p><span className="text-muted-foreground">E-mail:</span> {query.data.email}</p>
          <p><span className="text-muted-foreground">CPF:</span> {query.data.cpf}</p>
        </div>
        <form className="grid gap-4 sm:grid-cols-2" onSubmit={handleSubmit(onSubmit)}>
          <FormField label="Nome" error={errors.nome?.message}><Input {...register("nome")} /></FormField>
          <FormField label="Telefone" error={errors.telefone?.message}><Input {...register("telefone")} /></FormField>
          <FormField label="Data de nascimento" error={errors.dataNascimento?.message}><Input type="date" {...register("dataNascimento")} /></FormField>
          <FormField label="Tipo sanguineo" error={errors.tipoSanguineo?.message}>
            <Select {...register("tipoSanguineo")}>
              <option value="A_POS">A+</option><option value="A_NEG">A-</option>
              <option value="B_POS">B+</option><option value="B_NEG">B-</option>
              <option value="AB_POS">AB+</option><option value="AB_NEG">AB-</option>
              <option value="O_POS">O+</option><option value="O_NEG">O-</option>
            </Select>
          </FormField>
          <FormField label="Sexo" error={errors.sexo?.message}>
            <Select {...register("sexo")}><option value="MASCULINO">Masculino</option><option value="FEMININO">Feminino</option></Select>
          </FormField>
          {feedback && <div className="sm:col-span-2"><ApiFeedback {...feedback} /></div>}
          <div className="sm:col-span-2"><Button type="submit" loading={atualizar.isPending}><Save className="size-4" /> Salvar alteracoes</Button></div>
        </form>
      </Card>
    </section>
  );
}
