'use strict';

require('reflect-metadata');
const test = require('node:test');
const assert = require('node:assert/strict');
const { MatchingService } = require('../dist/matching/matching.service');
const { UsersService } = require('../dist/users/users.service');
const { Prisma } = require('@prisma/client');
const {
  MATCHING_VERSION,
  CRITERIA_KEYS,
  ANSWER_OPTIONS,
  PUBLISHED_WEIGHTS,
  INITIAL_WEIGHTS,
  ORDINAL_CRITERIA,
  CATEGORICAL_MATRICES,
} = require('../dist/matching/matching.constants');
const {
  normalizeMatchingProfile,
  parseBudgetRange,
  parseDistrictIds,
  validateMatchingPreferences,
} = require('../dist/matching/matching-profile');

const BASE_ANSWERS = {
  schedule: 'EARLY',
  noise: 'VERY_QUIET',
  cleanliness: 'VERY_TIDY',
  smoking: 'NEVER',
  pets: 'LOVE',
  guests: 'RARE',
  privacy: 'HIGH',
  cooking: 'OFTEN',
  costSharing: 'EQUAL',
  responsibility: 'PROACTIVE',
};

function profile(id, preferenceChanges = {}, fieldChanges = {}) {
  const { answers, ...preferences } = preferenceChanges;
  return {
    id,
    cognitoSub: id,
    gender: 'Nam',
    ...fieldChanges,
    matchingPreferences: {
      schemaVersion: 1,
      acceptedGenders: ['MALE', 'FEMALE', 'OTHER'],
      districtIds: ['HCM_Q01'],
      budgetMinVnd: 2000000,
      budgetMaxVnd: 4000000,
      ...preferences,
      answers: answers === null ? null : { ...BASE_ANSWERS, ...answers },
    },
  };
}

const LEGACY_NAMED = [
  'Bạn muốn thấy ai: Mọi người',
  'Giờ sinh hoạt: Dậy sớm',
  'Không gian sống: Rất yên tĩnh',
  'Gọn gàng: Rất gọn gàng',
  'Hút thuốc: Không hút',
  'Thú cưng: Rất thích',
  'Khách đến phòng: Hầu như không',
  'Riêng tư: Rất cần riêng tư',
  'Nấu ăn: Thường xuyên',
  'Trách nhiệm với việc chung: Rất chủ động',
  'Chia chi phí: Chia đều',
].join('; ');

const LEGACY_ORDERED = [
  'Mọi người',
  'Dậy sớm',
  'Rất yên tĩnh',
  'Rất gọn gàng',
  'Không hút',
  'Rất thích',
  'Hầu như không',
  'Rất cần riêng tư',
  'Thường xuyên',
  'Rất chủ động',
  'Chia đều',
].join('; ');

function legacyProfile(id, lifestyle = LEGACY_NAMED) {
  return {
    id,
    cognitoSub: id,
    gender: 'Nam',
    location: 'Quận 1, TP.HCM',
    budget: '2 - 4 triệu/tháng',
    lifestyle,
  };
}

function createService(viewer = profile('viewer'), candidates = []) {
  const prisma = {
    profile: {
      findUnique: async () => viewer,
      findMany: async () => candidates,
    },
  };
  return new MatchingService(prisma);
}

function close(actual, expected, message) {
  assert.ok(Math.abs(actual - expected) < 1e-12, message || `${actual} != ${expected}`);
}

function weightsFor(entries) {
  return Object.fromEntries(CRITERIA_KEYS.map((key) => [key, entries[key] ?? 0]));
}

test('published survey weights remain intact and scoring weights sum to one', () => {
  assert.equal(MATCHING_VERSION, 'semantic-v1');
  close(Object.values(PUBLISHED_WEIGHTS).reduce((sum, weight) => sum + weight, 0), 0.9999);
  close(Object.values(INITIAL_WEIGHTS).reduce((sum, weight) => sum + weight, 0), 1);
  assert.equal(PUBLISHED_WEIGHTS.responsibility, 0.1075);
  assert.equal(PUBLISHED_WEIGHTS.schedule, 0.0897);
  for (const key of CRITERIA_KEYS) {
    close(INITIAL_WEIGHTS[key], PUBLISHED_WEIGHTS[key] / 0.9999);
    assert.ok(INITIAL_WEIGHTS[key] > 0);
  }
});

test('semantic matrices cover every canonical answer, stay bounded and are symmetric', () => {
  assert.deepEqual(Object.keys(CATEGORICAL_MATRICES).sort(),
    ['guests', 'pets', 'cooking', 'costSharing', 'responsibility'].sort());
  for (const [key, matrix] of Object.entries(CATEGORICAL_MATRICES)) {
    assert.deepEqual(Object.keys(matrix).sort(), [...ANSWER_OPTIONS[key]].sort());
    for (const a of ANSWER_OPTIONS[key]) {
      assert.deepEqual(Object.keys(matrix[a]).sort(), [...ANSWER_OPTIONS[key]].sort());
      assert.equal(matrix[a][a], 1);
      for (const b of ANSWER_OPTIONS[key]) {
        assert.ok(Number.isFinite(matrix[a][b]) && matrix[a][b] >= 0 && matrix[a][b] <= 1);
        assert.equal(matrix[a][b], matrix[b][a]);
      }
    }
  }
  assert.equal(CATEGORICAL_MATRICES.guests.RARE.OFTEN, 0.25);
  assert.equal(CATEGORICAL_MATRICES.costSharing.USAGE.SEPARATE, 0.9);
  assert.equal(CATEGORICAL_MATRICES.costSharing.EQUAL.SEPARATE, 0.4);
});

test('gender acceptance is required independently in both directions', () => {
  const service = createService();
  const male = profile('male', { acceptedGenders: ['FEMALE'] });
  const female = profile('female', { acceptedGenders: ['MALE'] }, { gender: 'Nữ' });
  assert.equal(service.passHardFiltering(male, female), true);

  female.matchingPreferences.acceptedGenders = ['FEMALE'];
  assert.equal(service.passHardFiltering(male, female), false, 'candidate rejects viewer');
  assert.equal(service.passHardFiltering(female, male), false);

  female.matchingPreferences.acceptedGenders = ['MALE'];
  male.matchingPreferences.acceptedGenders = ['MALE'];
  assert.equal(service.passHardFiltering(male, female), false, 'viewer rejects candidate');
});

test('districts intersect by canonical ID; Q1 and Q10 do not match through city names', () => {
  const service = createService();
  const q1 = profile('q1', {}, { location: 'Quận 1, TP.HCM' });
  const q10 = profile('q10', { districtIds: ['HCM_Q10'] }, { location: 'Quận 10, TP.HCM' });
  assert.equal(service.passHardFiltering(q1, q10), false);

  q10.matchingPreferences.districtIds = ['HCM_Q10', 'HCM_Q01'];
  assert.equal(service.passHardFiltering(q1, q10), true);
  assert.deepEqual(parseDistrictIds('Quận 1, TP.HCM'), ['HCM_Q01']);
  assert.deepEqual(parseDistrictIds('Quận 10, TP.HCM'), ['HCM_Q10']);
  assert.equal(parseDistrictIds('TP.HCM'), null, 'a city alone does not choose a district');
  assert.equal(parseDistrictIds('Quận 1, Hà Nội'), null);
});

test('budget intervals reject disjoint ranges and include a touching endpoint', () => {
  const service = createService();
  const viewer = profile('viewer');
  const candidate = profile('candidate', { budgetMinVnd: 4000000, budgetMaxVnd: 6000000 });
  assert.equal(service.passHardFiltering(viewer, candidate), true);
  candidate.matchingPreferences.budgetMinVnd = 4000001;
  assert.equal(service.passHardFiltering(viewer, candidate), false);

  viewer.matchingPreferences.budgetMinVnd = 25000000;
  viewer.matchingPreferences.budgetMaxVnd = null;
  candidate.matchingPreferences.budgetMinVnd = 30000000;
  candidate.matchingPreferences.budgetMaxVnd = 35000000;
  assert.equal(service.passHardFiltering(viewer, candidate), true, 'no artificial 20M ceiling');
});

test('legacy budgets parse decimal commas, VND amounts and unbounded upper limits', () => {
  assert.deepEqual(parseBudgetRange('2,5 - 3,5 triệu/tháng'), { min: 2500000, max: 3500000 });
  assert.deepEqual(parseBudgetRange('2,01 - 2,05 triệu/tháng'), { min: 2010000, max: 2050000 });
  assert.deepEqual(parseBudgetRange('2.000.000 - 4.000.000 đ/tháng'), { min: 2000000, max: 4000000 });
  assert.deepEqual(parseBudgetRange('2.000.000 Đ/tháng'), { min: 2000000, max: 2000000 });
  assert.deepEqual(parseBudgetRange('Trên 25 triệu/tháng'), { min: 25000000, max: null });
  assert.equal(parseBudgetRange('không rõ'), null);
  assert.equal(parseBudgetRange(null), null);
  assert.equal(parseBudgetRange('5 - 3 triệu/tháng'), null);
});

test('missing hard preferences are incomplete rather than universal acceptance', () => {
  const service = createService();
  for (const key of ['acceptedGenders', 'districtIds', 'budgetMinVnd', 'budgetMaxVnd']) {
    const incomplete = profile('incomplete');
    delete incomplete.matchingPreferences[key];
    const normalized = normalizeMatchingProfile(incomplete);
    assert.equal(normalized.profile, null, key);
    assert.ok(normalized.missingFields.length > 0);
    assert.equal(service.passHardFiltering(profile('complete'), incomplete), false);
  }
  assert.equal(normalizeMatchingProfile({ id: 'empty', gender: 'Nam' }).profile, null);
});

test('typed preferences reject invalid values without falling back to legacy text', () => {
  const invalid = profile('invalid', { answers: { smoking: 'UNKNOWN' } }, {
    ...legacyProfile('invalid'),
  });
  assert.equal(validateMatchingPreferences(invalid.matchingPreferences), null);
  assert.equal(normalizeMatchingProfile(invalid).profile, null);
  assert.equal(createService().passHardFiltering(profile('complete'), invalid), false);

  for (const changes of [
    { schemaVersion: 2 },
    { acceptedGenders: [] },
    { acceptedGenders: ['UNKNOWN'] },
    { districtIds: ['HCM_Q99'] },
    { budgetMinVnd: -1 },
    { budgetMinVnd: 5000000, budgetMaxVnd: 4000000 },
  ]) {
    assert.equal(validateMatchingPreferences(profile('x', changes).matchingPreferences), null);
  }
});

test('named and complete ordered legacy questionnaires normalize without cross-criterion leakage', () => {
  const service = createService();
  for (const value of [LEGACY_NAMED, LEGACY_ORDERED]) {
    const legacy = legacyProfile('legacy', value);
    assert.notEqual(normalizeMatchingProfile(legacy).profile, null);
    assert.equal(service.passHardFiltering(profile('canonical'), legacy), true);
    const score = service.calculateHarmonicScore(profile('canonical'), legacy);
    close(score.cr, 1, 'frequent cooking must not change NEVER smoking');
  }
});

test('notes and free text do not overwrite canonical or named questionnaire answers', () => {
  const service = createService();
  const noise = 'Thường xuyên hút thuốc; Hầu như không nấu; Không muốn ở cùng thú cưng';
  const typed = profile('typed', {}, { lifestyle: noise, traits: noise, needType: noise });
  close(service.calculateHarmonicScore(profile('canonical'), typed).cr, 1);
  const legacy = legacyProfile('legacy', LEGACY_NAMED + '\n\nGhi chú:\n' + noise);
  legacy.traits = 'Tôi có thể thường xuyên tiếp khách';
  close(service.calculateHarmonicScore(profile('canonical'), legacy).cr, 1);
});

test('missing and unknown answers cannot become neutral or perfect synthetic scores', () => {
  const service = createService();
  const missing = { ...BASE_ANSWERS };
  delete missing.smoking;
  assert.throws(() => service.calculateCriteriaScores(missing, BASE_ANSWERS));
  assert.throws(() => service.calculateCriteriaScores(
    { ...BASE_ANSWERS, pets: 'UNRECOGNIZED' }, BASE_ANSWERS,
  ));
  const incomplete = profile('incomplete');
  delete incomplete.matchingPreferences.answers.smoking;
  assert.equal(normalizeMatchingProfile(incomplete).profile, null);
  assert.throws(() => service.calculateHarmonicScore(profile('complete'), incomplete));
});

test('ordinal endpoints score zero while flexible schedules accept each concrete schedule', () => {
  const service = createService();
  for (const [key, config] of Object.entries(ORDINAL_CRITERIA)) {
    const ranks = Object.entries(config).filter(([option]) => option !== '_maxRank');
    const low = ranks.find(([, rank]) => rank === 1)[0];
    const high = ranks.find(([, rank]) => rank === config._maxRank)[0];
    const scores = service.calculateCriteriaScores(
      { ...BASE_ANSWERS, [key]: low }, { ...BASE_ANSWERS, [key]: high },
    );
    close(scores[key], 0, key);
  }
  assert.equal(ORDINAL_CRITERIA.schedule.FLEXIBLE, undefined);
  for (const schedule of ['EARLY', 'NORMAL', 'NIGHT', 'FLEXIBLE']) {
    assert.equal(service.calculateCriteriaScores(
      { ...BASE_ANSWERS, schedule: 'FLEXIBLE' }, { ...BASE_ANSWERS, schedule },
    ).schedule, 1);
  }
});

test('zero weights remain zero and directional scores use their own validated vectors', () => {
  const service = createService();
  const u = profile('u');
  const v = profile('v', { answers: { smoking: 'OFTEN' } });
  const scheduleOnly = weightsFor({ schedule: 1 });
  const score = service.calculateHarmonicScore(u, v, scheduleOnly, scheduleOnly);
  close(score.cu, 1);
  close(score.cv, 1);
  close(score.cr, 1);

  const halfSmoking = weightsFor({ schedule: 0.5, smoking: 0.5 });
  const directional = service.calculateHarmonicScore(u, v, halfSmoking, scheduleOnly);
  close(directional.cu, 0.5);
  close(directional.cv, 1);
  close(directional.cr, 2 / 3);
  assert.equal(directional.percentage, 67);
  assert.equal(directional.modelVersion, MATCHING_VERSION);

  const zeroDirection = service.calculateHarmonicScore(
    u, v, weightsFor({ smoking: 1 }), scheduleOnly,
  );
  close(zeroDirection.cr, 0);
  for (const invalid of [
    weightsFor({}),
    weightsFor({ schedule: -1, smoking: 2 }),
    weightsFor({ schedule: Number.NaN }),
    weightsFor({ schedule: Number.POSITIVE_INFINITY }),
    { ...scheduleOnly, unrecognized: 0.1 },
  ]) {
    assert.throws(() => service.calculateHarmonicScore(u, v, invalid, scheduleOnly));
  }
});

test('Top-K returns only complete candidates passing every hard constraint, without backfill', async () => {
  const viewer = profile('viewer');
  const badGender = profile('gender', { acceptedGenders: ['FEMALE'] });
  const badDistrict = profile('district', { districtIds: ['HCM_Q10'] });
  const badBudget = profile('budget', { budgetMinVnd: 6000000, budgetMaxVnd: 8000000 });
  const incomplete = profile('incomplete');
  delete incomplete.matchingPreferences.answers.noise;
  const valid = profile('valid', { answers: { smoking: 'OFTEN', schedule: 'NIGHT' } });
  const candidates = await createService(
    viewer, [badGender, badDistrict, badBudget, incomplete, valid],
  ).getTopCandidates('viewer', 20);
  assert.deepEqual(candidates.map((candidate) => candidate.profile.id), ['valid']);
  const result = candidates[0].score;
  assert.equal(result.percentage, Math.round(result.cr * 100));
});

test('an incomplete viewer receives an explicit 422 instead of fabricated matches', async () => {
  const incomplete = profile('viewer');
  delete incomplete.matchingPreferences.acceptedGenders;
  await assert.rejects(
    createService(incomplete, [profile('valid')]).getTopCandidates('viewer'),
    (error) => error.getStatus() === 422 &&
      error.getResponse().code === 'MATCHING_PROFILE_INCOMPLETE',
  );
});

test('Top-K sorts by full precision and breaks exact score ties by stable ID', async () => {
  const viewer = profile('viewer');
  const low = profile('a-low', { answers: { responsibility: 'NORMAL', costSharing: 'SEPARATE' } });
  const high = profile('z-high', { answers: { privacy: 'BALANCED', cooking: 'TAKEOUT' } });
  const nearTie = createService(viewer, [low, high]);
  const lowScore = nearTie.calculateHarmonicScore(viewer, low).cr;
  const highScore = nearTie.calculateHarmonicScore(viewer, high).cr;
  assert.ok(highScore > lowScore);
  assert.equal(highScore.toFixed(4), lowScore.toFixed(4), 'real scores share four rounded decimals');
  const ordered = await nearTie.getTopCandidates('viewer', 1);
  assert.deepEqual(ordered.map((candidate) => candidate.profile.id), ['z-high']);

  const ties = await createService(
    profile('viewer'), [profile('z-tie'), profile('a-tie')],
  ).getTopCandidates('viewer', 2);
  assert.deepEqual(ties.map((candidate) => candidate.profile.id), ['a-tie', 'z-tie']);
});

test('Top-K validates integer limits between one and one hundred', async () => {
  for (const limit of [0, -1, 1.5, 101, NaN, Infinity, '20']) {
    await assert.rejects(createService().getTopCandidates('viewer', limit));
  }
});

function createUsersService(existing) {
  let saved;
  const prisma = {
    profile: {
      findUnique: async () => existing,
      upsert: async (query) => {
        saved = query;
        return query.update;
      },
    },
  };
  return { service: new UsersService(prisma), saved: () => saved };
}

test('saving unrelated profile text preserves existing structured matching choices', async () => {
  const existing = profile('viewer');
  const users = createUsersService(existing);
  await users.service.createProfile('viewer', {
    bio: 'Updated introduction',
    traits: 'Thường xuyên hút thuốc is only a free-text note',
    avatarUrl: 'https://example.test/new-avatar.jpg',
  });
  assert.equal(users.saved().update.matchingPreferences, undefined);
  assert.deepEqual(existing.matchingPreferences.answers, BASE_ANSWERS);
});

test('changing own gender preserves preferences because acceptance is a separate field', async () => {
  const users = createUsersService(profile('viewer'));
  await users.service.createProfile('viewer', { gender: 'Nữ' });
  assert.equal(users.saved().update.gender, 'Nữ');
  assert.equal(users.saved().update.matchingPreferences, undefined);
});

test('profile saves validate structured choices before persisting them', async () => {
  const requested = profile('new', { districtIds: ['HCM_Q01', 'HCM_Q10'] }).matchingPreferences;
  const users = createUsersService(null);
  await users.service.createProfile('new', { gender: 'Nam', matchingPreferences: requested });
  assert.deepEqual(users.saved().create.matchingPreferences, requested);
  assert.deepEqual(users.saved().update.matchingPreferences, requested);

  for (const invalid of [
    { ...requested, schemaVersion: 2 },
    { ...requested, answers: { ...requested.answers, cooking: 'UNKNOWN' } },
    null,
  ]) {
    const rejected = createUsersService(profile('viewer'));
    await assert.rejects(
      rejected.service.createProfile('viewer', { matchingPreferences: invalid }),
      (error) => error.getStatus() === 400 &&
        error.getResponse().code === 'INVALID_MATCHING_PREFERENCES',
    );
    assert.equal(rejected.saved(), undefined, 'invalid preferences must not reach upsert');
  }
});

test('legacy profile edits recompute choices from exact named answers and new budget', async () => {
  const users = createUsersService(legacyProfile('viewer'));
  await users.service.createProfile('viewer', { budget: '3 - 5 triệu/tháng' });
  const saved = users.saved().update.matchingPreferences;
  assert.notEqual(saved, Prisma.DbNull);
  assert.equal(saved.budgetMinVnd, 3000000);
  assert.equal(saved.budgetMaxVnd, 5000000);
  assert.deepEqual(saved.answers, BASE_ANSWERS);
  assert.deepEqual(saved.districtIds, ['HCM_Q01']);
});

test('invalid legacy changes clear stale structured preferences instead of keeping old eligibility', async () => {
  const existing = { ...legacyProfile('viewer'), matchingPreferences: profile('viewer').matchingPreferences };
  const users = createUsersService(existing);
  await users.service.createProfile('viewer', { location: 'Quận 1, Hà Nội' });
  assert.equal(users.saved().update.matchingPreferences, Prisma.DbNull);
});

test('free prose lifestyle edits preserve authoritative structured choices', async () => {
  const notes = [
    'Thường xuyên nghe nhạc, không muốn tiếp khách là ghi chú của tôi.',
    'âm nhạc, đọc sách, cà phê, thể thao, hội họa, phim ảnh, du lịch, làm vườn, cắm trại, bơi lội, chạy bộ',
  ];
  for (const lifestyle of notes) {
    const users = createUsersService(profile('viewer'));
    await users.service.createProfile('viewer', { lifestyle });
    assert.equal(users.saved().update.matchingPreferences, undefined);
  }
});

test('editing only appended questionnaire notes preserves structured preferences', async () => {
  const existing = { ...legacyProfile('viewer'), matchingPreferences: profile('viewer').matchingPreferences };
  const users = createUsersService(existing);
  await users.service.createProfile('viewer', {
    lifestyle: LEGACY_NAMED + '\n\nGhi chú:\nThường xuyên hút thuốc là câu ví dụ trong ghi chú.',
  });
  assert.equal(users.saved().update.matchingPreferences, undefined);
});

test('explicitly clearing a legacy budget clears eligibility rather than restoring old values', async () => {
  const existing = { ...legacyProfile('viewer'), matchingPreferences: profile('viewer').matchingPreferences };
  const users = createUsersService(existing);
  await users.service.createProfile('viewer', { budget: null });
  assert.equal(users.saved().update.matchingPreferences, Prisma.DbNull);
});
