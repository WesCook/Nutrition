## Changelog

[v3.4.0](https://github.com/WesCook/Nutrition/releases/tag/v3.4.0) - 2018-05-20
* Added client-side prediction to significantly reduce network overhead
* Updated Harvestcraft support

[v3.3.0](https://github.com/WesCook/Nutrition/releases/tag/v3.3.0) - 2018-05-06
* Added support for Exotic Birds, Thaumcraft, and Zoo & Wild Animals Rebuilt (contribution from Sunconure11)
* Added support for XL Food Mod (contribution from ellor1138)
* Ported support for Aquaculture (contribution from Sunconure11)

[v3.2.0](https://github.com/WesCook/Nutrition/releases/tag/v3.2.0) - 2018-04-19
* Major overhaul of chat commands.  Introduced tab completion, player selectors, and add/subtract/reset features.
* Added support for Galacticraft (contribution from Broam)

[v3.1.0](https://github.com/WesCook/Nutrition/releases/tag/v3.1.0) - 2018-03-26
* Drinking milk will no longer clear potion effects from Nutrition
* Updated VanillaFoodPantry support
* Ported Animalium support, and updated support for Familiar Fauna (contribution from Sunconure11)
* Added Tropicraft and Inspirations support (contribution from Sunconure11)

[v3.0.0](https://github.com/WesCook/Nutrition/releases/tag/v3.0.0) - 2018-03-11
* Made a breaking change to the custom mal/nourished effects, so each amplifier level grants half a heart instead of a full heart.  This is to allow more granularity in custom effects.
* Added support for Familiar Fauna (contribution from Sunconure11)

[v2.9.0](https://github.com/WesCook/Nutrition/releases/tag/v2.9.0) - 2018-02-25
* Added support for Heat and Climate (contribution from MaskedBird)
* Added support for Erebus (contribution from Sunconure11)
* Updated support for Plants and Betweenlands (contribution from Sunconure11)

[v2.8.0](https://github.com/WesCook/Nutrition/releases/tag/v2.8.0) - 2018-02-17
* Added support for Lycanites Mobs (contribution from Sunconure11)
* Additional player entity check (fixes conflict with "Welcome to the Jungle")

[v2.7.0](https://github.com/WesCook/Nutrition/releases/tag/v2.7.0) - 2018-01-26
* Added support for Cave Root, Combustive Fishing, NetherEx, Primitive Mobs, and The Betweenlands (contribution from Sunconure11)
* Updated support for Aether Legacy, and Ice and Fire (contribution from Sunconure11)
* Updated support for HarvestCraft, SaltyMod, VanillaFoodPantry, and PrimalCore
* Minor config comment changes
* Bumped Forge version

[v2.6.0](https://github.com/WesCook/Nutrition/releases/tag/v2.6.0) - 2017-11-24
* Added support for Skye's Donuts and Duck Craft
* Updated Reptile Mod support (contribution from Sunconure11)

[v2.5.0](https://github.com/WesCook/Nutrition/releases/tag/v2.5.0) - 2017-11-10
* Added support for Chococraft, Defiled Lands, Mob Mash, and Twilight Forest (contribution from Sunconure11)
* Added German localizations (contribution from 3TUSK)

[v2.4.1](https://github.com/WesCook/Nutrition/releases/tag/v2.4.1) - 2017-09-23
* Now compatible with any Forge 1.12.* version

[v2.4.0](https://github.com/WesCook/Nutrition/releases/tag/v2.4.0) - 2017-09-22
* Gui background can now be textured
* Hardcoded support for milk buckets
* HarvestCraft cakes are now supported
* Added SaltyMod support (contribution from Ash70)
* Added Actually Additions support (contribution from ethanator2008)
* Added Extra Utilities 2 support (contribution from ethanator2008)
* Added Plants 2 support
* Ported Animania support
* Ported VanillaFoodPantry support
* Updated to Forge 1.12.2

[v2.3.0](https://github.com/WesCook/Nutrition/releases/tag/v2.3.0) - 2017-09-08
* GUI now resizes horizontally for long nutrient names
* Nutrition button can now be positioned more dynamically (eg. top-right of screen)
* Nutrition button now accepts negative coordinates
* Added Primal Core support (contribution from Sunconure11)
* Added RealWorld support (contribution from Ash70)
* Added Better with Addons support
* Updated to Forge 1.12.1

[v2.2.0](https://github.com/WesCook/Nutrition/releases/tag/v2.2.0) - 2017-08-04
* Added config option to move nutrition button

[v2.1.0](https://github.com/WesCook/Nutrition/releases/tag/v2.1.0) - 2017-08-04
* Small GUI texture optimization
* Added support for Reptiles Mod (contribution from Sunconure11)

[v2.0.0](https://github.com/WesCook/Nutrition/releases/tag/v2.0.0) - 2017-08-03
* Updated to Minecraft 1.12.  Previous versions will only receive major bugfixes.
* The nutrient detection mode has been removed in favor of a more powerful whitelist system
* The decay multiplier can now be set on a per-nutrient basis
* The nutrition button UI has changed to match the recipe book
* Improved logging feature to show foods without registered nutrients
* Updated support for missing foods, and removed mods which haven't been ported yet

[v1.6.0](https://github.com/WesCook/Nutrition/releases/tag/v1.6.0) - 2017-08-01
* Added config to prevent nutrition from being reset back up to the minimum upon death
* Amplifier is no longer a required field
* Added support for VanillaFoodPantry (contribution from darloth)
* Updated support for Animania (contribution from Sunconure11)

[v1.5.0](https://github.com/WesCook/Nutrition/releases/tag/v1.5.0) - 2017-06-28
* Added nourished and malnourished effects.  Each adds or subtracts one heart per amplifier level.
* Added `cumulative_modifier` property to effects.  In cumulative detection mode, this will increase the amplifier by the provided value for each cumulative level.  

[v1.4.0](https://github.com/WesCook/Nutrition/releases/tag/v1.4.0) - 2017-06-22
* Added support for Aether Legacy, Animalium, Aquaculture, Ice and Fire, Jurassicraft, Plants, and Rustic (contribution from Sunconure11)

[v1.3.1](https://github.com/WesCook/Nutrition/releases/tag/v1.3.1) - 2017-06-13
* Enabled Nutrition key to both open and close GUI
* Fixed severe nutrition decay bug in multiplayer

[v1.3.0](https://github.com/WesCook/Nutrition/releases/tag/v1.3.0) - 2017-06-10
* Corrected packet sync error which resulted in clients showing incorrect data under some conditions 
* Added Dutch localizations (contribution from Arthur Dent)
* Added Spanish localizations (contribution from Rougito)

[v1.2.0](https://github.com/WesCook/Nutrition/releases/tag/v1.2.0) - 2017-06-03
* Updated support for HarvestCraft on 1.10
* Added German localizations (contribution from ACGaming)
* Added Swedish localizations (contribution from John "Rufus" Lundström)
* Added Norwegian localizations (contribution from Marcus "Rex" Holm)

[v1.1.0](https://github.com/WesCook/Nutrition/releases/tag/v1.1.0) - 2017-05-31
* Added support for Biomes O' Plenty, Forestry, Natura, Roots, Simply Tea!, and Tinkers Construct (contribution from KnightMiner)
* Nutrient field can now be negated from other detection modes (contribution from KnightMiner)

[v1.0.1](https://github.com/WesCook/Nutrition/releases/tag/v1.0.1) - 2017-05-28
* Improved detection when attaching capability

[v1.0.0](https://github.com/WesCook/Nutrition/releases/tag/v1.0.0) - 2017-05-28
* Initial release
