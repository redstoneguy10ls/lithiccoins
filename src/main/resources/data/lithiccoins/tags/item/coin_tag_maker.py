
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


def write_tag (metal : str, user : str) :

    tag_file = open ("coins/" + metal + ".json", "wt")

    tag_file.write ("{\n")
    tag_file.write ("   \"replace\": false,\n")
    tag_file.write ("   \"values\": [\n")

    # We write all but the last stamp_type automatically, as the last entry in the tag should not end with a ','
    for i in range (len (stamp_types) - 1) :

        tag_file.write ("       \"lithiccoins:coin/" + stamp_types[i] + "/" + metal + "\",\n")

    tag_file.write ("       \"lithiccoins:coin/" + stamp_types[-1] + "/" + metal + "\"\n")
    
    tag_file.write ("   ]\n")
    tag_file.write ("}")

    return


def write_all (user : str) :

    # The program automatically writes tag files for all metals
    for metal in metals :

        write_tag (metal, user)
    
    return



def __main__ () :

    # Replace this with your username
    user = "JotredDev"
    write_all (user)


if __name__ == "__main__" :
    __main__ ()
