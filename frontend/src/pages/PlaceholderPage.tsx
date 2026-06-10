import { Construction } from "lucide-react";
import { Card } from "@/components/ui/card";
import { PageHeader } from "@/components/PageHeader";
import { cn } from "@/lib/utils";

interface PlaceholderPageProps {
  title: string;
  description: string;
  compact?: boolean;
}

export function PlaceholderPage({ title, description, compact = false }: PlaceholderPageProps) {
  return (
    <section className={cn(compact ? "page-container max-w-2xl py-16" : "py-2")}>
      <PageHeader title={title} description={description} />
      <Card className="flex min-h-56 flex-col items-center justify-center border-dashed p-8 text-center">
        <Construction className="size-7 text-primary" />
        <p className="mt-4 font-semibold text-foreground">Estrutura preparada</p>
        <p className="mt-1 max-w-md text-sm text-muted-foreground">
          Esta funcionalidade sera conectada ao backend em uma das proximas etapas.
        </p>
      </Card>
    </section>
  );
}
