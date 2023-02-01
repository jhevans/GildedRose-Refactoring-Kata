package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

private const val SULFURAS = "Sulfuras, Hand of Ragnaros"

private const val AGED_BRIE = "Aged Brie"

private const val BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert"

private const val STANDARD_ITEM = "Thing"

internal class GildedRoseTest {
  @Test
  fun `Given an empty array, then empty array remains unchanged`() {
    val items = arrayOf<Item>()
    val app = GildedRose(items)
    app.updateQuality()
    assertThat(app.items.size).isEqualTo(0)
  }

  @Test
  fun `Given an array of multiple items, then return correct values for each item`() {
    val originalItems = arrayOf(
      Item(SULFURAS, sellIn = 5, 25),
      Item(AGED_BRIE, sellIn = 5, 25),
      Item(STANDARD_ITEM, sellIn = 5, 25),
      Item(BACKSTAGE_PASSES, sellIn = 5, 25)
    )

    val expectedItems = arrayOf(
      Item(SULFURAS, sellIn = 5, 25),
      Item(AGED_BRIE, sellIn = 4, 26),
      Item(STANDARD_ITEM, sellIn = 4, 24),
      Item(BACKSTAGE_PASSES, sellIn = 4, 28)
    )

    val app = GildedRose(originalItems)
    app.updateQuality()
    val actualItems = app.items

    assertThat(actualItems.size)
      .isEqualTo(originalItems.size)
      .isEqualTo(expectedItems.size)

    actualItems.zip(expectedItems).forEach {
      assertThat(it.first).usingRecursiveComparison().isEqualTo(it.second)
    }
  }

  @Nested
  @DisplayName(value = "Given a standard item")
  inner class StandardItemTest {
    // TODO: This doesn't cover a large range of item names, possibly missed a trick testing this way, may be worth refactoring
    // Might be better to refactor these tests to be behaviour-centric rather than item-centric 🤔

    @Test
    fun `Then sellIn and quality are reduced by one`() {
      val app = singleUpdatedItemOf(STANDARD_ITEM, 10, 20)
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item(STANDARD_ITEM, sellIn = 9, quality = 19))
    }

    @ParameterizedTest(name = "quality of {0} is reduced by 2")
    @ValueSource(ints = [0, -1, -999, Int.MIN_VALUE + 1])
    fun `And a sellIn of 0 or less, then quality is reduced by 2`(startingSellIn: Int) {
      val app = singleUpdatedItemOf(STANDARD_ITEM, startingSellIn, 20)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(STANDARD_ITEM, sellIn = startingSellIn - 1, quality = 18))
    }

    @ParameterizedTest(name = "quality of {0} is reduced to 0")
    @ValueSource(ints = [0, 1, 2])
    fun `And a sellIn of 0 and a quality of less than or equal to 2, then quality is reduced to 0`(
      startingQuality: Int
    ) {
      val app = singleUpdatedItemOf(STANDARD_ITEM, 0, startingQuality)
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item(STANDARD_ITEM, sellIn = -1, quality = 0))
    }

    @ParameterizedTest(name = "quality of {0} is reduced to {1}")
    @CsvSource("3,1", "999,997", "2147483647,2147483645")
    fun `And a sellIn of 0 and a quality greater than 2, then quality is reduced by 2`(
      startingQuality: Int,
      updatedQuality: Int
    ) {
      val app = singleUpdatedItemOf(STANDARD_ITEM, 0, 1)
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item(STANDARD_ITEM, sellIn = -1, quality = 0))
    }
  }

  @Nested
  @DisplayName(value = "Given an aged Brie")
  inner class AgedBrieTest {
    @ParameterizedTest(name = "quality of {0} is increased by 1")
    @ValueSource(ints = [0, 1, 5, 49])
    fun `Then sellIn is reduced by one, quality increases by one`(startingQuality: Int) {
      val app = singleUpdatedItemOf(AGED_BRIE, 10, startingQuality)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(AGED_BRIE, sellIn = 9, quality = startingQuality + 1))
    }

    @Test
    fun `And a quality of 50, then quality stays at 50`() {
      val app = singleUpdatedItemOf(AGED_BRIE, 10, 50)
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item(AGED_BRIE, sellIn = 9, quality = 50))
    }
  }

  @Nested
  @DisplayName(value = "Given Backstage passes to a TAFKAL80ETC concert")
  inner class BackstagePassesTest {
    @ParameterizedTest(name = "quality of {0} is increased by 1")
    @ValueSource(ints = [Int.MAX_VALUE, 99, 11])
    fun `And sellIn is greater than 10, Then sellIn is reduced by one, quality increases by 1`(sellIn: Int) {
      val app = singleUpdatedItemOf(BACKSTAGE_PASSES, sellIn, 10)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(BACKSTAGE_PASSES, sellIn = sellIn - 1, quality = 11))
    }

    @ParameterizedTest(name = "at sellIn of {0} quality is increased by 2")
    @ValueSource(ints = [10, 9, 8, 7, 6])
    fun `And sellIn is between 10 and 6, Then sellIn is reduced by one, quality increases by 2`(sellIn: Int) {
      val app = singleUpdatedItemOf(BACKSTAGE_PASSES, sellIn, 10)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(BACKSTAGE_PASSES, sellIn = sellIn - 1, quality = 12))
    }

    @ParameterizedTest(name = "at sellIn of {0} quality is increased by 3")
    @ValueSource(ints = [5, 4, 3, 2, 1])
    fun `And sellIn is between 5 and 1, Then sellIn is reduced by one, quality increases by 3`(sellIn: Int) {
      val app = singleUpdatedItemOf(BACKSTAGE_PASSES, sellIn, 10)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(BACKSTAGE_PASSES, sellIn = sellIn - 1, quality = 13))
    }

    @ParameterizedTest(name = "at sellIn of {0} quality is 0")
    @ValueSource(ints = [0, -1, -2, -99, Int.MIN_VALUE + 1])
    fun `And sellIn is less than 0, Then sellIn is reduced by one, quality is zero`(sellIn: Int) {
      val app = singleUpdatedItemOf(BACKSTAGE_PASSES, sellIn, 50)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(BACKSTAGE_PASSES, sellIn = sellIn - 1, quality = 0))
    }

    @Test
    fun `And a quality of 50, then quality stays at 50`() {
      val app = singleUpdatedItemOf(BACKSTAGE_PASSES, 10, 50)
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item(BACKSTAGE_PASSES, sellIn = 9, quality = 50))
    }
  }

  @Nested
  @DisplayName(value = "Given Sulfuras, Hand of Ragnaros")
  inner class SulfurasTest {

    @ParameterizedTest(name = "quality of {0} and sellIn of {0} both remain at {0}")
    @ValueSource(ints = [Int.MIN_VALUE, -1, 0, 1, 5, 49, Int.MAX_VALUE])
    fun `Then quality and sellIn do not change`(qualityOrSellin: Int) {
      val itemName = SULFURAS
      val app = singleUpdatedItemOf(itemName, qualityOrSellin, qualityOrSellin)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(itemName, sellIn = qualityOrSellin, quality = qualityOrSellin))
    }
  }

  @Nested
  inner class UnspecifiedBehaviourTest {
    @Test
    fun `Given input has a negative value for quality then return the value unchanged`() {
      val app = singleUpdatedItemOf(STANDARD_ITEM, 0, -1)
      assertThat(app.items[0]).usingRecursiveComparison().isEqualTo(Item(STANDARD_ITEM, sellIn = -1, quality = -1))
    }

    @ParameterizedTest
    @DisplayName(value = "Given {0} then quality is 51")
    @CsvSource(delimiter = '|', value = ["Aged Brie|51", "Thing|50", "Backstage passes to a TAFKAL80ETC concert|51"])
    fun `Given input has a quality greater than 50 then return some value`(itemName: String, expectedItemQuality: Int) {
      val app = singleUpdatedItemOf(itemName, 10, 51)
      assertThat(app.items[0]).usingRecursiveComparison()
        .isEqualTo(Item(itemName, sellIn = 9, quality = expectedItemQuality))
    }
  }

  private fun singleUpdatedItemOf(
    itemName: String,
    sellIn: Int,
    quality: Int
  ): GildedRose {
    val items = arrayOf(Item(itemName, sellIn, quality))
    val app = GildedRose(items)
    app.updateQuality()
    return app
  }
}


