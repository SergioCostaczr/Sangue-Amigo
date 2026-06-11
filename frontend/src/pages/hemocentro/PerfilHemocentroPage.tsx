import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
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
import { getApiError } from "@/lib/api-error";
import { onlyDigits } from "@/lib/formatters";
import { perfilHemocentroSchema, type PerfilHemocentroForm } from "@/schemas/hemocentro-schema";
import { hemocentroService } from "@/services/hemocentro-service";

export function PerfilHemocentroPage() {
  const queryClient = useQueryClient();
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const query = useQuery({ queryKey: ["hemocentro", "perfil"], queryFn: hemocentroService.buscarPerfil });
  const { register, handleSubmit, reset, formState: { errors } } = useForm<PerfilHemocentroForm>({
    resolver: zodResolver(perfilHemocentroSchema),
  });

  useEffect(() => {
    if (query.data) {
      reset({
        nome: query.data.nome,
        telefone: query.data.telefone,
        endereco: query.data.endereco,
        cidade: query.data.cidade || "",
        estado: query.data.estado || "",
      });
    }
  }, [query.data, reset]);

  const mutation = useMutation({
    mutationFn: hemocentroService.atualizarPerfil,
    onSuccess: (data) => {
      queryClient.setQueryData(["hemocentro", "perfil"], data);
      setFeedback({ type: "success", message: "Perfil atualizado com sucesso." });
    },
    onError: (error) => setFeedback({ type: "error", message: getApiError(error) }),
  });

  if (query.isLoading) return <LoadingState />;
  if (query.isError || !query.data) return <ErrorState message="Nao foi possivel carregar o perfil." />;

  return (
    <section>
      <PageHeader title="Perfil do hemocentro" description="Mantenha os dados da unidade atualizados." />
      <Card className="max-w-3xl p-5 sm:p-7">
        <div className="mb-6 grid gap-3 rounded-md bg-muted p-4 text-sm sm:grid-cols-2">
          <p><span className="text-muted-foreground">E-mail:</span> {query.data.email}</p>
          <p><span className="text-muted-foreground">CNPJ:</span> {query.data.cnpj}</p>
        </div>
        <form className="grid gap-4 sm:grid-cols-2" onSubmit={handleSubmit((data) => {
          setFeedback(null);
          mutation.mutate({ ...data, telefone: onlyDigits(data.telefone) });
        })}>
          <div className="sm:col-span-2"><FormField label="Nome" error={errors.nome?.message}><Input {...register("nome")} /></FormField></div>
          <FormField label="Telefone" error={errors.telefone?.message}><Input {...register("telefone")} /></FormField>
          <FormField label="Estado" error={errors.estado?.message}><Input maxLength={2} {...register("estado")} /></FormField>
          <div className="sm:col-span-2"><FormField label="Endereco" error={errors.endereco?.message}><Input {...register("endereco")} /></FormField></div>
          <FormField label="Cidade" error={errors.cidade?.message}><Input {...register("cidade")} /></FormField>
          {feedback && <div className="sm:col-span-2"><ApiFeedback {...feedback} /></div>}
          <div className="sm:col-span-2"><Button type="submit" loading={mutation.isPending}><Save className="size-4" /> Salvar alteracoes</Button></div>
        </form>
      </Card>
    </section>
  );
}
