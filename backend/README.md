# Homever Backend

## Setup and Run locally
1. `npm install`
2. Start Postgres using Docker Compose: `docker compose up -d`
3. Push the Prisma Schema: `npx prisma db push`
4. Start the app: `npm run start:dev`

The application will run on `http://localhost:3000`. Android Emulator will access it on `http://10.0.2.2:3000/v1`.
