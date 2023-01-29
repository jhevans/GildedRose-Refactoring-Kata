package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class GildedRoseTest {
    // There is only a single method so for the sake of brevity I've excluded the 'when' step as it's always along the
    // lines of 'When quality is updated'
    @Test
    fun `Given an empty array, then empty array remains unchanged`() {
        val items = arrayOf<Item>()
        val app = GildedRose(items)
        app.updateQuality()
        assertThat(app.items.size).isEqualTo(0)
    }


    @Test
    fun `Given a single standard item, then sellIn and quality are reduced by one`() {
        val items = arrayOf(Item("Thing", 10, 20))
        val app = GildedRose(items)
        app.updateQuality()
        assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = 9, quality = 19))
    }

    @Test
    fun `Given a single standard item with sellIn of 0, then quality is reduced by 2`() {
        val items = arrayOf(Item("Thing", 0, 20))
        val app = GildedRose(items)
        app.updateQuality()
        assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = -1, quality = 18))
    }

    @ParameterizedTest(name = "quality of {0} is reduced to 0")
    @ValueSource(ints = [0, 1, 2])
    fun `Given a single standard item with sellIn of 0 and a quality of less than or equal to 2, then quality is reduced to 0`(startingQuality:Int) {
        val items = arrayOf(Item("Thing", 0, startingQuality))
        val app = GildedRose(items)
        app.updateQuality()
        assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = -1, quality = 0))
    }

    @ParameterizedTest(name = "quality of {0} is reduced to {1}")
    @CsvSource("3,1", "999,997", "2147483647,2147483645")
    fun `Given a single standard item with sellIn of 0 and a quality greater than 2, then quality is reduced by 2`(startingQuality:Int, updatedQuality: Int) {
        val items = arrayOf(Item("Thing", 0, 1))
        val app = GildedRose(items)
        app.updateQuality()
        assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = -1, quality = 0))
    }

    // TODO: Check what happens when passed negative values
}


