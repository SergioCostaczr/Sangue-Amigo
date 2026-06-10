import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { KeyRound } from "lucide-react";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useSearchParams } from "react-router-dom";
import { ApiFeedback } from "@/components/ApiFeedback";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { redefinirSenhaSchema, type RedefinirSenhaForm } from "@/schemas/public-schema";
import { publicService } from "@/services/public-service";
import type { ApiError } from "@/types/auth";

export function RedefinirSenhaPage() {
  const [params] = useSearchParams();
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const { register, handleSubmit, setValue, formState: { errors, isSubmitting } } = useForm<RedefinirSenhaForm>({
    resolver: zodResolver(redefinirSenhaSchema),
    defaultValues: { token: "", novaSenha: "", confirmarSenha: "" },
  });

  useEffect(() => {
    const token = params.get("token");
    if (token) setValue("token", token);
  }, [params, setValue]);

  const onSubmit = async (data: RedefinirSenhaForm) => {
    setFeedback(null);
    try {
      await publicService.redefinirSenha(data.token, data.novaSenha);
      setFeedback({ type: "success", message: "Senha redefinida. Voce ja pode entrar com a nova senha." });
    } catch (error) {
      const message = axios.isAxiosError<ApiError>(error)
        ? error.response?.data?.mensagem
        : null;
      setFeedback({ type: "error", message: message || "Token invalido ou expirado." });
    }
  };

  return (
    <section className="page-container grid min-h-[calc(100vh-8rem)] place-items-center py-12">
      <Card className="w-full max-w-md p-6 sm:p-8">
        <KeyRound className="size-7 text-primary" />
        <h1 className="mt-5 text-2xl font-bold">Defina uma nova senha</h1>
        <form className="mt-6 space-y-4" onSubmit={handleSubmit(onSubmit)} noValidate>
          <FormField label="Token" error={errors.token?.message}>
            <Input {...register("token")} />
          </FormField>
          <FormField label="Nova senha" error={errors.novaSenha?.message}>
            <Input type="password" autoComplete="new-password" {...register("novaSenha")} />
          </FormField>
          <FormField label="Confirmar nova senha" error={errors.confirmarSenha?.message}>
            <Input type="password" autoComplete="new-password" {...register("confirmarSenha")} />
          </FormField>
          {feedback && <ApiFeedback {...feedback} />}
          <Button type="submit" loading={isSubmitting} className="w-full">Redefinir senha</Button>
        </form>
        <Link to="/login" className="mt-5 block text-center text-sm font-medium text-primary hover:underline">Voltar para o login</Link>
      </Card>
    </section>
  );
}
