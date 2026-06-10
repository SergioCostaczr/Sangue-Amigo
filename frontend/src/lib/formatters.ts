import { format, parseISO } from "date-fns";
import { ptBR } from "date-fns/locale";

export function onlyDigits(value: string) {
  return value.replace(/\D/g, "");
}

export function formatDate(value: string) {
  return format(parseISO(value), "dd/MM/yyyy", { locale: ptBR });
}

export function formatBloodType(value: string) {
  return value.replace("_POS", "+").replace("_NEG", "-");
}

export function formatLocation(endereco?: string, cidade?: string, estado?: string) {
  const cityState = [cidade, estado].filter(Boolean).join(" - ");
  return [endereco, cityState].filter(Boolean).join(", ") || "Local nao informado";
}

export function isValidCpf(value: string) {
  const cpf = onlyDigits(value);
  if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

  const digit = (length: number) => {
    const sum = cpf
      .slice(0, length)
      .split("")
      .reduce((total, number, index) => total + Number(number) * (length + 1 - index), 0);
    const result = (sum * 10) % 11;
    return result === 10 ? 0 : result;
  };

  return digit(9) === Number(cpf[9]) && digit(10) === Number(cpf[10]);
}

export function isValidCnpj(value: string) {
  const cnpj = onlyDigits(value);
  if (cnpj.length !== 14 || /^(\d)\1+$/.test(cnpj)) return false;

  const calculate = (length: number) => {
    const weights = length === 12
      ? [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]
      : [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    const sum = cnpj
      .slice(0, length)
      .split("")
      .reduce((total, number, index) => total + Number(number) * weights[index], 0);
    const remainder = sum % 11;
    return remainder < 2 ? 0 : 11 - remainder;
  };

  return calculate(12) === Number(cnpj[12]) && calculate(13) === Number(cnpj[13]);
}
