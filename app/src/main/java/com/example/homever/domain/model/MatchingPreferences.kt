package com.example.homever.domain.model

data class MatchingAnswers(
    val schedule: String,
    val noise: String,
    val cleanliness: String,
    val smoking: String,
    val pets: String,
    val guests: String,
    val privacy: String,
    val cooking: String,
    val costSharing: String,
    val responsibility: String
)

data class MatchingPreferences(
    val schemaVersion: Int = 1,
    val acceptedGenders: List<String>,
    val districtIds: List<String>,
    val budgetMinVnd: Long,
    val budgetMaxVnd: Long?,
    val answers: MatchingAnswers
)

data class SearchDistrict(val id: String, val label: String)

/** Stable Homever search areas; these IDs do not describe administrative boundaries. */
object MatchingPreferenceCatalog {
    val districts = listOf(
        SearchDistrict("HCM_Q01", "Quận 1, TP. HCM"),
        SearchDistrict("HCM_Q03", "Quận 3, TP. HCM"),
        SearchDistrict("HCM_Q04", "Quận 4, TP. HCM"),
        SearchDistrict("HCM_Q05", "Quận 5, TP. HCM"),
        SearchDistrict("HCM_Q06", "Quận 6, TP. HCM"),
        SearchDistrict("HCM_Q07", "Quận 7, TP. HCM"),
        SearchDistrict("HCM_Q08", "Quận 8, TP. HCM"),
        SearchDistrict("HCM_Q10", "Quận 10, TP. HCM"),
        SearchDistrict("HCM_Q11", "Quận 11, TP. HCM"),
        SearchDistrict("HCM_Q12", "Quận 12, TP. HCM"),
        SearchDistrict("HCM_BINH_TAN", "Quận Bình Tân, TP. HCM"),
        SearchDistrict("HCM_BINH_THANH", "Quận Bình Thạnh, TP. HCM"),
        SearchDistrict("HCM_GO_VAP", "Quận Gò Vấp, TP. HCM"),
        SearchDistrict("HCM_PHU_NHUAN", "Quận Phú Nhuận, TP. HCM"),
        SearchDistrict("HCM_TAN_BINH", "Quận Tân Bình, TP. HCM"),
        SearchDistrict("HCM_TAN_PHU", "Quận Tân Phú, TP. HCM"),
        SearchDistrict("HCM_THU_DUC", "TP. Thủ Đức, TP. HCM"),
        SearchDistrict("HCM_BINH_CHANH", "Bình Chánh, TP. HCM"),
        SearchDistrict("HCM_CAN_GIO", "Cần Giờ, TP. HCM"),
        SearchDistrict("HCM_CU_CHI", "Củ Chi, TP. HCM"),
        SearchDistrict("HCM_HOC_MON", "Hóc Môn, TP. HCM"),
        SearchDistrict("HCM_NHA_BE", "Nhà Bè, TP. HCM")
    )

    private val optionIds = listOf(
        linkedMapOf("Nam" to "MALE", "Nữ" to "FEMALE", "Mọi người" to "ALL"),
        linkedMapOf("Dậy sớm" to "EARLY", "Bình thường" to "NORMAL", "Cú đêm" to "NIGHT", "Linh hoạt" to "FLEXIBLE"),
        linkedMapOf("Rất yên tĩnh" to "VERY_QUIET", "Khá yên tĩnh" to "QUIET", "Bình thường" to "NORMAL", "Không ngại ồn" to "NOISE_OK"),
        linkedMapOf("Rất gọn gàng" to "VERY_TIDY", "Khá gọn gàng" to "TIDY", "Bình thường" to "NORMAL", "Khá thoải mái" to "RELAXED"),
        linkedMapOf("Không hút" to "NEVER", "Thỉnh thoảng" to "SOMETIMES", "Thường xuyên" to "OFTEN"),
        linkedMapOf("Rất thích" to "LOVE", "Có thể sống cùng" to "ACCEPT", "Không quan tâm" to "INDIFFERENT", "Không muốn ở cùng" to "NO_PETS"),
        linkedMapOf("Hầu như không" to "RARE", "Thỉnh thoảng" to "SOMETIMES", "Cuối tuần" to "WEEKENDS", "Thường xuyên" to "OFTEN"),
        linkedMapOf("Rất cần riêng tư" to "HIGH", "Cân bằng" to "BALANCED", "Thích sinh hoạt chung" to "COMMUNAL"),
        linkedMapOf("Thường xuyên" to "OFTEN", "Thỉnh thoảng" to "SOMETIMES", "Chủ yếu mua ngoài" to "TAKEOUT", "Hầu như không nấu" to "NEVER"),
        linkedMapOf("Rất chủ động" to "PROACTIVE", "Thích phân công rõ ràng" to "ASSIGNED", "Linh hoạt theo tình huống" to "FLEXIBLE", "Bình thường" to "NORMAL"),
        linkedMapOf("Chia đều" to "EQUAL", "Theo sử dụng" to "USAGE", "Tự thanh toán" to "SEPARATE", "Linh hoạt" to "FLEXIBLE")
    )

    /** Only named answers before the notes section can become matching input. */
    fun questionnaireAnswers(lifestyle: String?): List<String> {
        val entries = lifestyle.orEmpty().substringBefore("\n\nGhi chú:\n")
            .split(";").filter { ':' in it }
            .map { it.substringBefore(':').trim() to it.substringAfter(':').trim() }
        return LifestyleCriteria.criteria.mapIndexed { index, criterion ->
            val answers = entries.filter { it.first == criterion.title }.map { it.second }
            answers.singleOrNull()?.takeIf { it in optionIds[index] }.orEmpty()
        }
    }

    fun lifestyleDisplay(preferences: MatchingPreferences): String {
        val a = preferences.answers
        val ids = listOf(
            if (preferences.acceptedGenders.toSet() == setOf("MALE", "FEMALE", "OTHER")) "ALL"
            else preferences.acceptedGenders.singleOrNull().orEmpty(),
            a.schedule, a.noise, a.cleanliness, a.smoking, a.pets, a.guests,
            a.privacy, a.cooking, a.responsibility, a.costSharing
        )
        return LifestyleCriteria.encode(ids.mapIndexed { index, id ->
            optionIds[index].entries.singleOrNull { it.value == id }?.key.orEmpty()
        })
    }

    fun districtIdsFromDisplay(value: String): List<String> {
        if (value == "Toàn TP. HCM") return districts.map { it.id }
        if (value.isBlank()) return emptyList()
        val tokens = value.split(";").map(String::trim)
        val ids = tokens.map { token -> districts.singleOrNull { it.label == token }?.id ?: return emptyList() }
        return ids.distinct()
    }

    fun locationDisplay(ids: List<String>): String {
        val selected = districts.filter { it.id in ids }
        return if (selected.size == districts.size) "Toàn TP. HCM"
        else selected.joinToString("; ") { it.label }
    }

    fun budgetLabel(value: String): String = when (value) {
        "Dưới 2 triệu" -> "0 - 2 triệu/tháng"
        "Trên 8 triệu/tháng" -> "Từ 8 triệu/tháng"
        else -> value
    }

    fun budgetRange(value: String): Pair<Long, Long?>? = when (value) {
        "Dưới 2 triệu" -> 0L to 2_000_000L
        "2.0 - 4.0 triệu/tháng" -> 2_000_000L to 4_000_000L
        "4.0 - 6.0 triệu/tháng" -> 4_000_000L to 6_000_000L
        "6.0 - 8.0 triệu/tháng" -> 6_000_000L to 8_000_000L
        "Trên 8 triệu/tháng" -> 8_000_000L to null
        else -> null
    }

    fun budgetDisplay(preferences: MatchingPreferences): String =
        listOf("Dưới 2 triệu", "2.0 - 4.0 triệu/tháng", "4.0 - 6.0 triệu/tháng", "6.0 - 8.0 triệu/tháng", "Trên 8 triệu/tháng")
            .singleOrNull { budgetRange(it) == preferences.budgetMinVnd to preferences.budgetMaxVnd }.orEmpty()

    fun build(lifestyle: String, budget: String, location: String): MatchingPreferences? {
        val choices = questionnaireAnswers(lifestyle)
        if (choices.any(String::isBlank)) return null
        val bounds = budgetRange(budget) ?: return null
        val districtIds = districtIdsFromDisplay(location).takeIf { it.isNotEmpty() } ?: return null
        val ids = choices.mapIndexed { index, choice -> optionIds[index].getValue(choice) }
        return MatchingPreferences(
            acceptedGenders = if (ids[0] == "ALL") listOf("MALE", "FEMALE", "OTHER") else listOf(ids[0]),
            districtIds = districtIds,
            budgetMinVnd = bounds.first,
            budgetMaxVnd = bounds.second,
            answers = MatchingAnswers(
                schedule = ids[1], noise = ids[2], cleanliness = ids[3], smoking = ids[4],
                pets = ids[5], guests = ids[6], privacy = ids[7], cooking = ids[8],
                responsibility = ids[9], costSharing = ids[10]
            )
        )
    }
}
