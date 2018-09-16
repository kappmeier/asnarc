package de.kappmeier.asnarc.event

import de.kappmeier.asnarc.entity.Entity

trait ToEvent extends SnakeEvent {
  def applyTo(entity: Entity): (Entity, Seq[SnakeEvent])
}
