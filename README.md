# Sangue Amigo

Monorepo da plataforma Sangue Amigo.

## Estrutura

- `backend/`: API Spring Boot com Java 21.
- `frontend/`: React, Vite e TypeScript.
- `docker-compose.yml`: PostgreSQL local.

## Desenvolvimento

```powershell
docker compose up -d
npm --prefix frontend install
npm run dev
```

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`

O comando `npm run dev` inicia frontend e backend em processos separados.

## Configuracao

O frontend usa a variavel abaixo para localizar a API:

```env
VITE_API_URL=http://localhost:8080
```

Crie `frontend/.env.local` somente quando precisar alterar o endereco padrao.

## Funcionalidades

- Publico: hemocentros, campanhas, login, cadastros e recuperacao de senha.
- Doador: perfil, agendamento, confirmacao, cancelamento, QR Code e historico.
- Hemocentro: painel diario, horarios, campanhas, validacao de token, perfil e doacoes.

## Verificacoes

```powershell
npm run lint
npm run typecheck
npm run build
npm run backend:test
```

O build do frontend usa divisao de codigo por rota para reduzir o carregamento inicial.
