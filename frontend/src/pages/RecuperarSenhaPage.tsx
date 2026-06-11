import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { Mail } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link } from "react-router-dom";
import { ApiFeedback } from "@/components/ApiFeedback";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { recuperarSenhaSchema, type RecuperarSenhaForm } from "@/schemas/public-schema";
import { publicService } from "@/services/public-service";
import type { ApiError } from "@/types/auth";

export function RecuperarSenhaPage() {
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<RecuperarSenhaForm>({
    resolver: zodResolver(recuperarSenhaSchema),
    defaultValues: { email: "" },
  });

  const onSubmit = async ({ email }: RecuperarSenhaForm) => {
    setFeedback(null);
    try {
      await publicService.recuperarSenha(email);
      setFeedback({ type: "success", message: "Se o e-mail estiver cadastrado, as instrucoes serao enviadas." });
    } catch (error) {
      const message = axios.isAxiosError<ApiError>(error)
        ? error.response?.data?.mensagem
        : null;
      setFeedback({ type: "error", message: message || "Nao foi possivel solicitar a recuperacao." });
    }
  };

  return (
    <section className="page-container grid min-h-[calc(100vh-8rem)] place-items-center py-12">
      <Card className="w-full max-w-md p-6 sm:p-8">
        <Mail className="size-7 text-primary" />
        <h1 className="mt-5 text-2xl font-bold">Recuperar senha</h1>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">Informe o e-mail usado no cadastro.</p>
        <form className="mt-6 space-y-4" onSubmit={handleSubmit(onSubmit)} noValidate>
          <FormField label="E-mail" error={errors.email?.message}>
            <Input type="email" autoComplete="email" {...register("email")} />
          </FormField>
          {feedback && <ApiFeedback {...feedback} />}
          <Button type="submit" loading={isSubmitting} className="w-full">Enviar instrucoes</Button>
        </form>
        <Link to="/login" className="mt-5 block text-center text-sm font-medium text-primary hover:underline">Voltar para o login</Link>
      </Card>
    </section>
  );
}
