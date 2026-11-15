# Homever Backend

## Setup and Run (Supabase PostgreSQL - Không cần Docker)

1. Cài đặt dependencies:
   ```bash
   npm install
   ```
2. Cập nhật file `.env` với connection string từ Supabase (`DATABASE_URL` và `DIRECT_URL`).
3. Đồng bộ Prisma Schema lên Supabase:
   ```bash
   npx prisma db push
   ```
4. Khởi chạy Server NestJS Backend:
   ```bash
   npm run start:dev
   ```

Backend chạy tại `http://localhost:3000`. Android Emulator kết nối qua `http://10.0.2.2:3000/v1`.
