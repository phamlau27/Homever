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

## Matching semantic-v1

Trước khi chạy backend mới trên database đã có dữ liệu, thêm cột JSONB bằng SQL chỉ bổ sung cột:

```bash
npx prisma db execute --file prisma/matching-preferences.sql --schema prisma/schema.prisma
npx prisma generate
npm run test:matching
npm run start:dev
```

Dừng backend trước `prisma generate` trên Windows nếu DLL Prisma đang bị khóa. SQL có thể chạy lại
và không xóa hồ sơ. Database mới có thể dùng `prisma db push` theo phần Setup.

`POST /v1/users/me` nhận thêm `matchingPreferences` theo hợp đồng phiên bản 1:

```json
{
  "schemaVersion": 1,
  "acceptedGenders": ["MALE", "FEMALE", "OTHER"],
  "districtIds": ["HCM_Q01", "HCM_Q03"],
  "budgetMinVnd": 2000000,
  "budgetMaxVnd": null,
  "answers": {
    "schedule": "FLEXIBLE",
    "noise": "QUIET",
    "cleanliness": "TIDY",
    "smoking": "NEVER",
    "pets": "ACCEPT",
    "guests": "SOMETIMES",
    "privacy": "BALANCED",
    "cooking": "SOMETIMES",
    "responsibility": "ASSIGNED",
    "costSharing": "EQUAL"
  }
}
```

Ngân sách là VNĐ/người/tháng, biên đóng, `null` nghĩa là không giới hạn trên; giả định chia đều
tiền thuê theo mục 2.1 báo cáo. Chia chi phí sinh hoạt là một tiêu chí mềm, không thay đổi công thức
lọc tiền thuê. Khu vực là danh mục tìm kiếm TP.HCM cố định của app; so khớp giao của ID,
không khớp chuỗi con hoặc tự mở rộng vì cùng thành phố.

Cả hai phía phải chấp nhận giới tính của nhau, có khu vực chung và ngân sách giao nhau.
Chỉ hồ sơ có đủ dữ liệu hợp lệ mới được chấm điểm. `GET /v1/matching/candidates?limit=20`
chấp nhận số nguyên từ 1 đến 100, trả tối đa K hồ sơ hợp lệ theo điểm điều hòa giảm dần;
không bổ sung hồ sơ trượt lọc hay nâng điểm hiển thị. Hồ sơ người đang tìm chưa đầy đủ nhận
HTTP 422, mã `MATCHING_PROFILE_INCOMPLETE` và `missingFields`; hết ứng viên hợp lệ trả `[]`.

Trọng số HOMEVER gốc được giữ và chuẩn hóa tổng về 1. Ma trận khách đến phòng theo Bảng 2;
các ma trận còn lại là giả định thiết kế `semantic-v1`, chưa được xác nhận tối ưu bằng thực nghiệm.
Giờ giấc linh hoạt chấp nhận cả ba lịch; riêng tư dùng đúng ba đáp án Android.
Điểm phần trăm là điểm tương thích, không phải xác suất Match. Với cùng trọng số và ma trận
đối xứng, hai điểm có hướng bằng nhau; công thức điều hòa vẫn được giữ để dùng khi có trọng số riêng.

`lifestyle`, `budget`, `location` vẫn được giữ để hiển thị. Hồ sơ cũ chưa có JSON chỉ được đọc
khi các câu trả lời có tên rõ ràng hoặc đủ 11 câu theo đúng thứ tự bảng hỏi, có khu vực và ngân sách
đọc được chính xác. Ghi chú và traits không tham gia matching. Thiếu/mơ hồ thì yêu cầu bổ sung,
không tự điền đáp án hay coi là không giới hạn. Các lần lưu hồ sơ hợp lệ sẽ chuyển sang JSON;
chưa chạy chuyển đổi hàng loạt dữ liệu cũ.

Bộ hồi quy backend dùng `node:test` với Prisma mock, không truy cập database hay Cognito:
`npm run test:matching`. Android kiểm tra bằng `./gradlew :app:testDebugUnitTest :app:assembleDebug`.
