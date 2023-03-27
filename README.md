# Android Music Player (Oct-2020)

An android audio-player built using MediaPlayer and MediaStore interfaces.

## Feature

- Plays the selected audio file. Nothing more, nothing less.

File extensions allowed are limited to the MediaStore support. For each audio file in the external storage `MediaStore.Audio.Media.IS_MUSIC` flag is checked to determine whether or not that file is considered a music file. [MediaStore.Audio.AudioColumns interface details](https://developer.android.com/reference/android/provider/MediaStore.Audio.AudioColumns#IS_MUSIC)

## Application Screenshots

![Audio-Player, Music-List screens](/assets/img/MusicPlayer_output.jpg)

## Install the application

1. Download the `music-player.apk` file available in the "root" directory.
2. Copy the APK file to your android device and install it on your device.
3. You may be prompted to allow to `Install unknown apps` from your File Storage application, that you are currently using
4. Further more, if **Google Play Protect** is setup on your device, it may stop the installation, giving a message as `Unsafe app blocked`, look for an option similar to `More Details` on the pop-up and click on `Install Anyway` to complete the installation.

## Permissions Required

- READ_EXTERNAL_STORAGE

```
Allow Music Player to access photos and media on your device?
```

The application will only attempt to access the audio files to play during the runtime. But will need to parse through the entire external storage in order to list out the music files in the application.
