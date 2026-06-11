import axios from "axios";
import type { ApiError } from "@/types/auth";

export function getApiError(error: unknown, fallback = "Nao foi possivel concluir a operacao.") {
  if (axios.isAxiosError<ApiError>(error)) {
    return error.response?.data?.mensagem || fallback;
  }
  return fallback;
}
