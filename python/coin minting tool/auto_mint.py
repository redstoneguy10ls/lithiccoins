
from PIL import Image
import numpy as np
import os

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

# Defining the stamp types
stamp_types = [
    "angler",
    "archer",
    "arms_up",
    "bee",
    "blade",
    "brewer",
    "burn",
    "bust",
    "danger",
    "eagle",
    "explorer",
    "face",
    "friend",
    "heart",
    "heartbreak",
    "howl",
    "miner",
    "mourner",
    "plenty",
    "prize",
    "punched",
    "sheaf",
    "shelter",
    "skull",
    "symbol",
    "trifoil"
]




# Defining the colors associated with the 'depth' of the mint, aka the shades of color used in the imprint of the stamp
def get_depth_colors () :

    depth_colors = {}

    for metal in metals :

        colors = Image.open("depth_colors/" + metal + ".png")
        depth_colors[metal] = np.array(colors)[0]
    
    return depth_colors


# Applying the depth mask to the base coins
def apply_depth (depth_colors : dict) :

    for metal in metals :

        for stamp_type in stamp_types :

            if not os.path.exists("results/" + stamp_type + "/") :
                os.makedirs("results/" + stamp_type + "/")

            mask = np.array(Image.open("depth_masks/" + stamp_type + ".png"))
            coin_base = np.array(Image.open("blank_coins/" + metal + ".png"))

            # Iterating through every pixel of the blank coin (row, column), changing the color based on the given depth mask
            for i in range(16) :

                for j in range (16) :

                    depth_level = mask[i, j, 0]

                    match depth_level :

                        case 32: # darkest shade
                            coin_base[i, j] = depth_colors[metal][0]
                            
                        case 64: # medium shade
                            coin_base[i, j] = depth_colors[metal][1]

                        case 96: # lightest shade
                            coin_base[i, j] = depth_colors[metal][2]
                            
                        case 128: # empty
                            coin_base[i, j] = np.array([255, 255, 255, 0])

            # Once the coin is iterated, save the resulting image as a .png again

            coin = Image.fromarray(coin_base)
            coin.save("results/" + stamp_type + "/" + metal + ".png")

def __main__ () :

    depth_colors = get_depth_colors()
    apply_depth(depth_colors)


if __name__ == "__main__" :
    __main__ ()

