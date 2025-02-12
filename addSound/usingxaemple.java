public void name() {
    if (playerWins) {
        SoundEffects.winSound();  // play sound effect--win
    } else if (playerLoses) {
        SoundEffects.loseSound(); // play sound effect--lose
    }
}
