# Some Assembly Required [![CurseForge](https://img.shields.io/curseforge/dt/422951?label=CurseForge&color=F16436&style=flat&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/some-assembly-required) [![Modrinth](https://img.shields.io/modrinth/dt/some-assembly-required?color=00AF5C&label=Modrinth&style=flat)](https://modrinth.com/mod/some-assembly-required) [![](https://discordapp.com/api/guilds/298798089068609537/widget.png?style=shield)](https://discord.gg/87pXJadaRr)
Some Assembly Required is a Farmer's Delight and Create addon that allows players to create custom sandwiches. 
More information can be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/some-assembly-required) or [Modrinth](https://modrinth.com/mod/some-assembly-required).

## Customizing Items on Sandwiches
The behavior and appearance of items on sandwiches can be customized through data packs. Note that the data pack format for this can change between versions,
so make sure you are viewing this README on the GitHub branch for your Minecraft version.


Ingredient JSONs are placed in the `data/<namespace>/some_assembly_required/ingredients` folder.
Each JSON file corresponds to a single item. Items with a corresponding ingredient JSON can be added onto a sandwich even if the item is normally inedible.
The following fields can be customized (all fields are optional except `item`):

* `item`: (*required*) The corresponding item id for this ingredient. (The file name does not need to correspond to the item name. However, if there are multiple ingredient JSONs for a single item, only one will be loaded)
* `food`: A json object, replaces the item's food stats when it is on a sandwich. The format for this is the same as the [`food` data component](https://minecraft.wiki/w/Data_component_format#food).
* `display_name`: The name of the item as it should appear in the name of the sandwich. This is a text component, information on how to format these can be found [here](https://minecraft.wiki/w/Text_component_format). (You can also use a string)
* `full_name`: The name of the item as it should appear in the tooltip of the sandwich. (If the display name is omitted, and the full name is set, the full name is also used as the display name)
* `display_item`: A json object describing an item stack, overrides which item is rendered when this item is on a sandwich
    * `id`: (*required*) The item ID of the item to render instead.
    * `count`: The size of the item stack.
    * `components`: A map of components to apply to the item.
* `render_as_item`: (*default = true*) Set this to `false` when the display item has a 3D model. Flat item models are rotated and translated by default.
* `height`: (*default = 1*) The height of the model of this item, in pixels. (Determines the size of the gap between the previous item on the sandwich and the next)
* `sound`: A sound event id. Changes the sound this item makes when added or removed from a sandwich.

For some example ingredients, see the [default data pack](https://github.com/ochotonida/some-assembly-required/tree/1.21.1/src/generated/resources/data/some_assembly_required/some_assembly_required/ingredients).