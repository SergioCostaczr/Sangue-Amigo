export function ApiFeedback({
  type,
  message,
}: {
  type: "success" | "error";
  message: string;
}) {
  return (
    <div
      role={type === "error" ? "alert" : "status"}
      className={
        type === "error"
          ? "rounded-md border border-destructive/25 bg-destructive/10 px-3 py-2.5 text-sm text-destructive"
          : "rounded-md border border-emerald-200 bg-emerald-50 px-3 py-2.5 text-sm text-emerald-800"
      }
    >
      {message}
    </div>
  );
}
