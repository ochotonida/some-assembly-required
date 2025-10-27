# Some Assembly Required [![CurseForge](https://img.shields.io/curseforge/dt/422951?label=CurseForge&color=F16436&style=flat&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/some-assembly-required) [![Modrinth](https://img.shields.io/modrinth/dt/some-assembly-required?color=00AF5C&label=Modrinth&style=flat)](https://modrinth.com/mod/some-assembly-required) [![](https://discordapp.com/api/guilds/298798089068609537/widget.png?style=shield)](https://discord.gg/87pXJadaRr)
Some Assembly Required is a Farmer's Delight and Create addon that allows players to create custom sandwiches. 
More information can be found on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/some-assembly-required) or [Modrinth](https://modrinth.com/mod/some-assembly-required).

## Customizing Items on Sandwiches
The behavior and appearance of items on sandwiches can be customized through data packs.
⚠️ Note that the data pack format for this can change between versions,
so make sure you are viewing this README on the GitHub branch for your Minecraft version.


Ingredient JSONs are placed in the `data/<namespace>/some_assembly_required/ingredients` folder.
Each JSON file corresponds to a single item. Items with a corresponding ingredient JSON can be added onto a sandwich even if the item is normally inedible.
The following fields can be customized (all fields are optional except `item`):

* `item`: (*required*) The corresponding item id for this ingredient. (The file name does not need to correspond to the item name. However, if there are multiple ingredient JSONs for a single item, only one will be loaded)
* `food`: A json object, replaces the item's food stats when it is on a sandwich. The format for this is the same as the [`food` data component](https://minecraft.wiki/w/Data_component_format#food).
  * `nutrition`: (*required*) The number of food points restored by this item when eaten as part of a sandwich.
  * `saturation`: (*required*) The amount of saturation restored by this item when eaten as part of a sandwich.
  * `using_converts_to`: The item returned to the player when this food is added onto a sandwich, typically a bowl, bottle or bucket.
    Items with this property are voided rather than returned when removed from a sandwich on a sandwiching station.
  * `effects`: A list of possible effects that this food can apply when eaten as part of a sandwich. Each entry uses the following format:
    * `probability`: (*default = 1*) The probability that the effect is applied when this food is eaten.
    * `effect`: (*required*) A single custom effect. The format here is the same as for effects in other data components.
      * `id`: The ID of the effect.
      * `amplifier`: (*default = 0*) The amplifier of the effect, with level I having value 0.
      * `duration`: (*default = 1*) The duration of the effect in ticks.
      * `ambient`: (*default = false*) Whether this is an effect provided by a beacon or conduit and therefore should be less intrusive on the screen.
      * `show_particles`: (*default = true*) Whether this effect produces particles.
      * `show_icon`: (*default = true*) Whether an icon should be shown for this effect.
* `display_name`: The name of the item as it should appear in the name of the sandwich. This is a text component, information on how to format these can be found [here](https://minecraft.wiki/w/Text_component_format). (You can also use a string)
* `full_name`: The name of the item as it should appear in the tooltip of the sandwich. (If the display name is omitted, and the full name is set, the full name is also used as the display name)
* `display_item`: A json object describing an item stack, overrides which item is rendered when this item is on a sandwich
    * `id`: (*required*) The item ID of the item to render instead.
    * `count`: The size of the item stack.
    * `components`: A map of components to apply to the item.
* `render_as_item`: (*default = true*) Set this to `false` when the display item has a 3D model. Flat item models are rotated and translated by default.
* `height`: (*default = 1*) The height of the model of this item, in pixels. (Determines the size of the gap between the previous item on the sandwich and the next)
* `sound`: A sound event id. Changes the sound this item makes when added or removed from a sandwich.

Examples: (Comments should be removed in your own data files)
```json5
{
  "item": "create:chocolate_bucket",
  // Chocolate buckets aren't drinkable by default, so we need to define its food stats.
  // We also set the 'using_converts_to' property. For edible items, this usually isn't needed.
  "food": {
    "nutrition": 6,
    "saturation": 3.6,
    "using_converts_to": {
      "id": "minecraft:bucket"
    }
  },
  "display_item": {
    // We want chocolate buckets to display as a liquid instead of a bucket,
    // so we set the display item to Some Assembly Required's 'spread' item.
    // The color of this item can be changed using the 'spread_color' item component.
    "id": "someassemblyrequired:spread",
    "components": {
      // Accepts an RRGGBB hexadecimal color value.
      // AARRGGBB is also allowed for translucent spreads.
      "someassemblyrequired:spread_color": "#AD513C"
    },
    "count": 1
  },
  // We don't want it to be called a 'Chocolate Bucket' in the tooltip of sandwiches,
  // so we set the 'full_name' property to 'Chocolate'
  // The 'display_name' can be used if you want to keep the full item name in the tooltip
  "full_name": {
    // Translates to 'Chocolate'
    "translate": "someassemblyrequired.ingredient.create.chocolate_bucket"
  },
  // Use the 'moist' application sound.
  // Other sounds used by SAR are 'wet', 'slimy' and 'leafy'.
  "sound": "someassemblyrequired:block.sandwich.add_item.moist"
}
```
```json5
{
  // An example of an item with a custom model
  "item": "farmersdelight:beef_patty",
  "display_item": {
    // Some Assembly Required adds all of its custom models to the spread item's model
    // as custom model data. You can either use custom model data yourself, or use kubeJS
    // to add a new item with your 3D model.
    "id": "someassemblyrequired:spread",
    "components": {
      "minecraft:custom_model_data": 14
    },
    "count": 1
  },
  "display_name": {
    // Translates to 'Beef'
    "translate": "someassemblyrequired.ingredient.farmersdelight.beef_patty"
  },
  // The beef patty is twice as tall as a regular item
  "height": 2,
  // We use a custom model that doesn't need to be rotated or translated.
  "render_as_item": false
}
```
The spread item model can be found [here](https://github.com/ochotonida/some-assembly-required/blob/1.21.1/src/generated/resources/assets/someassemblyrequired/models/item/spread.json),
the beef patty 3D model can be found [here](https://github.com/ochotonida/some-assembly-required/blob/1.21.1/src/generated/resources/assets/someassemblyrequired/models/ingredient/farmersdelight/beef_patty.json).

For more example ingredients, see the [default data pack](https://github.com/ochotonida/some-assembly-required/tree/1.21.1/src/generated/resources/data/someassemblyrequired/someassemblyrequired/ingredients).


## Custom Spouting Recipes

To allow fluids to be spouted onto sandwiches by Create's spouts, the `someassemblyrequired:sandwich_spouting` recipe type can be used.
Recipes of this type have the following properties:

* `fluid`: The fluid to be spouted
    * `type`: The type of fluid ingredient. Should be set to `"fluid_stack"` to match a fluid based on its ID. Can be omitted starting from version 5.2.5.
    * `amount`: The amount of fluid that should be consumed, in millibuckets.
    * `fluid`: The ID of the fluid that should be spouted.
* `result`: The result of the recipe
    * `id` The ID of the item that should be deposited on the sandwich. The item will need to have an ingredient JSON associated with it in order for it to be rendered as a spread.

Example:
```json5
{
  "type": "someassemblyrequired:sandwich_spouting",
  "fluid": {
    "type": "fluid_stack",
    "amount": 250,
    "fluid": "create:chocolate"
  },
  "result": {
    // This will use the chocolate bucket ingredient we defined earlier
    "id": "create:chocolate_bucket"
  }
}
```



For more examples, see the [default data pack](https://github.com/ochotonida/some-assembly-required/tree/1.21.1/src/generated/resources/data/someassemblyrequired/recipe/sandwich_spouting).
