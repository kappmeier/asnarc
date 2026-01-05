package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.AsnarcSpec

class AsnarcJSRendererTest extends AsnarcSpec {

  behavior of "AsnarcJSRendererCompanion"

  it should "strip trailing dollar in simple class name" in {
    AsnarcJSRenderer.stripSimpleName("Wall$") should be("Wall")
  }

  it should "not strip anything from simple class name" in {
    AsnarcJSRenderer.stripSimpleName("Wall") should be("Wall")
  }

  behavior of "AsnarcJSRenderer"

  it should "calculate draw size as size minus border for size 10" in {
    val renderer = new AsnarcJSRenderer(10)
    renderer.DrawSize should be(8)
  }

  it should "calculate border as 1 for size 10" in {
    val renderer = new AsnarcJSRenderer(10)
    renderer.Border should be(8)
  }

  it should "calculate canvas width based on columns and size" in {
    val renderer = new AsnarcJSRenderer(10)
    renderer.canvasWidth(60) should be(600)
  }

  it should "calculate canvas height based on rows and size plus info line" in {
    val renderer = new AsnarcJSRenderer(10)
    // 40 rows * 10 pixels + 20 pixels info line = 420
    renderer.canvasHeight(40) should be(420)
  }

  it should "calculate correct values for different block sizes" in {
    val renderer = new AsnarcJSRenderer(20)
    renderer.Size should be(20)
    renderer.Border should be(4)
    renderer.DrawSize should be(16)
    renderer.canvasWidth(30) should be(600)
    renderer.canvasHeight(20) should be(420)
  }

}
