# Readme

General strategy for tackling this problem:
- [ ] Test existing code 
- [ ] Do any 'easy win' refactoring
- [ ] Work out next steps for making changes

Test cases:

- [x] Once the sell by date has passed, Quality degrades twice as fast
- [x] The Quality of an item is never negative
- [x] "Aged Brie" actually increases in Quality the older it gets
- [x] The Quality of an item is never more than 50
- [x] "Sulfuras", being a legendary item, never has to be sold or decreases in Quality
- [ ] The Quality of Backstage passes is never more than 50
- [ ] "Backstage passes", like aged brie, increases in Quality as its SellIn value approaches;
- [ ] Quality increases by 2 when there are 10 days or less and by 3 when there are 5 days or less but
- [ ] Quality drops to 0 after the concert
- [ ] Test that it works for multiple items
