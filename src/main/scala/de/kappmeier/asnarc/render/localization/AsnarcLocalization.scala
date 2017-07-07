package de.kappmeier.asnarc.render.localization

/**
  * Provides all texts that are used in the frontend.
  *
  * @author Jan-Philipp Kappmeier
  */
trait AsnarcLocalization {

    /**
      * Displays the message to start the game by pressing a key or mouse button.
      */
    val stateMessageStart: String

    /**
      * The status message for a paused game.
      */
    val stateMessagePause: String

    /**
      * The status message for a failed game.
      */
    val stateMessageGameOver: String

    /**
      * The status message containing the current position of the head. Accepts two decimal parameters for `x`- and `y`-coordinate.
      */
    val statusPosition: String

    /**
      * The status text that is displayed below the board. Accepts four decimal parameters and displays current length,
      * current points, average steps per food and average turns per food.
      */
    val statusText: String

}
