# سهل للقبلة · Sahl Qibla

A calm Qibla compass that **checks itself** before it guides you. Part of the **Sahl apps** family: Sahl Adhan, Sahl Phone, Sahl Contacts, Sahl Messages, Sahl Files, Sahl Tools, Sahl Sync and Sahl TV.

**⬇ Download:** [latest release](https://github.com/AliSherif1309/TrueQibla-Android/releases/latest) (`SahlQibla-v1.x.apk`). Android 8 or newer.

> **Moving from True Qibla (com.aly.trueqibla)?** Sahl Qibla is now signed with the shared Sahl key and uses the package `com.sahl.qibla`, so it installs as a new app. Uninstall the old *True Qibla / Sahl Qibla 1.x* first. Only a few settings are lost.

## What it does
- **Opens straight to the arrow**, with nothing to set up.
- **Checks itself**: it watches the magnetic field strength and dip, and the age and accuracy of your location. If it can't trust the reading, it says so instead of pointing wrong.
- **Vibration guidance**: ticks speed up as you turn toward the Qibla, then a double buzz when you're facing it.
- **Prayer-mat mode**: a dark, dimmed screen with only the arrow and no vibration.
- **Sun over the Kaaba (رصد القبلة)**: on the two days a year the sun passes over the Kaaba, the app shows the time when every shadow points exactly away from the Qibla. It's a natural check, with an optional reminder that plays the Sahl chime.
- **Mini globe** (Detailed view): the shortest path from you to Makkah, with the distance.
- **Arabic / English** (right-to-left for Arabic), **light / dark**, and **Simple / Detailed** views.

## Methods
- **Smart** (default): the best available sensors, with live checks.
- **Strict**: guides only when the field checks pass.
- **Room average**: combines readings indoors. It's approximate, and agreement between readings doesn't prove accuracy.

For the most reliable result, stand away from metal, magnets and magnetic phone cases, and hold the phone flat with the top edge pointing ahead. For an independent check, use the sun on a "Sun over the Kaaba" day or a surveyed direction. Details: [ACCURACY_AUDIT.md](ACCURACY_AUDIT.md).

## Privacy
The compass works fully offline. No ads, no tracking and no runtime libraries. The internet is used only to check GitHub for updates, and your location never leaves the phone.

## Sahl family
- **Sahl key and Sahl Union:** signed with the same key as every Sahl app, so they can safely share data through the `com.sahl.permission.UNION` signature permission.
- **Crash report:** if the app ever crashes, a report screen opens with a **Copy** button.
- **Updates in the app:** it checks once a day. Updates are accepted only from this repository's releases, and only if the checksum, size, package and signing key all match. See [UPDATES.md](UPDATES.md).

## Build
Every push to `main` runs the tests and lint, then builds a signed APK and publishes it as the latest release.
Repository secrets (shared by all Sahl apps; never commit the keystore): `KEYSTORE_B64`, `KEYSTORE_PASS`.

Local check (Java 17, Android SDK 34): `./gradlew testDebugUnitTest lintDebug assembleDebug --no-daemon`

## License
[MIT](LICENSE)
