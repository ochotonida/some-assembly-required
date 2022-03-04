# Some Assembly Required [![CurseForge](http://cf.way2muchnoise.eu/full_422951_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/some-assembly-required) [![](https://discordapp.com/api/guilds/298798089068609537/widget.png?style=shield)](https://discord.gg/87pXJadaRr)
Some Assembly Required is a Farmer's Delight addon loosely based on the fabric mod [Sandwichable](https://github.com/FoundationGames/Sandwichable). 
More information can be found here:
https://www.curseforge.com/minecraft/mc-mods/some-assembly-required

## Customizing Items on Sandwiches
In version 2.0.0 and above, it is possible to customize the behavior and appearance of items on sandwiches through data packs.
Ingredient JSONs are placed in the `data/<namespace>/some_assembly_required/ingredients` folder.
Each JSON file corresponds to a single item. Items with a corresponding ingredient JSON can be added onto a sandwich even if the item is normally not edible.
The following fields can be customized (All fields are optional except `item`):

* `item`: (*required*) The corresponding item id for this ingredient. (The file name does not need to correspond to the item name. However, if there are multiple ingredient JSONs for a single item, only one will be loaded)
* `food`: A json object, replaces the item's food stats when it is on a sandwich.
    * `nutrition`: (*required*) The amount of hunger the item heals.
    * `saturationModifier`: (*required*) The amount of saturation the item restores per point of hunger healed.
    * `canAlwaysEat`: (*default = false*) When true, sandwiches containing this item can be always eaten.
* `displayName`: The name of the item as it should appear in the name of the sandwich. This is a text component, information on how to format these can be found [here](https://minecraft.fandom.com/wiki/Raw_JSON_text_format). (You can also use a string)
* `fullName`: The name of the item as it should appear in the tooltip of the sandwich. (If the display name is omitted, and the full name is set, the full name is also used as the display name)
* `displayItem`: A json object describing an item stack, overrides which item is rendered when this item is on a sandwich
    * `item`: (*required*) The item ID of the item to render instead.
    * `count`: The size of the item stack.
    * `nbt`: The NBT of the item stack, either as a JSON object or stringified NBT.
* `container`: A json object describing an item stack (see `displayItem`). If an item has a container, 
  the container item will be returned to the player when the item is put on a sandwich. 
  When an item with a container is removed from a sandwich, the item is voided instead of returned.
* `soundEvent`: A sound event id. Changes the sound this item makes when added or removed from a sandwich.

For some example ingredients, see the [default data pack](https://github.com/ochotonida/some-assembly-required/tree/1.18/src/generated/resources/data/some_assembly_required/some_assembly_required/ingredients).