package com.redstoneguy10ls.lithiccoins.util;

import com.redstoneguy10ls.lithiccoins.common.items.LCItems;
import com.redstoneguy10ls.lithiccoins.common.items.TopDies;
import com.redstoneguy10ls.lithiccoins.common.items.coinMaterial;
import com.redstoneguy10ls.lithiccoins.common.items.stampTypes;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.redstoneguy10ls.lithiccoins.LithicCoins.MOD_ID;

public class LCHelpers {
    
    public static ResourceLocation identifier(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static stampTypes getStamptype(Item item)
    {
        String x = item.getName(new ItemStack(item)).toString();
        stampTypes temp = null;
        for(stampTypes stamps : stampTypes.VALUES)
        {
            String str1 = stamps.name();
            if(!(x.toUpperCase().contains(str1.toUpperCase())) )
            {
                continue;
            }
            else
            {
                if (temp != null && temp.name().toUpperCase().contains(str1.toUpperCase()))
                {
                    continue;
                }
                else
                {
                    temp = stamps;
                }
            }
        }
        return temp;
    }

    public static int getStampTypesInInt(ItemStack stack) {

        int count = 1;
        for (TopDies stamps : TopDies.VALUES) {

            if (!Helpers.isItem(stack, LCItems.TOP_DIE.get(stamps).get(getMetalFromInt(getMetalInInt(stack.getItem().asItem()))).get()) )
            {
                count++;
            }
            else
            {
                return count;
            }
        }
        return 0;
    }


    public static Metal.Default getMetalFromInt(int number)
    {
        switch(number)
        {
            case 1:
                return Metal.Default.COPPER;
            case 2:
                return Metal.Default.BRONZE;
            case 3:
                return Metal.Default.BISMUTH_BRONZE;
            case 4:
                return Metal.Default.BLACK_BRONZE;
            case 5:
                return Metal.Default.WROUGHT_IRON;
            case 6:
                return Metal.Default.STEEL;
            case 7:
                return Metal.Default.BLACK_STEEL;
            case 8:
                return Metal.Default.RED_STEEL;
            case 9:
                return Metal.Default.BLUE_STEEL;
            default:
                return Metal.Default.COPPER;
        }
    }

    public static int getTier(Item item)
    {
        switch(item.getMaxDamage())
        {
            case 600:
                return 1;
            case 1300:
               return 2;
            case 1200:
                return 2;
            case 1460:
                return 2;
            case 2200:
                return 3;
            case 3300:
                return 4;
            case 4200:
                return 5;
            case 6500:
                return 6;
            default:
                return 0;
        }

    }

    public static int getMetalInInt(Item item)
    {
        switch(item.getMaxDamage())
        {
            case 600:
                return 1;
            case 1300:
                return 2;
            case 1200:
                return 3;
            case 1460:
                return 4;
            case 2200:
                return 5;
            case 3300:
                return 6;
            case 4200:
                return 7;
            case 6500:
                if(item.getName(new ItemStack(item)).toString().contains("red"))
                {
                    return 8;
                }
                else
                {
                    return 9;
                }
            default:
                return 0;
        }
    }

    public static int getCoinInInt(ItemStack stack)
    {
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.ALUMINUM).orElse(null))){return 1;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.BISMUTH).orElse(null))){return 2;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.BISMUTH_BRONZE).orElse(null))){return 3;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.BLACK_BRONZE).orElse(null))){return 4;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.BLACK_STEEL).orElse(null))){return 5;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.BLUE_STEEL).orElse(null))){return 6;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.BRASS).orElse(null))){return 7;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.BRONZE).orElse(null))){return 8;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.CAST_IRON).orElse(null))){return 9;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.CHROMIUM).orElse(null))){return 10;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.CONSTANTAN).orElse(null))){return 11;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.COPPER).orElse(null))){return 12;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.ELECTRUM).orElse(null))){return 13;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.GOLD).orElse(null))){return 14;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.LEAD).orElse(null))){return 15;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.NICKEL).orElse(null))){return 16;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.RED_STEEL).orElse(null))){return 17;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.ROSE_GOLD).orElse(null))){return 18;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.SILVER).orElse(null))){return 19;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.STAINLESS_STEEL).orElse(null))){return 20;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.STEEL).orElse(null))){return 21;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.STERLING_SILVER).orElse(null))){return 22;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.TIN).orElse(null))){return 23;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.URANIUM).orElse(null))){return 24;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.WROUGHT_IRON).orElse(null))){return 25;}
        if(stack.is(LCItems.BLANK_COINS.get(coinMaterial.ZINC).orElse(null))){return 26;}
        return 0;
    }




    /*
    public static ItemStack getDisplayCoin(int numb)
    {
        switch(numb){
            default -> {return new ItemStack(LCItems.BLANK_COINS.get(coinMaterial.ALUMINUM));}
        }
    }

     */

}
