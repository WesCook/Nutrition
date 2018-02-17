# Nutrition

Nutrition is a highly configurable mod to enable the creation of health systems in Minecraft.

See [releases tab for downloads](https://github.com/WesCook/Nutrition/releases), and [Wiki for documentation](https://github.com/WesCook/Nutrition/wiki).

![Nutrition GUI](screenshots/gui.png "Nutrition GUI")

Through its [config files](https://github.com/WesCook/Nutrition/wiki/Configuration), Nutrition allows you to define custom food groups (called [nutrients](https://github.com/WesCook/Nutrition/wiki/Nutrients)) which apply [effects](https://github.com/WesCook/Nutrition/wiki/Effects) based on nutrition levels.

By default, Nutrition comes configured with the classic five food group system: dairy, fruit, grain, protein, and vegetables.  However many different systems are possible.  Some examples include:

* A spicy tolerance bar, where eating enough spicy foods will grant a permanent resistance to fire.
* A zombie simulation where you must constantly eat brains/flesh to avoid negative effects.
* Gluten intolerance.  Eat bread, get sick.

## Mods Supported

Mods that are supported with the default five-food group system include:

* [Actually Additions](https://minecraft.curseforge.com/projects/actually-additions)
* [Aether Legacy](https://minecraft.curseforge.com/projects/aether-legacy)
* [Animania](https://minecraft.curseforge.com/projects/animania)
* [Better with Addons](https://minecraft.curseforge.com/projects/better-with-addons)
* [Better with Mods](https://minecraft.curseforge.com/projects/better-with-mods)
* [Biomes O' Plenty](https://minecraft.curseforge.com/projects/biomes-o-plenty)
* [Cave Root](https://minecraft.curseforge.com/projects/cave-root)
* [Chococraft](https://minecraft.curseforge.com/projects/chococraft)
* [Combustive Fishing](https://minecraft.curseforge.com/projects/combustive-fishing)
* [Defiled Lands](https://minecraft.curseforge.com/projects/defiled-lands)
* [Duck Craft](https://minecraft.curseforge.com/projects/duck-craft)
* [Edible Bugs](https://minecraft.curseforge.com/projects/edible-bugs)
* [Extra Utilities 2](https://minecraft.curseforge.com/projects/extra-utilities)
* [Food Expansion](https://minecraft.curseforge.com/projects/food-expansion)
* [Forestry](https://minecraft.curseforge.com/projects/forestry)
* [Grim Pack](https://minecraft.curseforge.com/projects/grim-pack)
* [Ice and Fire](https://minecraft.curseforge.com/projects/ice-and-fire-dragons-in-a-whole-new-light)
* [Lycanites Mobs](https://minecraft.curseforge.com/projects/lycanites-mobs)
* [Mob Mash](https://minecraft.curseforge.com/projects/mob-mash)
* [Natura](https://minecraft.curseforge.com/projects/natura)
* [NetherEx](https://minecraft.curseforge.com/projects/netherex)
* [Pam's HarvestCraft](https://minecraft.curseforge.com/projects/pams-harvestcraft)
* [Plants](https://minecraft.curseforge.com/projects/plants)
* [PrimalCore](https://minecraft.curseforge.com/projects/primalcore)
* [Primitive Mobs](https://minecraft.curseforge.com/projects/primitive-mobs)
* [RealWorld](http://10paksmods.net/realworld.html)
* [Reptile Mod](https://minecraft.curseforge.com/projects/reptilemod)
* [Roots](https://minecraft.curseforge.com/projects/roots)
* [Rustic](https://minecraft.curseforge.com/projects/rustic)
* [SaltyMod](https://minecraft.curseforge.com/projects/saltymod)
* [Simple Corn](https://minecraft.curseforge.com/projects/simple-corn)
* [Simply Tea!](https://minecraft.curseforge.com/projects/simply-tea)
* [Skye's Donuts](https://minecraft.curseforge.com/projects/skyes-donuts)
* [The Betweenlands](https://minecraft.curseforge.com/projects/angry-pixel-the-betweenlands-mod)
* [Tinkers Construct](https://minecraft.curseforge.com/projects/tinkers-construct)
* [Twilight Forest](https://minecraft.curseforge.com/projects/the-twilight-forest)
* [VanillaFoodPantry](https://minecraft.curseforge.com/projects/vanillafoodpantry-mod)

Mod support contributions are welcome!  Please create a [Pull Request](https://github.com/WesCook/Nutrition/pulls) or an [Issue](https://github.com/WesCook/Nutrition/issues) with the related .json files.

When updating the mod, please clear your /nutrition/ config directory to receive new mod support (point releases will not require this).

## Changelog

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
