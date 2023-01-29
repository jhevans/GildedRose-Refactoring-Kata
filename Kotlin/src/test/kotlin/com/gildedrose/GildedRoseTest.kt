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
  @DisplayName(value = "Given a standard item")
  inner class StandardItemTest {
    // TODO: This doesn't cover a large range of item names, possibly missed a trick testing this way, may be worth refactoring
    // Might be better to refactor these tests to be behaviour-centric rather than item-centric 🤔

    @Test
    fun `Then sellIn and quality are reduced by one`() {
      val items = arrayOf(Item("Thing", 10, 20))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = 9, quality = 19))
    }

    @ParameterizedTest(name = "quality of {0} is reduced by 2")
    @ValueSource(ints = [0, -1, -999, Int.MIN_VALUE + 1])
    fun `And a sellIn of 0 or less, then quality is reduced by 2`(startingSellIn: Int) {
      val items = arrayOf(Item("Thing", startingSellIn, 20))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item("Thing", sellIn = startingSellIn - 1, quality = 18))
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
  @DisplayName(value = "Given an aged Brie")
  inner class AgedBrieTest {
    @ParameterizedTest(name = "quality of {0} is increased by 1")
    @ValueSource(ints = [0, 1, 5, 49])
    fun `Then sellIn is reduced by one, quality increases by one`(startingQuality: Int) {
      val items = arrayOf(Item("Aged Brie", 10, startingQuality))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Aged Brie", sellIn = 9, quality = startingQuality + 1))
    }

    @Test
    fun `And a quality of 50, then quality stays at 50`() {
      val items = arrayOf(Item("Aged Brie", 10, 50))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Aged Brie", sellIn = 9, quality = 50))
    }
  }

  @Nested
  inner class UndefinedBehaviourTest {
    @Test
    fun `Given input has a negative value for quality then return the value unchanged`() {
      val items = arrayOf(Item("Thing", 0, -1))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item("Thing", sellIn = -1, quality = -1))
    }

    @ParameterizedTest
    @DisplayName(value = "Given {0} then quality is 51")
    @CsvSource(delimiter = '|', value = ["Aged Brie|51", "Thing|50", "Backstage passes to a TAFKAL80ETC concert|51"])
    // TODO: This test may be redundant once everything else is tested
    fun `Given input has a quality greater than 50 then return some value`(itemName: String, itemQuality: Int) {
      val items = arrayOf(Item(itemName, 10, 51))
      val app = GildedRose(items)
      app.updateQuality()
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item(itemName, sellIn = 9, quality = itemQuality))
    }
  }
}


