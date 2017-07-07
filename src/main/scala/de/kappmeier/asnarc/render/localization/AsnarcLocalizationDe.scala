package de.kappmeier.asnarc.render.localization

/**
  * German localization of the Asnarc frontend.
  *
  * @author Jan-Philipp Kappmeier
  */
class AsnarcLocalizationDe extends AsnarcLocalization {
    override val stateMessageStart = "Press button or click to start"

    override val stateMessagePause: String = "Pause"

    override val stateMessageGameOver: String = "Game Over"

    override val statusPosition: String = "Position: (%d,%d)"

    override val statusText: String = "Länge: %d Punkte: %d Zeit pro Nahrung: %d Kurven pro Nahrung: %d"
}
