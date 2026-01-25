This is a tool for automatically creating the textures for minted coins. To use it, follow these instructions:

Case 1: Making textures for a new stamp type

1. Create a depth mask (16x16) for your design, save it in the /depth_masks/ folder. Up to 4 colors can be used:
   - #202020 (dark shade)
   - #404040 (medium shade)
   - #606060 (light shade)
   - #808080 (see-through)
   any other color will not do anything

2. Open auto_mint.py
3. Add your stamp type to the stamp types array (if your stamp type is called "example", your depth mask image file should be named "example.png")
4. Run auto_mint.py
5. Take the resulting coin textures from /results/


Case 2: Making textures for a new metal type

1. Create a blank coin texture image file (16x16), save it in the /blank_coins/ folder
2. Create the associated depth color image (1x3), going dark->medium->light (left->right), save it in the /depth_colors/ folder
3. Open auto_mint.py
3. Add your metal to the metals array (if your metal is called "example", your coin and depth color image files should both be named "example.png")
5. Run auto_mint.py
6. Take the resulting coin textures from /results/
