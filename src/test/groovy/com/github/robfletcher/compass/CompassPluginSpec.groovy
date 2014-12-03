package com.github.robfletcher.compass

import com.steadystate.css.parser.CSSOMParser
import com.steadystate.css.parser.SACParserCSS3
import nebula.test.IntegrationSpec
import org.w3c.css.sac.InputSource
import org.w3c.dom.css.CSSRuleList
import spock.lang.Shared

abstract class CompassPluginSpec extends IntegrationSpec {

  @Shared parser = new CSSOMParser(new SACParserCSS3())
  protected final COMPILE_TASK_NAME = "compassCompile"
  protected final CLEAN_TASK_NAME = "compassClean"
  protected final WATCH_TASK_NAME = "compassWatch"

  def setup() {
    fork = true // don't get stdout from the compass process without this

    buildFile.text = applyPlugin(CompassPlugin)
  }

  protected CSSRuleList stylesheet(String path) {
    stylesheet file(path)
  }

  protected CSSRuleList stylesheet(File file) {
    assert file, "file $file.path does not exist"
    file.withReader { r ->
      parser.parseStyleSheet(new InputSource(r), null, null).cssRules
    }
  }
}
