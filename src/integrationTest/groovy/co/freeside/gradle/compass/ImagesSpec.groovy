package co.freeside.gradle.compass

import spock.lang.Shared

class ImagesSpec extends CompassPluginSpec {

  @Shared testImage = ImagesSpec.getResource("/sacred-chao.png")

  def "can read images from the default images directory"() {
    given:
    file("src/main/images/sacred-chao.png").bytes = testImage.bytes
    file("src/main/sass/image.scss") << '''
      .chao { background: image-url('sacred-chao.png', false, false); }
    '''

    when:
    run COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/image.css")) {
      item(0).cssText == "*.chao { background: url(/images/sacred-chao.png) }"
    }

    and:
    file("build/stylesheets/images/sacred-chao.png").isFile()
  }

  def "can read specify an images directory"() {
    given:
    buildFile << '''
      compass {
        imagesDir = file("src/main/images")
      }
    '''

    and:
    file("src/main/images/sacred-chao.png").bytes = testImage.bytes
    file("src/main/sass/image.scss") << '''
      .chao { background: image-url('sacred-chao.png', false, false); }
    '''

    when:
    run COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/image.css")) {
      item(0).cssText == "*.chao { background: url(/images/sacred-chao.png) }"
    }
  }

  def "can use relative paths"() {
    given:
    buildFile << '''
      compass {
        relativeAssets = true
      }
    '''

    and:
    file("src/images/sacred-chao.png").bytes = testImage.bytes
    file("src/main/sass/image.scss") << '''
      .chao { background: image-url('sacred-chao.png', false, false); }
    '''

    when:
    run COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/image.css")) {
      item(0).cssText == "*.chao { background: url(../../images/sacred-chao.png) }"
    }
  }

  def "can use image helpers"() {
    given:
    buildFile << '''
      compass {
        imagesDir = file("src/main/images")
      }
    '''

    and:
    file("src/main/images/sacred-chao.png").bytes = testImage.bytes
    file("src/main/sass/image.scss") << '''
      .chao { width: image-width('sacred-chao.png'); }
    '''

    when:
    run COMPILE_TASK_NAME

    then:
    with(stylesheet("build/stylesheets/image.css")) {
      item(0).cssText == "*.chao { width: 40px }"
    }
  }

}
