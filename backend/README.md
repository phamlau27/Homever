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
Trên Android 17 với target SDK 37, cho phép quyền mạng nội bộ khi ứng dụng yêu cầu.

## Match và nhắn tin

Cập nhật database đã có hồ sơ và lượt quan tâm bằng SQL bổ sung, không xóa dữ liệu:

```bash
npx prisma db execute --file prisma/sql/add-chat.sql --schema prisma/schema.prisma
npx prisma generate
npm run start:dev
```

SQL tạo hội thoại cho cả các cặp đã cùng quan tâm trước đó. Khi một lượt quan tâm trở thành hai chiều,
backend tạo đúng một hội thoại cho cặp đó và ẩn cặp khỏi danh sách Đã gửi/Đã nhận.
Lượt quan tâm gốc được giữ lại. Mỗi bên xác nhận màn hình Match riêng, trạng thái này được lưu trên máy chủ.

Các API chat đều yêu cầu Cognito JWT và chỉ cho thành viên của hội thoại truy cập:

- `GET /v1/chat/conversations`: kết nối, tin cuối, số tin chưa đọc và Match chưa xác nhận.
- `POST /v1/chat/conversations/:id/match-seen`: xác nhận đã xem Match cho người hiện tại.
- `GET /v1/chat/conversations/:id/messages?afterId=...`: tải tin mới; dùng `beforeId` để tải lịch sử cũ. Mỗi trang tối đa 50 tin.
- `POST /v1/chat/conversations/:id/messages`: body `{"content":"Xin chào","clientId":"UUID"}`, tối đa 4000 ký tự. Gửi lại cùng `clientId` không tạo tin trùng.
- `POST /v1/chat/conversations/:id/read`: body `{"lastMessageId":123}`, chỉ đánh dấu tới tin đã tải trong chính hội thoại này.

Ứng dụng đồng bộ danh sách/Match mỗi 3,5 giây và tin đang mở mỗi 2,5 giây khi ở foreground.
Đây là polling, chưa có thông báo đẩy khi app đóng. Dừng polling khi app ra background.
Kết nối chưa có tin nằm ở “Kết nối mới”; sau tin đầu tiên chuyển sang “Trò chuyện gần đây”.
Tin nhắn và token không được ghi vào HTTP body log.

## Lượt quan tâm

Với database đã có hồ sơ, thêm bảng Quan tâm bằng SQL chỉ tạo bảng và index mới:

```bash
npx prisma db execute --file prisma/sql/add-interests.sql --schema prisma/schema.prisma
npx prisma generate
npm run start:dev
```

Trên Windows, dừng backend trước khi chạy `prisma generate` nếu Prisma báo DLL đang bị khóa.

- `GET /v1/matching/interests`: danh sách `sent` và `received` của người đang đăng nhập.
- `POST /v1/matching/interests` với `{"profileId":"..."}`: gửi quan tâm, không tạo trùng, trả về danh sách đã cập nhật.
- `mutual` chỉ đúng khi hai người đã quan tâm lẫn nhau. API yêu cầu Cognito JWT và không nhận ID người gửi từ client.

Các lựa chọn mới được lưu trong `lifestyle` theo dạng `Tên tiêu chí: Lựa chọn`, ngăn cách bằng dấu chấm phẩy.
Ứng dụng dùng chung bộ nhãn để hiển thị cả dữ liệu mới và dữ liệu cũ; không tự suy đoán nhóm cho lựa chọn cũ bị mất ngữ cảnh.
