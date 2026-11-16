/**
 * Cấu hình matching semantic-v1.
 *
 * W^(0) lấy từ khảo sát HOMEVER (Bảng 3). Ma trận khách đến phòng giữ
 * nguyên Bảng 2; các ma trận còn lại là tham số ngữ nghĩa thiết kế v1,
 * chưa phải ước lượng thực nghiệm từ dữ liệu khảo sát.
 */
export const MATCHING_VERSION = 'semantic-v1';

export const CRITERIA_KEYS = [
  'schedule',
  'noise',
  'cleanliness',
  'smoking',
  'pets',
  'guests',
  'privacy',
  'cooking',
  'responsibility',
  'costSharing',
] as const;

export type CriterionKey = (typeof CRITERIA_KEYS)[number];

/** Mã đáp án ổn định, tách khỏi nhãn tiếng Việt hiển thị trên Android. */
export const ANSWER_OPTIONS: Readonly<Record<CriterionKey, readonly string[]>> = {
  schedule: ['EARLY', 'NORMAL', 'NIGHT', 'FLEXIBLE'],
  noise: ['VERY_QUIET', 'QUIET', 'NORMAL', 'NOISE_OK'],
  cleanliness: ['VERY_TIDY', 'TIDY', 'NORMAL', 'RELAXED'],
  smoking: ['NEVER', 'SOMETIMES', 'OFTEN'],
  pets: ['LOVE', 'ACCEPT', 'INDIFFERENT', 'NO_PETS'],
  guests: ['RARE', 'SOMETIMES', 'WEEKENDS', 'OFTEN'],
  privacy: ['HIGH', 'BALANCED', 'COMMUNAL'],
  cooking: ['OFTEN', 'SOMETIMES', 'TAKEOUT', 'NEVER'],
  responsibility: ['PROACTIVE', 'ASSIGNED', 'FLEXIBLE', 'NORMAL'],
  costSharing: ['EQUAL', 'USAGE', 'SEPARATE', 'FLEXIBLE'],
};

/** Giá trị công bố nguyên bản; tổng bằng 99,99% do làm tròn khảo sát. */
export const PUBLISHED_WEIGHTS: Readonly<Record<CriterionKey, number>> = {
  responsibility: 0.1075, // Trách nhiệm việc chung: 10.75%
  privacy: 0.1069,        // Quyền riêng tư: 10.69%
  smoking: 0.1050,        // Hút thuốc: 10.50%
  cleanliness: 0.1040,    // Sạch sẽ: 10.40%
  costSharing: 0.1008,    // Chia chi phí: 10.08%
  guests: 0.1000,         // Khách đến phòng: 10.00%
  cooking: 0.1000,        // Nấu ăn: 10.00%
  noise: 0.0960,          // Tiếng ồn: 9.60%
  pets: 0.0900,           // Thú cưng: 9.00%
  schedule: 0.0897,       // Giờ giấc: 8.97%
};

const publishedWeightSum = CRITERIA_KEYS.reduce(
  (sum, key) => sum + PUBLISHED_WEIGHTS[key],
  0,
);

/** Trọng số dùng để tính điểm: w_i = W^(0)_i / sum(W^(0)). */
export const INITIAL_WEIGHTS: Readonly<Record<CriterionKey, number>> =
  CRITERIA_KEYS.reduce(
    (weights, key) => {
      weights[key] = PUBLISHED_WEIGHTS[key] / publishedWeightSum;
      return weights;
    },
    {} as Record<CriterionKey, number>,
  );

export interface OrdinalCriterion {
  readonly [option: string]: number;
  readonly _maxRank: number;
}

/**
 * Khoảng cách thứ bậc chuẩn hóa lấy cảm hứng từ Podani:
 * c_i = 1 - |rank(u) - rank(v)| / (K - 1).
 * FLEXIBLE của giờ giấc là tập chấp nhận {EARLY, NORMAL, NIGHT};
 * service xử lý riêng, không gán nó thành rank NORMAL.
 */
export const ORDINAL_CRITERIA: Readonly<Record<string, OrdinalCriterion>> = {
  schedule: {
    EARLY: 1,
    NORMAL: 2,
    NIGHT: 3,
    _maxRank: 3,
  },
  noise: {
    VERY_QUIET: 1,
    QUIET: 2,
    NORMAL: 3,
    NOISE_OK: 4,
    _maxRank: 4,
  },
  cleanliness: {
    VERY_TIDY: 1,
    TIDY: 2,
    NORMAL: 3,
    RELAXED: 4,
    _maxRank: 4,
  },
  smoking: {
    NEVER: 1,
    SOMETIMES: 2,
    OFTEN: 3,
    _maxRank: 3,
  },
  // Ba lựa chọn thực tế trên bảng hỏi Android.
  privacy: {
    HIGH: 1,
    BALANCED: 2,
    COMMUNAL: 3,
    _maxRank: 3,
  },
};

/**
 * M_i(a_u, a_v): đối xứng, đường chéo 1, mỗi ô thuộc [0, 1].
 * Giữ riêng từng đáp án; không suy ra đáp án thiếu hay ghi đè ràng buộc cứng.
 */
export const CATEGORICAL_MATRICES: Readonly<
  Record<string, Readonly<Record<string, Readonly<Record<string, number>>>>>
> = {
  // Bảng 2: cuối tuần là một kiểu tiếp khách, không phải thứ bậc tần suất.
  guests: {
    RARE: { RARE: 1, SOMETIMES: 0.75, WEEKENDS: 0.5, OFTEN: 0.25 },
    SOMETIMES: { RARE: 0.75, SOMETIMES: 1, WEEKENDS: 0.75, OFTEN: 0.5 },
    WEEKENDS: { RARE: 0.5, SOMETIMES: 0.75, WEEKENDS: 1, OFTEN: 0.75 },
    OFTEN: { RARE: 0.25, SOMETIMES: 0.5, WEEKENDS: 0.75, OFTEN: 1 },
  },
  // Thái độ với thú cưng, không phải bằng chứng đang nuôi hay bị dị ứng.
  pets: {
    LOVE: { LOVE: 1, ACCEPT: 0.85, INDIFFERENT: 0.7, NO_PETS: 0.1 },
    ACCEPT: { LOVE: 0.85, ACCEPT: 1, INDIFFERENT: 0.85, NO_PETS: 0.4 },
    INDIFFERENT: { LOVE: 0.7, ACCEPT: 0.85, INDIFFERENT: 1, NO_PETS: 0.6 },
    NO_PETS: { LOVE: 0.1, ACCEPT: 0.4, INDIFFERENT: 0.6, NO_PETS: 1 },
  },
  // SOMETIMES = thỉnh thoảng, TAKEOUT = chủ yếu mua ngoài.
  cooking: {
    OFTEN: { OFTEN: 1, SOMETIMES: 0.8, TAKEOUT: 0.5, NEVER: 0.3 },
    SOMETIMES: { OFTEN: 0.8, SOMETIMES: 1, TAKEOUT: 0.8, NEVER: 0.6 },
    TAKEOUT: { OFTEN: 0.5, SOMETIMES: 0.8, TAKEOUT: 1, NEVER: 0.9 },
    NEVER: { OFTEN: 0.3, SOMETIMES: 0.6, TAKEOUT: 0.9, NEVER: 1 },
  },
  // Tự thanh toán gần với theo sử dụng, nhưng không gộp thành linh hoạt.
  costSharing: {
    EQUAL: { EQUAL: 1, USAGE: 0.6, SEPARATE: 0.4, FLEXIBLE: 0.8 },
    USAGE: { EQUAL: 0.6, USAGE: 1, SEPARATE: 0.9, FLEXIBLE: 0.8 },
    SEPARATE: { EQUAL: 0.4, USAGE: 0.9, SEPARATE: 1, FLEXIBLE: 0.8 },
    FLEXIBLE: { EQUAL: 0.8, USAGE: 0.8, SEPARATE: 0.8, FLEXIBLE: 1 },
  },
  // Linh hoạt là cách phối hợp, không suy diễn thành thiếu trách nhiệm.
  responsibility: {
    PROACTIVE: { PROACTIVE: 1, ASSIGNED: 0.85, FLEXIBLE: 0.85, NORMAL: 0.6 },
    ASSIGNED: { PROACTIVE: 0.85, ASSIGNED: 1, FLEXIBLE: 0.85, NORMAL: 0.6 },
    FLEXIBLE: { PROACTIVE: 0.85, ASSIGNED: 0.85, FLEXIBLE: 1, NORMAL: 0.75 },
    NORMAL: { PROACTIVE: 0.6, ASSIGNED: 0.6, FLEXIBLE: 0.75, NORMAL: 1 },
  },
};
