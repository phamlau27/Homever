package com.example.homever.domain.model

object LifestyleCriteria {
    private const val NOTE_SEPARATOR = "\n\nGhi chú:\n"
    data class Criterion(val title: String, val options: Map<String, String>)

    val criteria = listOf(
        Criterion("Bạn muốn thấy ai", mapOf("Nam" to "Muốn ở cùng nam", "Nữ" to "Muốn ở cùng nữ", "Mọi người" to "Không giới hạn giới tính")),
        Criterion("Giờ sinh hoạt", mapOf("Dậy sớm" to "Dậy sớm", "Bình thường" to "Giờ sinh hoạt bình thường", "Cú đêm" to "Cú đêm", "Linh hoạt" to "Giờ sinh hoạt linh hoạt")),
        Criterion("Không gian sống", mapOf("Rất yên tĩnh" to "Rất yên tĩnh", "Khá yên tĩnh" to "Khá yên tĩnh", "Bình thường" to "Không gian sống bình thường", "Không ngại ồn" to "Không ngại ồn")),
        Criterion("Gọn gàng", mapOf("Rất gọn gàng" to "Rất gọn gàng", "Khá gọn gàng" to "Khá gọn gàng", "Bình thường" to "Gọn gàng ở mức bình thường", "Khá thoải mái" to "Khá thoải mái về gọn gàng")),
        Criterion("Hút thuốc", mapOf("Không hút" to "Không hút thuốc", "Thỉnh thoảng" to "Thỉnh thoảng hút thuốc", "Thường xuyên" to "Thường xuyên hút thuốc")),
        Criterion("Thú cưng", mapOf("Rất thích" to "Rất thích thú cưng", "Có thể sống cùng" to "Có thể sống cùng thú cưng", "Không quan tâm" to "Không quan tâm việc nuôi thú cưng", "Không muốn ở cùng" to "Không muốn ở cùng thú cưng")),
        Criterion("Khách đến phòng", mapOf("Hầu như không" to "Hầu như không tiếp khách", "Thỉnh thoảng" to "Thỉnh thoảng tiếp khách", "Cuối tuần" to "Tiếp khách cuối tuần", "Thường xuyên" to "Thường xuyên tiếp khách")),
        Criterion("Riêng tư", mapOf("Rất cần riêng tư" to "Rất cần riêng tư", "Cân bằng" to "Riêng tư cân bằng", "Thích sinh hoạt chung" to "Thích sinh hoạt chung")),
        Criterion("Nấu ăn", mapOf("Thường xuyên" to "Thường xuyên nấu ăn", "Thỉnh thoảng" to "Thỉnh thoảng nấu ăn", "Chủ yếu mua ngoài" to "Chủ yếu mua đồ ăn ngoài", "Hầu như không nấu" to "Hầu như không nấu ăn")),
        Criterion("Trách nhiệm với việc chung", mapOf("Rất chủ động" to "Rất chủ động việc chung", "Thích phân công rõ ràng" to "Thích phân công việc chung rõ ràng", "Linh hoạt theo tình huống" to "Linh hoạt việc chung theo tình huống", "Bình thường" to "Trách nhiệm việc chung ở mức bình thường")),
        Criterion("Chia chi phí", mapOf("Chia đều" to "Chia đều chi phí", "Theo sử dụng" to "Chia chi phí theo sử dụng", "Tự thanh toán" to "Tự thanh toán chi phí", "Linh hoạt" to "Chia chi phí linh hoạt"))
    )

    private val titleAliases = mapOf(
        "khói thuốc" to "Hút thuốc",
        "giờ giấc" to "Giờ sinh hoạt",
        "giờ giấc sinh hoạt" to "Giờ sinh hoạt",
        "lịch sinh hoạt" to "Giờ sinh hoạt",
        "tiếng ồn" to "Không gian sống",
        "độ ồn" to "Không gian sống",
        "sạch sẽ" to "Gọn gàng",
        "vệ sinh" to "Gọn gàng",
        "nuôi thú cưng" to "Thú cưng",
        "vật nuôi" to "Thú cưng",
        "khách" to "Khách đến phòng",
        "tiếp khách" to "Khách đến phòng",
        "quyền riêng tư" to "Riêng tư",
        "không gian riêng" to "Riêng tư",
        "nấu nướng" to "Nấu ăn",
        "việc chung" to "Trách nhiệm với việc chung",
        "trách nhiệm" to "Trách nhiệm với việc chung",
        "việc nhà" to "Trách nhiệm với việc chung",
        "chi phí" to "Chia chi phí",
        "chia sẻ chi phí" to "Chia chi phí",
        "giới tính mong muốn" to "Bạn muốn thấy ai"
    )

    private fun criterionForTitle(title: String): Criterion? {
        val cleanTitle = title.trim().removeSuffix("?")
        val canonical = titleAliases[cleanTitle.lowercase()] ?: cleanTitle
        return criteria.find { it.title.equals(canonical, ignoreCase = true) }
    }

    private fun optionLabel(criterion: Criterion, answer: String): String? =
        criterion.options.entries.find {
            it.key.equals(answer, ignoreCase = true) || it.value.equals(answer, ignoreCase = true)
        }?.value

    fun encode(answers: List<String>): String =
        criteria.zip(answers).filter { it.second.isNotBlank() }
            .joinToString("; ") { (criterion, answer) -> "${criterion.title}: $answer" }

    fun labels(value: String?): List<String> {
        val tokens = value.orEmpty().substringBefore(NOTE_SEPARATOR).split(";", ",").map(String::trim).filter(String::isNotBlank)
        val assignments = mutableListOf<List<Int>>()
        fun findAssignments(position: Int, nextCriterion: Int, selected: List<Int>) {
            if (position == tokens.size) {
                assignments.add(selected)
                return
            }
            val token = tokens[position]
            val named = if (':' in token) criterionForTitle(token.substringBefore(':')) else null
            val answer = if (named != null) token.substringAfter(':').trim() else token
            for (index in nextCriterion until criteria.size) {
                if ((named == null || criteria[index] == named) && optionLabel(criteria[index], answer) != null) {
                    findAssignments(position + 1, index + 1, selected + index)
                }
            }
        }
        if (tokens.size <= criteria.size) {
            findAssignments(0, 0, emptyList())
        }
        return tokens.mapIndexed { position, token ->
            val title = token.substringBefore(':').trim()
            val answer = token.substringAfter(':', "").trim()
            val named = criterionForTitle(title)
            if (named != null && answer.isNotEmpty()) {
                optionLabel(named, answer) ?: "${named.title}: $answer"
            } else {
                val inferred = assignments.map { it[position] }.distinct().singleOrNull()
                val options = criteria.mapNotNull { optionLabel(it, token) }.distinct()
                when {
                    inferred != null -> optionLabel(criteria[inferred], token) ?: token
                    options.size == 1 -> options.single()
                    else -> token
                }
            }
        }.distinct()
    }

    fun criterionLabels(value: String?): List<String> =
        labels(value).filter { label ->
            criteria.any { optionLabel(it, label) != null } || criterionForTitle(label.substringBefore(':')) != null
        }

    private fun isGenderPreferenceLabel(label: String): Boolean =
        optionLabel(criteria.first(), label) != null ||
            criterionForTitle(label.substringBefore(':')) == criteria.first()

    fun publicLabels(value: String?): List<String> =
        labels(value).filterNot(::isGenderPreferenceLabel)

    fun profileLabels(value: String?, gender: String?): List<String> = buildList {
        gender?.trim()?.takeIf(String::isNotBlank)?.let { add(it) }
        addAll(criterionLabels(value).filterNot(::isGenderPreferenceLabel))
    }

    fun isCriteriaText(value: String): Boolean {
        val tokens = value.substringBefore(NOTE_SEPARATOR).split(";", ",").map(String::trim).filter(String::isNotBlank)
        return tokens.isNotEmpty() && tokens.all { token ->
            criteria.any { optionLabel(it, token) != null } ||
                criterionForTitle(token.substringBefore(':')) != null
        }
    }

    fun note(value: String?): String {
        val content = value.orEmpty()
        return when {
            NOTE_SEPARATOR in content -> content.substringAfter(NOTE_SEPARATOR)
            isCriteriaText(content) -> ""
            else -> content
        }
    }

    fun withNote(original: String?, note: String): String {
        val content = original.orEmpty()
        val choices = content.substringBefore(NOTE_SEPARATOR).takeIf { isCriteriaText(it) }.orEmpty()
        return when {
            choices.isBlank() -> note
            note.isBlank() -> choices
            else -> choices + NOTE_SEPARATOR + note
        }
    }

    fun displayText(value: String?): String {
        val content = value.orEmpty()
        val formattedChoices = publicLabels(content).joinToString(", ")
        return if (NOTE_SEPARATOR in content) {
            listOf(formattedChoices, note(content)).filter(String::isNotBlank).joinToString("\n\n")
        } else {
            formattedChoices
        }
    }
}
