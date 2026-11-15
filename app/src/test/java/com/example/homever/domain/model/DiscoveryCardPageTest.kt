package com.example.homever.domain.model

import org.junit.Assert.*
import org.junit.Test

class DiscoveryCardPageTest {
    @Test
    fun onePhotoRepeatsWhileAllCriteriaAreEvenlyDistributed() {
        val tags = List(11) { "Tiêu chí $it" }
        val pages = discoveryCardPages(listOf("avatar"), tags, emptyList())
        assertEquals(3, pages.size)
        assertTrue(pages.all { it.photo == "avatar" })
        assertEquals(listOf(4, 4, 3), pages.map { it.tags.size })
        assertEquals(tags, pages.flatMap { it.tags })
    }

    @Test
    fun notesGetSeparatePagesWithoutLosingText() {
        val note = List(80) { "nội dung" }.joinToString(" ")
        val pages = discoveryCardPages(listOf("avatar"), listOf("Không hút thuốc"), listOf("Giới thiệu" to note))
        assertEquals(note, pages.mapNotNull { it.note }.joinToString(" "))
        assertTrue(pages.drop(1).all { it.noteTitle == "Giới thiệu" })
        assertTrue(pages.all { it.photo == "avatar" })
    }

    @Test
    fun everyPhotoIsUsedAndAnEmptyProfileHasASafePage() {
        val photos = listOf("first", "second", "third")
        assertEquals(photos, discoveryCardPages(photos, emptyList(), emptyList()).map { it.photo })
        val empty = discoveryCardPages(emptyList(), emptyList(), emptyList())
        assertEquals(1, empty.size)
        assertNull(empty.single().photo)
    }
}
