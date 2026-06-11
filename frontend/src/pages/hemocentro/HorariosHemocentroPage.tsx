import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { addDays, format } from "date-fns";
import { CalendarPlus, Pencil, Plus, Trash2, X } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { ApiFeedback } from "@/components/ApiFeedback";
import { PageHeader } from "@/components/PageHeader";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { getApiError } from "@/lib/api-error";
import { formatDate } from "@/lib/formatters";
import { horarioSchema, type HorarioForm } from "@/schemas/hemocentro-schema";
import { hemocentroService } from "@/services/hemocentro-service";
import type { HorarioDisponivel } from "@/types/hemocentro";

const today = format(new Date(), "yyyy-MM-dd");
const inThirtyDays = format(addDays(new Date(), 30), "yyyy-MM-dd");

export function HorariosHemocentroPage() {
  const queryClient = useQueryClient();
  const [inicio, setInicio] = useState(today);
  const [fim, setFim] = useState(inThirtyDays);
  const [showForm, setShowForm] = useState(false);
  const [editing, setEditing] = useState<HorarioDisponivel | null>(null);
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const { register, handleSubmit, reset, formState: { errors } } = useForm<HorarioForm>({
    resolver: zodResolver(horarioSchema),
    defaultValues: { data: today, hora: "09:00", vagas: 1 },
  });
  const query = useQuery({
    queryKey: ["hemocentro", "horarios", inicio, fim],
    queryFn: () => hemocentroService.listarHorarios(inicio, fim),
    enabled: Boolean(inicio && fim),
  });

  const refresh = () => void queryClient.invalidateQueries({ queryKey: ["hemocentro", "horarios"] });
  const closeForm = () => {
    setShowForm(false);
    setEditing(null);
    reset({ data: today, hora: "09:00", vagas: 1 });
  };
  const save = useMutation({
    mutationFn: (data: HorarioForm) => editing
      ? hemocentroService.atualizarHorario(editing.id, { ...data, disponivel: editing.disponivel })
      : hemocentroService.criarHorario(data),
    onSuccess: () => {
      setFeedback({ type: "success", message: editing ? "Horario atualizado." : "Horario criado." });
      closeForm();
      refresh();
    },
    onError: (error) => setFeedback({ type: "error", message: getApiError(error) }),
  });
  const remove = useMutation({
    mutationFn: hemocentroService.removerHorario,
    onSuccess: () => {
      setFeedback({ type: "success", message: "Horario removido." });
      refresh();
    },
    onError: (error) => setFeedback({ type: "error", message: getApiError(error) }),
  });

  const edit = (item: HorarioDisponivel) => {
    setEditing(item);
    setShowForm(true);
    reset({ data: item.data, hora: item.hora.slice(0, 5), vagas: item.vagas });
  };

  return (
    <section>
      <PageHeader
        title="Horarios disponiveis"
        description="Organize datas, horarios e quantidade de vagas."
        action={<Button onClick={() => { closeForm(); setShowForm(true); }}><Plus className="size-4" /> Novo horario</Button>}
      />
      {feedback && <div className="mb-5"><ApiFeedback {...feedback} /></div>}

      {showForm && (
        <Card className="mb-6 p-5">
          <div className="mb-4 flex items-center justify-between">
            <h2 className="font-semibold">{editing ? "Editar horario" : "Novo horario"}</h2>
            <Button variant="ghost" className="size-9 p-0" onClick={closeForm} aria-label="Fechar"><X className="size-4" /></Button>
          </div>
          <form className="grid gap-4 sm:grid-cols-3 lg:grid-cols-[1fr_1fr_1fr_auto]" onSubmit={handleSubmit((data) => save.mutate(data))}>
            <FormField label="Data" error={errors.data?.message}><Input type="date" {...register("data")} /></FormField>
            <FormField label="Hora" error={errors.hora?.message}><Input type="time" {...register("hora")} /></FormField>
            <FormField label="Vagas" error={errors.vagas?.message}><Input type="number" min={1} {...register("vagas", { valueAsNumber: true })} /></FormField>
            <Button className="self-end" type="submit" loading={save.isPending}><CalendarPlus className="size-4" /> Salvar</Button>
          </form>
        </Card>
      )}

      <div className="mb-5 flex flex-col gap-3 sm:flex-row sm:items-end">
        <FormField label="Inicio"><Input type="date" value={inicio} onChange={(event) => setInicio(event.target.value)} /></FormField>
        <FormField label="Fim"><Input type="date" value={fim} onChange={(event) => setFim(event.target.value)} /></FormField>
      </div>

      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar os horarios." />}
      {query.data?.length === 0 && <EmptyState title="Nenhum horario no periodo" description="Crie um horario ou altere o periodo da consulta." />}
      {query.data && query.data.length > 0 && (
        <Card className="overflow-hidden">
          <div className="hidden grid-cols-[140px_100px_100px_130px_1fr] gap-4 border-b border-border bg-muted px-5 py-3 text-xs font-semibold uppercase text-muted-foreground md:grid">
            <span>Data</span><span>Hora</span><span>Vagas</span><span>Situacao</span><span className="text-right">Acoes</span>
          </div>
          {query.data.map((item) => (
            <div key={item.id} className="grid gap-2 border-b border-border px-5 py-4 last:border-0 md:grid-cols-[140px_100px_100px_130px_1fr] md:items-center">
              <span className="font-medium">{formatDate(item.data)}</span>
              <span>{item.hora.slice(0, 5)}</span>
              <span className="text-sm text-muted-foreground">{item.vagas}</span>
              <span className={item.disponivel ? "text-sm font-medium text-emerald-700" : "text-sm text-muted-foreground"}>{item.disponivel ? "Disponivel" : "Indisponivel"}</span>
              <div className="flex gap-2 md:justify-end">
                <Button variant="secondary" className="size-9 p-0" onClick={() => edit(item)} aria-label="Editar horario"><Pencil className="size-4" /></Button>
                <Button variant="danger" className="size-9 p-0" loading={remove.isPending} onClick={() => {
                  if (window.confirm("Deseja remover este horario?")) remove.mutate(item.id);
                }} aria-label="Remover horario"><Trash2 className="size-4" /></Button>
              </div>
            </div>
          ))}
        </Card>
      )}
    </section>
  );
}
