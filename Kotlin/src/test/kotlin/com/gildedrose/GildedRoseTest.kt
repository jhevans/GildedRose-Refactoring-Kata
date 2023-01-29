package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

internal class GildedRoseTest {
  @Test
  fun `Given an empty array, then empty array remains unchanged`() {
    val items = arrayOf<Item>()
    val app = GildedRose(items)
    app.updateQuality()
    assertThat(app.items.size).isEqualTo(0)
  }

  @Nested
  @DisplayName(value="Given a standard item")
  inner class StandardItemTest {

    @Test
    fun `Then sellIn and quality are reduced by one`() {
      val items = arrayOf(Item("Thing", 10, 20))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = 9, quality = 19))
    }

    @ParameterizedTest(name = "quality of {0} is reduced by 2")
    @ValueSource(ints = [0,-1, -999, Int.MIN_VALUE+1])
    fun `And a sellIn of 0 or less, then quality is reduced by 2`(startingSellIn: Int) {
      val items = arrayOf(Item("Thing", startingSellIn, 20))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = startingSellIn-1, quality = 18))
    }

    @ParameterizedTest(name = "quality of {0} is reduced to 0")
    @ValueSource(ints = [0, 1, 2])
    fun `And a sellIn of 0 and a quality of less than or equal to 2, then quality is reduced to 0`(
      startingQuality: Int
    ) {
      val items = arrayOf(Item("Thing", 0, startingQuality))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = -1, quality = 0))
    }

    @ParameterizedTest(name = "quality of {0} is reduced to {1}")
    @CsvSource("3,1", "999,997", "2147483647,2147483645")
    fun `And a sellIn of 0 and a quality greater than 2, then quality is reduced by 2`(
      startingQuality: Int,
      updatedQuality: Int
    ) {
      val items = arrayOf(Item("Thing", 0, 1))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = -1, quality = 0))
    }
  }

  @Nested
  inner class UndefinedBehaviourTest {
    @Test
    fun `Given input has a negative value for quality then return the value unchanged`() {
      // According to the spec this can't happen but there's nothing preventing instantiation of an Item with a negative
      // quality, in this case it just returns the quality unchanged
      val items = arrayOf(Item("Thing", 0, -1))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = -1, quality = -1))
    }
  }
}


