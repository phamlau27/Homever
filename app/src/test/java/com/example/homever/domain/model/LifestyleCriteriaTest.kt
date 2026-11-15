package com.example.homever.domain.model

import org.junit.Assert.*
import org.junit.Test

class LifestyleCriteriaTest {
    @Test
    fun labelledChoicesKeepTheirMeaningAcrossCategories() {
        val encoded = LifestyleCriteria.encode(listOf(
            "Mọi người", "Linh hoạt", "Bình thường", "Bình thường", "Thỉnh thoảng",
            "Không quan tâm", "Thường xuyên", "Cân bằng", "Thỉnh thoảng", "Bình thường", "Linh hoạt"
        ))
        assertEquals(listOf(
            "Không giới hạn giới tính", "Giờ sinh hoạt linh hoạt", "Không gian sống bình thường",
            "Gọn gàng ở mức bình thường", "Thỉnh thoảng hút thuốc", "Không quan tâm việc nuôi thú cưng",
            "Thường xuyên tiếp khách", "Riêng tư cân bằng", "Thỉnh thoảng nấu ăn",
            "Trách nhiệm việc chung ở mức bình thường", "Chia chi phí linh hoạt"
        ), LifestyleCriteria.labels(encoded))
    }

    @Test
    fun fullLegacyQuestionnaireUsesOriginalQuestionOrder() {
        val legacy = "Nam, Dậy sớm, Bình thường, Bình thường, Không hút, Rất thích, Thỉnh thoảng, Cân bằng, Thường xuyên, Bình thường, Linh hoạt"
        val labels = LifestyleCriteria.labels(legacy)
        assertEquals("Không hút thuốc", labels[4])
        assertEquals("Thỉnh thoảng tiếp khách", labels[6])
        assertEquals("Riêng tư cân bằng", labels[7])
        assertEquals("Thường xuyên nấu ăn", labels[8])
    }

    @Test
    fun partialLegacyDataDoesNotInventAnAmbiguousCategory() {
        assertEquals(listOf("Thỉnh thoảng"), LifestyleCriteria.labels("Thỉnh thoảng"))
        assertEquals(listOf("Không hút thuốc", "Riêng tư cân bằng"), LifestyleCriteria.labels("Không hút, Cân bằng"))
    }

    @Test
    fun freeNotesAndAlreadyFormattedLabelsArePreserved() {
        val text = "Không hút thuốc, Riêng tư cân bằng"
        assertEquals(text, LifestyleCriteria.displayText(text))
        assertEquals(listOf("Thích đọc sách"), LifestyleCriteria.labels("Thích đọc sách"))
        assertFalse(LifestyleCriteria.isCriteriaText("Thích đọc sách"))
        assertTrue(LifestyleCriteria.isCriteriaText("Hút thuốc: Không hút; Riêng tư: Cân bằng"))
    }

    @Test
    fun oldCategoryNamesAndCasingUseTheSameLabels() {
        val old = "Khói thuốc: Thỉnh thoảng, Quyền riêng tư: Cân bằng, Giờ giấc: Linh hoạt"
        assertEquals(listOf("Thỉnh thoảng hút thuốc", "Riêng tư cân bằng", "Giờ sinh hoạt linh hoạt"), LifestyleCriteria.criterionLabels(old))
        assertTrue(LifestyleCriteria.isCriteriaText(old))
        assertEquals("", LifestyleCriteria.note(old))
        assertEquals(listOf("Không hút thuốc"), LifestyleCriteria.labels("không hút"))
        val mixed = "Bình thường, Khá yên tĩnh, Khá gọn gàng, Khói thuốc: Không hút"
        assertEquals("Giờ sinh hoạt bình thường", LifestyleCriteria.labels(mixed).first())
    }

    @Test
    fun editingNotesPreservesCriteriaAndDoesNotTurnNotesIntoTags() {
        val choices = "Hút thuốc: Không hút; Riêng tư: Cân bằng"
        val saved = LifestyleCriteria.withNote(choices, "Mình thích đọc sách, nghe nhạc.")
        assertEquals(listOf("Không hút thuốc", "Riêng tư cân bằng"), LifestyleCriteria.criterionLabels(saved))
        assertEquals("Mình thích đọc sách, nghe nhạc.", LifestyleCriteria.note(saved))
        assertEquals(choices, LifestyleCriteria.withNote(saved, ""))
        assertEquals("Ghi chú mới", LifestyleCriteria.note(LifestyleCriteria.withNote(saved, "Ghi chú mới")))
    }

    @Test
    fun skippedAnswersKeepTheirCategoryWhenSaved() {
        val answers = List(11) { index -> if (index == 6) "Thỉnh thoảng" else "" }
        assertEquals("Khách đến phòng: Thỉnh thoảng", LifestyleCriteria.encode(answers))
        assertEquals(listOf("Thỉnh thoảng tiếp khách"), LifestyleCriteria.labels(LifestyleCriteria.encode(answers)))
    }
}
