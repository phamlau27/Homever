/**
 * Hằng số & Bảng tra cứu cho Giai đoạn 1 thuật toán Matching AISC
 * Theo báo cáo "PHƯƠNG PHÁP ĐỀ XUẤT CHO HỆ THỐNG MATCHING AISC"
 */

// 1. Vector trọng số ban đầu W^(0) từ khảo sát HOMEVER (Mục 2.4 - Bảng 3)
export const INITIAL_WEIGHTS: Record<string, number> = {
  responsibility: 0.1075, // Trách nhiệm việc chung: 10.75%
  privacy: 0.1069,        // Quyền riêng tư: 10.69%
  smoking: 0.1050,        // Hút thuốc: 10.50%
  cleanliness: 0.1040,    // Sạch sẽ: 10.40%
  costSharing: 0.1008,    // Chia chi phí: 10.08%
  guests: 0.1000,         // Khách đến phòng: 10.00%
  cooking: 0.1000,        // Nấu ăn: 10.00%
  noise: 0.0960,          // Tiếng ồn: 9.60%
  pets: 0.0900,           // Thú cưng: 9.00%
  schedule: 0.0897,       // Giờ giấc sinh hoạt: 8.97%
};

// 2. Thang thứ bậc (Ordinal Ranking) - Công thức Podani chuẩn hóa: c_i = 1 - |rank(u) - rank(v)| / (K - 1)
export const ORDINAL_CRITERIA: Record<string, { [option: string]: number; _maxRank: number }> = {
  // Tiêu chí 1: Giờ giấc sinh hoạt
  schedule: {
    'Dậy sớm': 1,
    'Bình thường': 2,
    'Cú đêm': 3,
    'Linh hoạt': 2, // Trung lập
    _maxRank: 3,
  },

  // Tiêu chí 2: Tiếng ồn
  noise: {
    'Rất yên tĩnh': 1,
    'Khá yên tĩnh': 2,
    'Bình thường': 3,
    'Không ngại ồn': 4,
    _maxRank: 4,
  },

  // Tiêu chí 3: Sạch sẽ / Gọn gàng
  cleanliness: {
    'Rất gọn gàng': 1,
    'Khá gọn gàng': 2,
    'Bình thường': 3,
    'Khá thoải mái': 4,
    _maxRank: 4,
  },

  // Tiêu chí 4: Hút thuốc
  smoking: {
    'Không hút': 1,
    'Thỉnh thoảng': 2,
    'Thường xuyên': 3,
    _maxRank: 3,
  },

  // Tiêu chí 7: Quyền riêng tư
  privacy: {
    'Rất cao': 1,
    'Khá cao': 2,
    'Vừa phải': 3,
    'Thoải mái': 4,
    _maxRank: 4,
  },
};

// 3. Ma trận danh mục ngữ nghĩa M_i(a_u, a_v) (Categorical Similarity - Mục 2.2 & 2.3 - Bảng 2)
export const CATEGORICAL_MATRICES: Record<string, Record<string, Record<string, number>>> = {
  // Tiêu chí 6: Khách đến phòng (Bảng 2 trong báo cáo AISC)
  guests: {
    'Hầu như không': { 'Hầu như không': 1.0, 'Thỉnh thoảng': 0.75, 'Cuối tuần': 0.5, 'Thường xuyên': 0.25 },
    'Thỉnh thoảng': { 'Hầu như không': 0.75, 'Thỉnh thoảng': 1.0, 'Cuối tuần': 0.75, 'Thường xuyên': 0.5 },
    'Cuối tuần': { 'Hầu như không': 0.5, 'Thỉnh thoảng': 0.75, 'Cuối tuần': 1.0, 'Thường xuyên': 0.75 },
    'Thường xuyên': { 'Hầu như không': 0.25, 'Thỉnh thoảng': 0.5, 'Cuối tuần': 0.75, 'Thường xuyên': 1.0 },
  },

  // Tiêu chí 5: Thú cưng
  pets: {
    'Thích thú cưng': { 'Thích thú cưng': 1.0, 'Có thể ở cùng': 0.85, 'Không quan tâm': 0.7, 'Không muốn': 0.1 },
    'Có thể ở cùng': { 'Thích thú cưng': 0.85, 'Có thể ở cùng': 1.0, 'Không quan tâm': 0.85, 'Không muốn': 0.4 },
    'Không quan tâm': { 'Thích thú cưng': 0.7, 'Có thể ở cùng': 0.85, 'Không quan tâm': 1.0, 'Không muốn': 0.6 },
    'Không muốn': { 'Thích thú cưng': 0.1, 'Có thể ở cùng': 0.4, 'Không quan tâm': 0.6, 'Không muốn': 1.0 },
  },

  // Tiêu chí 8: Nấu ăn
  cooking: {
    'Tự nấu hàng ngày': { 'Tự nấu hàng ngày': 1.0, 'Nấu cuối tuần': 0.8, 'Hiếm khi nấu': 0.5, 'Không nấu ăn': 0.3 },
    'Nấu cuối tuần': { 'Tự nấu hàng ngày': 0.8, 'Nấu cuối tuần': 1.0, 'Hiếm khi nấu': 0.8, 'Không nấu ăn': 0.6 },
    'Hiếm khi nấu': { 'Tự nấu hàng ngày': 0.5, 'Nấu cuối tuần': 0.8, 'Hiếm khi nấu': 1.0, 'Không nấu ăn': 0.9 },
    'Không nấu ăn': { 'Tự nấu hàng ngày': 0.3, 'Nấu cuối tuần': 0.6, 'Hiếm khi nấu': 0.9, 'Không nấu ăn': 1.0 },
  },

  // Tiêu chí 9: Chia chi phí
  costSharing: {
    'Chia đều tất cả': { 'Chia đều tất cả': 1.0, 'Dùng bao nhiêu trả bấy nhiêu': 0.6, 'Linh hoạt': 0.8 },
    'Dùng bao nhiêu trả bấy nhiêu': { 'Chia đều tất cả': 0.6, 'Dùng bao nhiêu trả bấy nhiêu': 1.0, 'Linh hoạt': 0.7 },
    'Linh hoạt': { 'Chia đều tất cả': 0.8, 'Dùng bao nhiêu trả bấy nhiêu': 0.7, 'Linh hoạt': 1.0 },
  },

  // Tiêu chí 10: Trách nhiệm việc chung
  responsibility: {
    'Rất chủ động': { 'Rất chủ động': 1.0, 'Theo phân công': 0.85, 'Linh hoạt': 0.6, 'Bình thường': 0.4 },
    'Theo phân công': { 'Rất chủ động': 0.85, 'Theo phân công': 1.0, 'Linh hoạt': 0.75, 'Bình thường': 0.5 },
    'Linh hoạt': { 'Rất chủ động': 0.6, 'Theo phân công': 0.75, 'Linh hoạt': 1.0, 'Bình thường': 0.7 },
    'Bình thường': { 'Rất chủ động': 0.4, 'Theo phân công': 0.5, 'Linh hoạt': 0.7, 'Bình thường': 1.0 },
  },
};
