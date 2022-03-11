# Connect Quest

This is the source code for [City Flow / Connect Quest](https://play.google.com/store/apps/details?id=uk.co.jakelee.cityflow), feel free to do whatever you want with it!

## Screenshots
| Start screen | In game | In editor | Sharing | Shop | Settings
| -- | -- | -- | -- | -- | -- |
| ![image](https://user-images.githubusercontent.com/12380876/157317488-e42fe747-0cb0-4413-b88b-9011167e15f6.png) | ![EVqH1lK](https://user-images.githubusercontent.com/12380876/157317140-59125690-1740-4a7b-b2be-0a5d8916f744.png) | ![image](https://user-images.githubusercontent.com/12380876/157317261-23d68bcc-57ef-44b6-8fd2-9b66e59a5350.png) | ![image](https://user-images.githubusercontent.com/12380876/157317294-61a7a8d0-bdec-4ef4-8c0e-f6d9c0a6f38e.png) | ![image](https://user-images.githubusercontent.com/12380876/157317399-5c98f46c-6b0c-4f3d-9a55-4be82594f3c7.png) | ![image](https://user-images.githubusercontent.com/12380876/157317438-ec67cc61-ccb5-4031-bc4c-5532f4c9a2eb.png) |

## Play Store description

✔️ Features:

* Complete 235+ puzzles!
* Unlock and use 215+ tiles!
* 12 flows across 6 environments!
* Create, share, and import your own puzzles!
* Generate over 235 ^ 215 (1 septuagintacentillion 😲) custom puzzles, you'll never run out!
* Earn boosts to gain an advantage!

🙌 Also:

* No internet required!
* 🔋 Low battery usage!
* Regular content additions & new features!
* Supports Chinese, Dutch, English, French, German, Polish, Russian, Spanish, and Swedish!
* Earn coins to unlock boosts, game modes, tiles, and packs!

🎨 Customisation:

* Choose your favourite background / in-game music, or leave on random!
* Don't like the game sounds 🎶? Pick new ones!
* Unlock and use new in-game background colours!

🎮 Google Play:

* 30 Achievements
* 23 Leaderboards
* 21 Daily / Weekly Quests
* Automatic & Manual Cloud Saves 💾

🤓 About Developer:

Connect Quest is created and maintained by Jake Lee, a software engineer from England. If you've encountered a 🐛 bug 🐝, or have an idea for a new feature, please mention it in a review or on https://reddit.com/r/ConnectQuest and I'll reply ASAP. I don't bite!

📲 Supported Devices:

* All Android versions from 🍦 Ice Cream Sandwich (4.0.3+) to Nougat, and beyond.
* All phone & tablet sizes, from a tiny 3.7" Nexus One to a chubby 5.7" Nexus 6P, and beyond to the 10.1" Nexus 10!
* Portrait & landscape modes.
* Google Play Services are optional.

🔒 Permissions:

* Billing: Used for in app purchases.
* Internet, External Storage, Network State: Used to save / import puzzles, and download adverts.
* External Files / Photos: Used to import + export puzzle cards.
* Vibrate: Optional vibration on tile rotate.

👋 Want to help out?<br>
💬 Share the app with others!<br>
👍 Leave a review or email me at connect.quest@jakelee.co.uk!<br>
💰 Purchase coins, coin doublers, or the tile unlocker!

Happy flowing, connect questers!

## Codebase notes
* The app is pretty much run entirely by a database, see `PatchHelper.java`.
* Each screen has an Activity, a very basic and outdated architecture.
* `TileHelper.java`, `PuzzleHelper.java`, and `DisplayHelper.java` are probably the most complicated / interesting files.
* The strings are in an insane database-driven system (see `TextHelper.java`). Good luck using it!
* It requires an older version of Android Studio to build, and likely a lot of changes.

## Original repository metadata
* First commit: 3rd June 2016
* Last commit: 4th February 2017
* Total commits: 573
* Releases: 12

## Licensing
* Entire repository is under the MIT license, essentially do whatever you want but don't blame me if it breaks!
* All images are modified versions of [Kenney](https://www.kenney.nl/assets?s=city) assets.

## Redacted information
* `MainApplication.java`: [Batch](https://doc.batch.com/) config & GCM sender ID redacted.
* `AndroidManifest.xml`: [AppLovin](https://www.applovin.com/) SDK key redacted.
