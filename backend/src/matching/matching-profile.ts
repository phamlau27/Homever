import { ANSWER_OPTIONS, CRITERIA_KEYS, CriterionKey } from './matching.constants';

export const GENDERS = ['MALE', 'FEMALE', 'OTHER'] as const;
export type Gender = (typeof GENDERS)[number];
export type MatchingAnswers = Record<CriterionKey, string>;

export interface BudgetRange {
  min: number;
  max: number | null; // null: không giới hạn trên, đơn vị VNĐ/người/tháng
}

export interface MatchingPreferences {
  schemaVersion: 1;
  acceptedGenders: Gender[];
  districtIds: string[];
  budgetMinVnd: number;
  budgetMaxVnd: number | null;
  answers: MatchingAnswers;
}

export interface MatchingProfileInput {
  gender?: string | null;
  lifestyle?: string | null;
  budget?: string | null;
  location?: string | null;
  matchingPreferences?: unknown;
}

export interface NormalizedMatchingProfile {
  gender: Gender;
  preferences: MatchingPreferences;
}

/** Danh mục khu vực tìm kiếm của app, không phải danh mục hành chính hiện hành. */
export const HCM_DISTRICTS: Readonly<Record<string, string>> = {
  HCM_Q01: 'Quận 1', HCM_Q03: 'Quận 3', HCM_Q04: 'Quận 4',
  HCM_Q05: 'Quận 5', HCM_Q06: 'Quận 6', HCM_Q07: 'Quận 7',
  HCM_Q08: 'Quận 8', HCM_Q10: 'Quận 10', HCM_Q11: 'Quận 11',
  HCM_Q12: 'Quận 12', HCM_BINH_TAN: 'Bình Tân',
  HCM_BINH_THANH: 'Bình Thạnh', HCM_GO_VAP: 'Gò Vấp',
  HCM_PHU_NHUAN: 'Phú Nhuận', HCM_TAN_BINH: 'Tân Bình',
  HCM_TAN_PHU: 'Tân Phú', HCM_THU_DUC: 'Thủ Đức',
  HCM_BINH_CHANH: 'Bình Chánh', HCM_CAN_GIO: 'Cần Giờ',
  HCM_CU_CHI: 'Củ Chi', HCM_HOC_MON: 'Hóc Môn', HCM_NHA_BE: 'Nhà Bè',
};

function normalizedText(value: string): string {
  return value.normalize('NFD').replace(/[\u0300-\u036f]/g, '')
    .replace(/đ/g, 'd').replace(/Đ/g, 'D').toLowerCase()
    .replace(/\./g, '').replace(/\s+/g, ' ').trim();
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return value !== null && typeof value === 'object' && !Array.isArray(value);
}

export function normalizeGender(value: unknown): Gender | null {
  if (typeof value !== 'string') return null;
  const gender = normalizedText(value);
  if (['nam', 'male'].includes(gender)) return 'MALE';
  if (['nu', 'female'].includes(gender)) return 'FEMALE';
  if (['khac', 'other'].includes(gender)) return 'OTHER';
  return null;
}

export function validateMatchingPreferences(value: unknown): MatchingPreferences | null {
  if (!isRecord(value) || value.schemaVersion !== 1) return null;
  const { acceptedGenders, districtIds, budgetMinVnd, budgetMaxVnd, answers } = value;
  if (!Array.isArray(acceptedGenders) || acceptedGenders.length < 1 ||
      acceptedGenders.length > GENDERS.length ||
      acceptedGenders.some(g => !GENDERS.includes(g))) return null;
  if (!Array.isArray(districtIds) || districtIds.length < 1 ||
      districtIds.length > Object.keys(HCM_DISTRICTS).length ||
      districtIds.some(id => typeof id !== 'string' ||
        !Object.prototype.hasOwnProperty.call(HCM_DISTRICTS, id))) return null;
  if (typeof budgetMinVnd !== 'number' || !Number.isSafeInteger(budgetMinVnd) ||
      budgetMinVnd < 0) return null;
  if (budgetMaxVnd !== null && (typeof budgetMaxVnd !== 'number' ||
      !Number.isSafeInteger(budgetMaxVnd) || budgetMaxVnd < budgetMinVnd)) return null;
  if (!isRecord(answers) || Object.keys(answers).length !== CRITERIA_KEYS.length ||
      CRITERIA_KEYS.some(key => typeof answers[key] !== 'string' ||
        !ANSWER_OPTIONS[key].includes(answers[key] as string))) return null;
  return {
    schemaVersion: 1,
    acceptedGenders: [...new Set(acceptedGenders)] as Gender[],
    districtIds: [...new Set(districtIds)] as string[],
    budgetMinVnd,
    budgetMaxVnd: budgetMaxVnd as number | null,
    answers: Object.fromEntries(CRITERIA_KEYS.map(key => [key, answers[key]])) as MatchingAnswers,
  };
}

/** Đọc đúng tên khu vực; không so chuỗi con Q1/Q10, không mở rộng theo thành phố. */
export function parseDistrictIds(value?: string | null): string[] | null {
  if (typeof value !== 'string' || !value.trim()) return null;
  const normalized = normalizedText(value);
  if (['toan tphcm', 'toan tp hcm', 'toan thanh pho ho chi minh',
       'tat ca quan/huyen tphcm', 'tat ca quan/huyen tp hcm'].includes(normalized)) {
    return Object.keys(HCM_DISTRICTS);
  }
  const cityTokens = ['tphcm', 'tp hcm', 'ho chi minh', 'tp ho chi minh', 'thanh pho ho chi minh', 'hcm'];
  const tokens = value.split(/[,;|\n]+/).map(normalizedText).filter(Boolean);
  const ids: string[] = [];
  for (const token of tokens) {
    if (cityTokens.includes(token)) continue;
    const found = Object.entries(HCM_DISTRICTS).find(([id, label]) => {
      const base = normalizedText(label);
      const number = /^quan (\d+)$/.exec(base)?.[1];
      const aliases = [normalizedText(id), base, 'quan ' + base, 'huyen ' + base];
      if (number) aliases.push('q' + number, 'q ' + number, 'quan ' + number.padStart(2, '0'));
      if (id === 'HCM_THU_DUC') aliases.push('tp thu duc', 'thanh pho thu duc');
      return aliases.includes(token);
    });
    if (!found) return null;
    ids.push(found[0]);
  }
  return ids.length ? [...new Set(ids)] : null;
}

/** Chuyển dữ liệu cũ có đơn vị rõ ràng; các khoảng dùng biên đóng như công thức (1). */
export function parseBudgetRange(value?: string | null): BudgetRange | null {
  if (typeof value !== 'string' || !value.trim()) return null;
  let text = value.normalize('NFD').replace(/[\u0300-\u036f]/g, '')
    .replace(/đ/gi, 'd').toLowerCase().replace(/\s+/g, ' ').trim()
    .replace(/\s*\/(?:\s*nguoi\s*\/)?\s*thang$/, '').trim();
  const million = /\btrieu(?:\s*dong)?$/.test(text);
  const vnd = /(?:\bvnd|\bdong|\bd)$/.test(text);
  if (!million && !vnd) return null;
  text = text.replace(/\s*(?:trieu(?:\s*dong)?|vnd|dong|d)$/, '').trim();
  const amount = (token: string): number | null => {
    let number: number;
    if (million) {
      if (!/^\d+(?:[.,]\d{1,6})?$/.test(token)) return null;
      const [whole, fraction = ''] = token.replace(',', '.').split('.');
      number = Number(whole) * 1_000_000 + Number(fraction.padEnd(6, '0'));
    } else {
      if (!/^\d+$/.test(token) && !/^\d{1,3}(?:[.,]\d{3})+$/.test(token)) return null;
      number = Number(token.replace(/[.,]/g, ''));
    }
    return Number.isSafeInteger(number) && number >= 0 ? number : null;
  };
  const upper = /^(?:duoi|den|toi da)\s+(\d[\d.,]*)$/.exec(text);
  if (upper) {
    const max = amount(upper[1]);
    return max === null ? null : { min: 0, max };
  }
  const lower = /^(?:tren|tu)\s+(\d[\d.,]*)$/.exec(text);
  if (lower) {
    const min = amount(lower[1]);
    return min === null ? null : { min, max: null };
  }
  const range = /^(?:tu\s+)?(\d[\d.,]*)\s*(?:-|–|—|den)\s*(\d[\d.,]*)$/.exec(text);
  if (range) {
    const min = amount(range[1]);
    const max = amount(range[2]);
    return min !== null && max !== null && min <= max ? { min, max } : null;
  }
  const exact = amount(text);
  return exact === null ? null : { min: exact, max: exact };
}

interface LegacyCriterion {
  key: CriterionKey;
  titles: string[];
  options: Record<string, string[]>;
}

// Chỉ ánh xạ nhãn đồng nghĩa trong đúng tiêu chí; không tìm trong traits/needType/ghi chú.
const LEGACY_CRITERIA: LegacyCriterion[] = [
  { key: 'schedule', titles: ['Giờ sinh hoạt', 'Giờ giấc', 'Giờ giấc sinh hoạt', 'Lịch sinh hoạt'], options: {
    EARLY: ['Dậy sớm'], NORMAL: ['Bình thường', 'Giờ sinh hoạt bình thường'],
    NIGHT: ['Cú đêm'], FLEXIBLE: ['Linh hoạt', 'Giờ sinh hoạt linh hoạt'],
  } },
  { key: 'noise', titles: ['Không gian sống', 'Tiếng ồn', 'Độ ồn'], options: {
    VERY_QUIET: ['Rất yên tĩnh'], QUIET: ['Khá yên tĩnh'],
    NORMAL: ['Bình thường', 'Không gian sống bình thường'], NOISE_OK: ['Không ngại ồn'],
  } },
  { key: 'cleanliness', titles: ['Gọn gàng', 'Sạch sẽ', 'Vệ sinh'], options: {
    VERY_TIDY: ['Rất gọn gàng'], TIDY: ['Khá gọn gàng'],
    NORMAL: ['Bình thường', 'Gọn gàng ở mức bình thường'], RELAXED: ['Khá thoải mái', 'Khá thoải mái về gọn gàng'],
  } },
  { key: 'smoking', titles: ['Hút thuốc', 'Khói thuốc'], options: {
    NEVER: ['Không hút', 'Không hút thuốc'], SOMETIMES: ['Thỉnh thoảng', 'Thỉnh thoảng hút thuốc'],
    OFTEN: ['Thường xuyên', 'Thường xuyên hút thuốc'],
  } },
  { key: 'pets', titles: ['Thú cưng', 'Nuôi thú cưng', 'Vật nuôi'], options: {
    LOVE: ['Rất thích', 'Rất thích thú cưng', 'Thích thú cưng'],
    ACCEPT: ['Có thể sống cùng', 'Có thể sống cùng thú cưng', 'Có thể ở cùng'],
    INDIFFERENT: ['Không quan tâm', 'Không quan tâm việc nuôi thú cưng'],
    NO_PETS: ['Không muốn ở cùng', 'Không muốn ở cùng thú cưng', 'Không muốn'],
  } },
  { key: 'guests', titles: ['Khách đến phòng', 'Khách', 'Tiếp khách'], options: {
    RARE: ['Hầu như không', 'Hầu như không tiếp khách'],
    SOMETIMES: ['Thỉnh thoảng', 'Thỉnh thoảng tiếp khách'],
    WEEKENDS: ['Cuối tuần', 'Tiếp khách cuối tuần'], OFTEN: ['Thường xuyên', 'Thường xuyên tiếp khách'],
  } },
  { key: 'privacy', titles: ['Riêng tư', 'Quyền riêng tư', 'Không gian riêng'], options: {
    HIGH: ['Rất cần riêng tư', 'Rất cao'], BALANCED: ['Cân bằng', 'Riêng tư cân bằng', 'Vừa phải'],
    COMMUNAL: ['Thích sinh hoạt chung', 'Thoải mái'],
  } },
  { key: 'cooking', titles: ['Nấu ăn', 'Nấu nướng'], options: {
    OFTEN: ['Thường xuyên', 'Thường xuyên nấu ăn', 'Tự nấu hàng ngày'],
    SOMETIMES: ['Thỉnh thoảng', 'Thỉnh thoảng nấu ăn'],
    TAKEOUT: ['Chủ yếu mua ngoài', 'Chủ yếu mua đồ ăn ngoài'],
    NEVER: ['Hầu như không nấu', 'Hầu như không nấu ăn', 'Không nấu ăn'],
  } },
  { key: 'responsibility', titles: ['Trách nhiệm với việc chung', 'Trách nhiệm việc chung', 'Việc chung', 'Trách nhiệm', 'Việc nhà'], options: {
    PROACTIVE: ['Rất chủ động', 'Rất chủ động việc chung'],
    ASSIGNED: ['Thích phân công rõ ràng', 'Thích phân công việc chung rõ ràng', 'Theo phân công'],
    FLEXIBLE: ['Linh hoạt theo tình huống', 'Linh hoạt việc chung theo tình huống', 'Linh hoạt'],
    NORMAL: ['Bình thường', 'Trách nhiệm việc chung ở mức bình thường'],
  } },
  { key: 'costSharing', titles: ['Chia chi phí', 'Chi phí', 'Chia sẻ chi phí'], options: {
    EQUAL: ['Chia đều', 'Chia đều chi phí', 'Chia đều tất cả'],
    USAGE: ['Theo sử dụng', 'Chia chi phí theo sử dụng', 'Dùng bao nhiêu trả bấy nhiêu'],
    SEPARATE: ['Tự thanh toán', 'Tự thanh toán chi phí'],
    FLEXIBLE: ['Linh hoạt', 'Chia chi phí linh hoạt'],
  } },
];

/** Chỉ phần câu trả lời có ngữ cảnh mới làm thay đổi lựa chọn matching đã lưu. */
export function legacyQuestionnaireFingerprint(value?: string | null): string {
  if (typeof value !== 'string') return '';
  const content = value.split(/\r?\n\s*\r?\nGhi chú:\s*\r?\n/i)[0];
  const tokens = content.split(/[;,]/).map(token => token.trim()).filter(Boolean);
  const hasNamedAnswer = tokens.some(token => {
    if (!token.includes(':')) return false;
    const title = normalizedText(token.slice(0, token.indexOf(':')).replace(/\?$/, ''));
    return ['ban muon thay ai', 'gioi tinh mong muon'].includes(title) ||
      LEGACY_CRITERIA.some(item => item.titles.some(alias => normalizedText(alias) === title));
  });
  const hasCompleteOrderedAnswers = tokens.length === 11 && tokens.every(token => !token.includes(':')) &&
    !!legacyGenderPreference(tokens[0]) && LEGACY_CRITERIA.every((criterion, index) =>
      !!legacyOption(criterion, tokens[index + 1]));
  return hasNamedAnswer || hasCompleteOrderedAnswers ? tokens.join(';') : '';
}

function legacyGenderPreference(value: string): Gender[] | null {
  const text = normalizedText(value);
  if (['moi nguoi', 'khong gioi han gioi tinh'].includes(text)) return [...GENDERS];
  if (['nam', 'muon o cung nam'].includes(text)) return ['MALE'];
  if (['nu', 'muon o cung nu'].includes(text)) return ['FEMALE'];
  return null;
}

function legacyOption(criterion: LegacyCriterion, value: string): string | null {
  const text = normalizedText(value);
  return Object.entries(criterion.options).find(([id, labels]) =>
    normalizedText(id) === text || labels.some(label => normalizedText(label) === text))?.[0] ?? null;
}

function parseLegacyLifestyle(value?: string | null): {
  acceptedGenders: Gender[] | null; answers: Partial<MatchingAnswers>;
} {
  const result = { acceptedGenders: null as Gender[] | null, answers: {} as Partial<MatchingAnswers> };
  if (typeof value !== 'string') return result;
  const content = value.split(/\r?\n\s*\r?\nGhi chú:\s*\r?\n/i)[0];
  const tokens = content.split(/[;,]/).map(token => token.trim()).filter(Boolean);
  // Chuỗi cũ không có tên chỉ an toàn khi có đủ 11 đáp án theo đúng thứ tự bảng hỏi.
  if (tokens.length === 11 && tokens.every(token => !token.includes(':'))) {
    result.acceptedGenders = legacyGenderPreference(tokens[0]);
    LEGACY_CRITERIA.forEach((criterion, index) => {
      const answer = legacyOption(criterion, tokens[index + 1]);
      if (answer) result.answers[criterion.key] = answer;
    });
    return result;
  }
  for (const token of tokens) {
    const separator = token.indexOf(':');
    if (separator < 0) return { acceptedGenders: null, answers: {} };
    const title = normalizedText(token.slice(0, separator).replace(/\?$/, ''));
    const value = token.slice(separator + 1).trim();
    if (['ban muon thay ai', 'gioi tinh mong muon'].includes(title)) {
      const genders = legacyGenderPreference(value);
      if (!genders || (result.acceptedGenders &&
          result.acceptedGenders.join() !== genders.join())) return { acceptedGenders: null, answers: {} };
      result.acceptedGenders = genders;
      continue;
    }
    const criterion = LEGACY_CRITERIA.find(item => item.titles.some(alias => normalizedText(alias) === title));
    if (!criterion) return { acceptedGenders: null, answers: {} };
    const answer = legacyOption(criterion, value);
    if (!answer || (result.answers[criterion.key] && result.answers[criterion.key] !== answer)) {
      return { acceptedGenders: null, answers: {} };
    }
    result.answers[criterion.key] = answer;
  }
  return result;
}

export function normalizeMatchingProfile(input: MatchingProfileInput): {
  profile: NormalizedMatchingProfile | null; missingFields: string[];
} {
  const missingFields: string[] = [];
  const gender = normalizeGender(input.gender);
  if (!gender) missingFields.push('gender');
  let preferences: MatchingPreferences | null;
  // Dữ liệu có cấu trúc đã gửi phải hợp lệ; không che lỗi bằng fallback chuỗi cũ.
  if (input.matchingPreferences !== undefined && input.matchingPreferences !== null) {
    preferences = validateMatchingPreferences(input.matchingPreferences);
    if (!preferences) missingFields.push('matchingPreferences');
  } else {
    const legacy = parseLegacyLifestyle(input.lifestyle);
    const districts = parseDistrictIds(input.location);
    const budget = parseBudgetRange(input.budget);
    if (!legacy.acceptedGenders) missingFields.push('acceptedGenders');
    if (!districts) missingFields.push('districtIds');
    if (!budget) missingFields.push('budget');
    for (const key of CRITERIA_KEYS) {
      if (!legacy.answers[key]) missingFields.push('answers.' + key);
    }
    preferences = missingFields.length ? null : {
      schemaVersion: 1,
      acceptedGenders: legacy.acceptedGenders,
      districtIds: districts,
      budgetMinVnd: budget.min,
      budgetMaxVnd: budget.max,
      answers: legacy.answers as MatchingAnswers,
    };
  }
  return missingFields.length || !preferences
    ? { profile: null, missingFields }
    : { profile: { gender, preferences }, missingFields: [] };
}
