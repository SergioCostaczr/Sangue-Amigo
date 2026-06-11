import { ArrowRight, Building2, CalendarCheck, Megaphone } from "lucide-react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";

const highlights = [
  { icon: Building2, title: "Encontre hemocentros", description: "Consulte unidades proximas e seus horarios." },
  { icon: CalendarCheck, title: "Agende sua doacao", description: "Escolha data e horario de forma simples." },
  { icon: Megaphone, title: "Acompanhe campanhas", description: "Veja onde sua doacao e mais necessaria." },
];

export function HomePage() {
  return (
    <>
      <section
        className="relative min-h-[560px] overflow-hidden border-b border-border bg-card bg-cover bg-[68%_center] sm:bg-center"
        style={{ backgroundImage: "url('/images/hero-doacao.png')" }}
      >
        <div className="absolute inset-0 bg-white/45 sm:bg-white/20" />
        <div className="page-container relative flex min-h-[560px] items-center py-14">
          <div className="max-w-2xl text-balance [text-shadow:0_1px_12px_rgba(255,255,255,0.9)]">
            <p className="text-sm font-semibold uppercase text-primary">Doar aproxima. Doar transforma.</p>
            <h1 className="mt-4 text-4xl font-bold leading-tight text-foreground sm:text-5xl">
              Sua doacao pode fazer parte da historia de alguem.
            </h1>
            <p className="mt-5 max-w-xl text-lg leading-8 text-muted-foreground">
              O Sangue Amigo conecta doadores e hemocentros para tornar cada etapa mais clara, organizada e acessivel.
            </p>
            <div className="mt-8 flex flex-wrap gap-3">
              <Link to="/cadastro"><Button className="h-11">Quero ser doador <ArrowRight className="size-4" /></Button></Link>
              <Link to="/hemocentros"><Button variant="secondary" className="h-11">Ver hemocentros</Button></Link>
            </div>
          </div>
        </div>
      </section>

      <section className="page-container py-14">
        <div className="grid gap-4 md:grid-cols-3">
          {highlights.map(({ icon: Icon, title, description }) => (
            <div key={title} className="border-t-2 border-primary px-1 pt-5">
              <Icon className="size-5 text-primary" />
              <h2 className="mt-4 font-semibold text-foreground">{title}</h2>
              <p className="mt-2 text-sm leading-6 text-muted-foreground">{description}</p>
            </div>
          ))}
        </div>
      </section>
    </>
  );
}
