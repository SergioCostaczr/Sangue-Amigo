import { useQuery } from "@tanstack/react-query";
import { CalendarDays, MapPin } from "lucide-react";
import { Card } from "@/components/ui/card";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { formatBloodType, formatDate, formatLocation } from "@/lib/formatters";
import { publicService } from "@/services/public-service";

const urgencyStyle = {
  NORMAL: "bg-muted text-muted-foreground",
  ALTA: "bg-amber-100 text-amber-800",
  CRITICA: "bg-destructive/10 text-destructive",
};

export function CampanhasPage() {
  const query = useQuery({
    queryKey: ["campanhas-publicas"],
    queryFn: publicService.listarCampanhas,
  });

  return (
    <section className="page-container py-12">
      <header className="mb-8 max-w-2xl">
        <p className="text-sm font-semibold text-primary">Mobilizacao</p>
        <h1 className="mt-2 text-3xl font-bold text-foreground">Campanhas ativas</h1>
        <p className="mt-3 text-muted-foreground">
          Veja quais tipos sanguineos estao em maior necessidade neste momento.
        </p>
      </header>

      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar as campanhas." />}
      {query.data?.length === 0 && (
        <EmptyState title="Nenhuma campanha ativa" description="As proximas campanhas dos hemocentros aparecerao aqui." />
      )}

      {query.data && query.data.length > 0 && (
        <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
          {query.data.map((campanha) => (
            <Card key={campanha.id} className="overflow-hidden">
              {campanha.urlImagem ? (
                <img src={campanha.urlImagem} alt="" className="aspect-[16/9] w-full object-cover" />
              ) : (
                <div className="grid aspect-[16/9] place-items-center bg-accent text-3xl font-bold text-primary">
                  {campanha.tiposSanguineosNecessarios.slice(0, 2).map(formatBloodType).join(" / ")}
                </div>
              )}
              <div className="p-5">
                <div className="flex items-center justify-between gap-3">
                  <span className={`rounded-full px-2.5 py-1 text-xs font-semibold ${urgencyStyle[campanha.urgencia]}`}>
                    {campanha.urgencia}
                  </span>
                  <span className="text-xs text-muted-foreground">{campanha.nomeHemocentro}</span>
                </div>
                <h2 className="mt-4 text-lg font-semibold text-foreground">{campanha.titulo}</h2>
                {campanha.descricao && <p className="mt-2 line-clamp-3 text-sm leading-6 text-muted-foreground">{campanha.descricao}</p>}
                <div className="mt-4 space-y-2 text-sm text-muted-foreground">
                  <p className="flex items-center gap-2"><CalendarDays className="size-4" /> {formatDate(campanha.dataInicio)} a {formatDate(campanha.dataFim)}</p>
                  <p className="flex items-start gap-2"><MapPin className="mt-0.5 size-4 shrink-0" /> {formatLocation(campanha.endereco, campanha.cidade, campanha.estado)}</p>
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}
    </section>
  );
}
