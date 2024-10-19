package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.AsnarcSpec

class AsnarcJSRendererTest extends AsnarcSpec {

  behavior of "AsnarcJSRendererTest"

  it should "strip trailing dollar in simple class name" in {
    AsnarcJSRenderer.stripSimpleName("Wall$") should be("Wall")
  }

  it should "not strip anything from simple class name" in {
    AsnarcJSRenderer.stripSimpleName("Wall") should be("Wall")
  }

}
