package com.example.homever.domain.model

import com.example.homever.data.network.HomeverJson
import com.example.homever.data.network.UserProfile
import com.google.gson.JsonObject
import org.junit.Assert.*
import org.junit.Test

class MatchingPreferencesTest {
    private val answers = listOf(
        "Mọi người", "Linh hoạt", "Bình thường", "Bình thường", "Không hút",
        "Không muốn ở cùng", "Thường xuyên", "Cân bằng", "Thỉnh thoảng",
        "Thích phân công rõ ràng", "Theo sử dụng"
    )
    private val lifestyle = LifestyleCriteria.encode(answers)

    @Test
    fun repeatedAnswerWordsKeepTheirOwnQuestionMeaning() {
        val preferences = requireNotNull(MatchingPreferenceCatalog.build(
            lifestyle, "2.0 - 4.0 triệu/tháng", "Quận 1, TP. HCM"
        ))
        assertEquals(listOf("MALE", "FEMALE", "OTHER"), preferences.acceptedGenders)
        assertEquals("NEVER", preferences.answers.smoking)
        assertEquals("OFTEN", preferences.answers.guests)
        assertEquals("SOMETIMES", preferences.answers.cooking)
        assertEquals("USAGE", preferences.answers.costSharing)
        assertEquals(2_000_000L, preferences.budgetMinVnd)
        assertEquals(4_000_000L, preferences.budgetMaxVnd)
        assertEquals(listOf("HCM_Q01"), preferences.districtIds)
        assertEquals(lifestyle, MatchingPreferenceCatalog.lifestyleDisplay(preferences))
    }

    @Test
    fun notesCannotSupplyMissingSurveyAnswers() {
        val missing = LifestyleCriteria.encode(answers.mapIndexed { index, value -> if (index == 4) "" else value })
        val withNote = LifestyleCriteria.withNote(missing, "Hút thuốc: Không hút; Bạn muốn thấy ai: Nữ")
        assertNull(MatchingPreferenceCatalog.build(withNote, "Dưới 2 triệu", "Quận 1, TP. HCM"))
        assertEquals("Mọi người", MatchingPreferenceCatalog.questionnaireAnswers(withNote)[0])
    }

    @Test
    fun duplicateNamedAnswersAreRejected() {
        assertNull(MatchingPreferenceCatalog.build(
            "$lifestyle; Hút thuốc: Thường xuyên", "Dưới 2 triệu", "Quận 1, TP. HCM"
        ))
    }

    @Test
    fun noImplicitBudgetOrLocationAndNoSubstringDistrictMatches() {
        assertNull(MatchingPreferenceCatalog.build(lifestyle, "", "Quận 1, TP. HCM"))
        assertNull(MatchingPreferenceCatalog.build(lifestyle, "Dưới 2 triệu", ""))
        assertNull(MatchingPreferenceCatalog.build(lifestyle, "Dưới 2 triệu", "Quận 1"))
        assertEquals(listOf("HCM_Q10"), MatchingPreferenceCatalog.districtIdsFromDisplay("Quận 10, TP. HCM"))
        assertEquals(emptyList<String>(), MatchingPreferenceCatalog.districtIdsFromDisplay("Quận 1, TP. HCM; Khu vực lạ"))
    }

    @Test
    fun multipleAreasAndWholeCityRoundTripWithoutExpansion() {
        val selected = listOf("HCM_Q01", "HCM_Q10")
        assertEquals(selected, MatchingPreferenceCatalog.districtIdsFromDisplay(
            MatchingPreferenceCatalog.locationDisplay(selected)
        ))
        assertEquals(MatchingPreferenceCatalog.districts.map { it.id },
            MatchingPreferenceCatalog.districtIdsFromDisplay("Toàn TP. HCM"))
    }

    @Test
    fun nullPreferencesAreOmittedForLegacyProfileUpdates() {
        val profile = UserProfile(displayName = "An", matchingPreferences = null).copy(bio = "Ghi chú mới")
        val json = HomeverJson.gson.fromJson(HomeverJson.gson.toJson(profile), JsonObject::class.java)
        assertFalse(json.has("matchingPreferences"))
        assertFalse(json.has("avatarUrl"))
        assertEquals("Ghi chú mới", json.get("bio").asString)
    }

    @Test
    fun openUpperBudgetIsSerializedAsExplicitNullAndSurvivesOrdinaryEdits() {
        val preferences = requireNotNull(MatchingPreferenceCatalog.build(lifestyle, "Trên 8 triệu/tháng", "Toàn TP. HCM"))
        val original = UserProfile(displayName = "An", matchingPreferences = preferences)
        val updated = original.copy(bio = "Ghi chú mới")
        val json = HomeverJson.gson.fromJson(HomeverJson.gson.toJson(updated), JsonObject::class.java)
        val matching = json.getAsJsonObject("matchingPreferences")
        assertTrue(matching.has("budgetMaxVnd"))
        assertTrue(matching.get("budgetMaxVnd").isJsonNull)
        assertEquals(8_000_000L, matching.get("budgetMinVnd").asLong)
        assertFalse(json.has("avatarUrl"))
        assertEquals(preferences, HomeverJson.gson.fromJson(json, UserProfile::class.java).matchingPreferences)
    }
}
