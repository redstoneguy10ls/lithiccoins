
from PIL import Image
import numpy as np

# Defining the metals of coins

metals = [
    "aluminum",
    "bismuth",
    "bismuth_bronze",
    "black_bronze",
    "black_steel",
    "blue_steel",
    "brass",
    "bronze",
    "cast_iron",
    "chromium",
    "constantan",
    "copper",
    "electrum",
    "gold",
    "lead",
    "nickel",
    "red_steel",
    "rose_gold",
    "silver",
    "stainless_steel",
    "steel",
    "sterling_silver",
    "tin",
    "uranium",
    "wrought_iron",
    "zinc"
]

# Defining the colors associated with the 'depth' of the mint, aka the shades of color used in the imprint of the stamp
# For simplicity we take the angler coins as a base to extract the colors from

def get_color_depths () :

    color_depths = {}

    for metal in metals :

        coin_base = Image.open("angler/" + metal + ".png")
        np_coin_base = np.array(coin_base)

        # The three pixels (5, 10), (5, 9), (6, 9) hold the three colors we need, in the order 'dark, medium, light'
        color_depths[metal] = [np_coin_base[5, 10], np_coin_base[5, 9], np_coin_base[6, 9]]
    
    return color_depths



# Applying the depth mask to the base coins

def apply_depth (depth_mask_name : str, color_depths : dict) :

    mask  = Image.open(depth_mask_name)
    np_mask = np.array(mask)

    for metal in metals :

        coin_base = Image.open("blank_coins/" + metal + ".png")
        np_coin_base = np.array(coin_base)

        # Iterating through every pixel of the blank coin (row, column), changing the color based on the given depth mask
        for i in range(16) :

            for j in range (16) :

                depth_level = np_mask[i, j, 0]

                match depth_level :

                    case 32: # darkest shade
                        np_coin_base[i, j] = (color_depths[metal])[0]
                        
                    case 64: # medium shade
                        np_coin_base[i, j] = (color_depths[metal])[1]

                    case 96: # lightest shade
                        np_coin_base[i, j] = (color_depths[metal])[2]

        # Once the coin is iterated, save the resulting image as a .png again

        coin=Image.fromarray(np_coin_base)
        coin.save("results/" + metal + ".png")



def __main__ () :

    # Replace this with the name of your depth mask
    depth_mask_name = "coin_bee_depth.png"

    color_depths = get_color_depths()

    apply_depth(depth_mask_name, color_depths)


if __name__ == "__main__" :
    __main__ ()

