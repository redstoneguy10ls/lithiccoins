import json
import os
# list = [
    # "Angler",
    # "Archer",
    # "Arms_up",
    # "Blade",
    # "Brewer",
    # "Burn",
    # "Danger",
    # "Eagle",
    # "Explorer",
    # "Friend",
    # "Heart",
    # "Heartbreak",
    # "Howl",
    # "Miner",
    # "Mourner",
    # "Plenty",
    # "Prize",
    # "Sheaf",
    # "Shelter",
    # "Skull",
    # "Trifoil"
# ]

# for items in list:
# 
    # print('"item.lithiccoins.coin.'+items.lower()+'.aluminum" :  "Aluminum '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.bismuth" : "Bismuth '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.bismuth_bronze" : "Bismuth Bronze '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.black_bronze" : "Black Bronze '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.black_steel" :  "Black Steel '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.blue_steel" : "Blue Steel '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.brass" :  "Brass '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.bronze" : "Bronze '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.cast_iron" :  "Cast Iron '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.chromium" : "Chromium '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.constantan" : "Constantan '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.copper" : "Copper '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.electrum" : "Electrum '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.gold" : "Gold '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.lead" :  "Lead '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.nickel" : "Nickel '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.red_steel" :  "Red Steel '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.rose_gold" : "Rose Gold '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.silver" :  "Silver '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.stainless_steel" : "Stainless Steel '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.steel" :  "Steel '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.sterling_silver" : "Sterling Silver '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.tin" :  "Tin '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.uranium" : "Uranium '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.wrought_iron" : "Wrought Iron '+items+' Coin",')
    # print('"item.lithiccoins.coin.'+items.lower()+'.zinc" : "Zinc '+items+' Coin",')
# 
# input()


specheats =[]

def find_heat_capacity():
    test = os.listdir('New Folder/')

    for stuff in test:
        f= open('New Folder/'+stuff)
        data = json.load(f)
        specheats.append(data['heat_capacity'])

temps = [
    650,
    270,
    985,
    1070,
    1485,
    1540,
    930,
    950,
    1535,
    1250,
    750,
    1080,
    900,
    1060,
    500,
    1453,
    1540,
    960,
    961,
    1540,
    1540,
    950,
    230,
    1250,
    420
]

metal = [
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
    "zinc"
]

metalfluid = [
    "tfc_ie_addon:metal/aluminum",
    "tfc:metal/bismuth",
    "tfc:metal/bismuth_bronze",
    "tfc:metal/black_bronze",
    "tfc:metal/black_steel",
    "tfc:metal/blue_steel",
    "tfc:metal/brass",
    "tfc:metal/bronze",
    "tfc:metal/cast_iron",
    "firmalife:metal/chromium",
    "tfc_ie_addon:metal/constantan",
    "tfc:metal/copper",
    "tfc_ie_addon:metal/electrum",
    "tfc:metal/gold",
    "tfc_ie_addon:metal/lead",
    "tfc:metal/nickel",
    "tfc:metal/red_steel",
    "tfc:metal/rose_gold",
    "tfc:metal/silver",
    "firmalife:metal/stainless_steel",
    "tfc:metal/steel",
    "tfc:metal/sterling_silver",
    "tfc:metal/tin",
    "tfc_ie_addon:metal/uranium",
    "tfc:metal/zinc"
]
def molds():
    for filename in metal:

        f = open(filename+".json","w")
        x = metal.index(filename)

        f.writelines("""

    {
        "__comment__": "Recipe written by redstoneguy10ls",
        "type": "tfc:casting",
        "mold": {
            "item": "lithiccoins:ceramic/coin_mold"
        },
        "fluid": {
            "ingredient": \""""+metalfluid[x]+"""\",
            "amount": 100
        },
        "result": {
            "item": "lithiccoins:blank_coin/"""+filename+"""\",
            "count": 4
        },
        "break_chance": 0.01
    }             
                     """)
        f.close



def melt():
    for filename in metal:

        f = open(filename+".json","w")
        x = metal.index(filename)
        f.writelines("""{
    "__comment__": "Recipe written by redstoneguy10ls",
    "type": "tfc:heating",
    "ingredient": {
        "item": "lithiccoins:blank_coin/"""+filename+"""\"
    },
    "result_fluid": {
        "fluid": \""""+metalfluid[x]+"""\",
        "amount": 20
    },
    "temperature": """+str(temps[x])+"""
}             """)
        f.close

melt()

# find_heat_capacity()

def heat_capacity():


    for filename in metal:

        f = open(filename+".json","w")
        x = metal.index(filename)
        f.writelines("""{
    "__comment__": "Recipe written by redstoneguy10ls",
    "ingredient": {
        "item": "lithiccoins:blank_coin/"""+filename+"""\"
    },
    "heat_capacity": """+str(specheats[x])+"""
}             """)
        f.close

# heat_capacity()