import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { Megaphone, Pencil, Plus, Trash2, X } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { ApiFeedback } from "@/components/ApiFeedback";
import { PageHeader } from "@/components/PageHeader";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { StatusBadge } from "@/components/StatusBadge";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { Select } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea";
import { getApiError } from "@/lib/api-error";
import { formatBloodType, formatDate, formatLocation } from "@/lib/formatters";
import { campanhaSchema, type CampanhaForm } from "@/schemas/hemocentro-schema";
import { hemocentroService } from "@/services/hemocentro-service";
import type { Campanha, StatusCampanha } from "@/types/hemocentro";
import type { TipoSanguineo } from "@/types/public";

const bloodTypes: TipoSanguineo[] = ["A_POS", "A_NEG", "B_POS", "B_NEG", "AB_POS", "AB_NEG", "O_POS", "O_NEG"];

export function CampanhasHemocentroPage() {
  const queryClient = useQueryClient();
  const [editing, setEditing] = useState<Campanha | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [status, setStatus] = useState<StatusCampanha>("AGENDADA");
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const query = useQuery({ queryKey: ["hemocentro", "campanhas"], queryFn: hemocentroService.listarCampanhas });
  const { register, handleSubmit, reset, formState: { errors } } = useForm<CampanhaForm>({
    resolver: zodResolver(campanhaSchema),
    defaultValues: { urgencia: "NORMAL", tiposSanguineosNecessarios: [] },
  });

  const refresh = () => void queryClient.invalidateQueries({ queryKey: ["hemocentro", "campanhas"] });
  const closeForm = () => {
    setEditing(null);
    setShowForm(false);
    setStatus("AGENDADA");
    reset({ urgencia: "NORMAL", tiposSanguineosNecessarios: [] });
  };
  const save = useMutation({
    mutationFn: (data: CampanhaForm) => editing
      ? hemocentroService.atualizarCampanha(editing.id, { ...data, status })
      : hemocentroService.criarCampanha(data),
    onSuccess: () => {
      setFeedback({ type: "success", message: editing ? "Campanha atualizada." : "Campanha criada." });
      closeForm();
      refresh();
    },
    onError: (error) => setFeedback({ type: "error", message: getApiError(error) }),
  });
  const remove = useMutation({
    mutationFn: hemocentroService.removerCampanha,
    onSuccess: () => {
      setFeedback({ type: "success", message: "Campanha removida." });
      refresh();
    },
    onError: (error) => setFeedback({ type: "error", message: getApiError(error) }),
  });

  const edit = (item: Campanha) => {
    setEditing(item);
    setStatus(item.status as StatusCampanha);
    setShowForm(true);
    reset({
      titulo: item.titulo,
      descricao: item.descricao || "",
      urlImagem: item.urlImagem || "",
      dataInicio: item.dataInicio,
      dataFim: item.dataFim,
      endereco: item.endereco || "",
      cidade: item.cidade || "",
      estado: item.estado || "",
      urgencia: item.urgencia,
      tiposSanguineosNecessarios: item.tiposSanguineosNecessarios,
    });
  };

  return (
    <section>
      <PageHeader
        title="Campanhas"
        description="Crie e acompanhe campanhas de doacao."
        action={<Button onClick={() => { closeForm(); setShowForm(true); }}><Plus className="size-4" /> Nova campanha</Button>}
      />
      {feedback && <div className="mb-5"><ApiFeedback {...feedback} /></div>}

      {showForm && (
        <Card className="mb-6 p-5 sm:p-7">
          <div className="mb-5 flex items-center justify-between">
            <h2 className="font-semibold">{editing ? "Editar campanha" : "Nova campanha"}</h2>
            <Button variant="ghost" className="size-9 p-0" onClick={closeForm} aria-label="Fechar"><X className="size-4" /></Button>
          </div>
          <form className="grid gap-4 sm:grid-cols-2" onSubmit={handleSubmit((data) => save.mutate(data))}>
            <FormField label="Titulo" error={errors.titulo?.message}><Input {...register("titulo")} /></FormField>
            <FormField label="URL da imagem" error={errors.urlImagem?.message}><Input type="url" {...register("urlImagem")} /></FormField>
            <div className="sm:col-span-2"><FormField label="Descricao" error={errors.descricao?.message}><Textarea {...register("descricao")} /></FormField></div>
            <FormField label="Data inicial" error={errors.dataInicio?.message}><Input type="date" {...register("dataInicio")} /></FormField>
            <FormField label="Data final" error={errors.dataFim?.message}><Input type="date" {...register("dataFim")} /></FormField>
            <FormField label="Urgencia" error={errors.urgencia?.message}>
              <Select {...register("urgencia")}><option value="NORMAL">Normal</option><option value="ALTA">Alta</option><option value="CRITICA">Critica</option></Select>
            </FormField>
            {editing && (
              <FormField label="Status">
                <Select value={status} onChange={(event) => setStatus(event.target.value as StatusCampanha)}>
                  <option value="AGENDADA">Agendada</option><option value="ATIVA">Ativa</option><option value="ENCERRADA">Encerrada</option>
                </Select>
              </FormField>
            )}
            <div className="sm:col-span-2">
              <span className="mb-2 block text-sm font-medium">Tipos sanguineos necessarios</span>
              <div className="flex flex-wrap gap-3">
                {bloodTypes.map((type) => (
                  <label key={type} className="flex items-center gap-2 rounded-md border border-border px-3 py-2 text-sm">
                    <input type="checkbox" value={type} {...register("tiposSanguineosNecessarios")} />
                    {formatBloodType(type)}
                  </label>
                ))}
              </div>
              {errors.tiposSanguineosNecessarios?.message && <span className="mt-1 block text-xs text-destructive">{errors.tiposSanguineosNecessarios.message}</span>}
            </div>
            <div className="sm:col-span-2"><FormField label="Endereco" error={errors.endereco?.message}><Input {...register("endereco")} /></FormField></div>
            <FormField label="Cidade" error={errors.cidade?.message}><Input {...register("cidade")} /></FormField>
            <FormField label="Estado" error={errors.estado?.message}><Input maxLength={2} {...register("estado")} /></FormField>
            <div className="sm:col-span-2"><Button type="submit" loading={save.isPending}><Megaphone className="size-4" /> Salvar campanha</Button></div>
          </form>
        </Card>
      )}

      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar as campanhas." />}
      {query.data?.length === 0 && <EmptyState title="Nenhuma campanha" description="Crie a primeira campanha do hemocentro." />}
      {query.data && query.data.length > 0 && (
        <div className="grid gap-4 lg:grid-cols-2">
          {query.data.map((item) => (
            <Card key={item.id} className="overflow-hidden">
              {item.urlImagem && <img src={item.urlImagem} alt="" className="h-40 w-full object-cover" />}
              <div className="p-5">
                <div className="flex items-start justify-between gap-3">
                  <div><h2 className="font-semibold">{item.titulo}</h2><p className="mt-1 text-sm text-muted-foreground">{formatDate(item.dataInicio)} a {formatDate(item.dataFim)}</p></div>
                  <StatusBadge status={item.status} />
                </div>
                {item.descricao && <p className="mt-3 text-sm text-muted-foreground">{item.descricao}</p>}
                <p className="mt-3 text-sm">{formatLocation(item.endereco, item.cidade, item.estado)}</p>
                <div className="mt-3 flex flex-wrap gap-1.5">
                  {item.tiposSanguineosNecessarios.map((type) => <span key={type} className="rounded-md bg-accent px-2 py-1 text-xs font-semibold text-accent-foreground">{formatBloodType(type)}</span>)}
                </div>
                <div className="mt-5 flex gap-2 border-t border-border pt-4">
                  <Button variant="secondary" onClick={() => edit(item)}><Pencil className="size-4" /> Editar</Button>
                  <Button variant="danger" loading={remove.isPending} onClick={() => {
                    if (window.confirm("Deseja remover esta campanha?")) remove.mutate(item.id);
                  }}><Trash2 className="size-4" /> Remover</Button>
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}
    </section>
  );
}
