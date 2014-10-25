package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

import static co.freeside.gradle.compass.CompassPlugin.CONFIGURATION_NAME

class CompassTask extends JRubyExec {

  String command
  @InputDirectory File sassDir
  @OutputDirectory File cssDir
  @InputFiles @Optional FileCollection importPath
  @InputDirectory @Optional File imagesDir
  @InputDirectory @Optional File javascriptsDir
  @InputDirectory @Optional File fontsDir
  @InputFile @Optional File config
  boolean sourcemap
  boolean time
  boolean debugInfo
  boolean quiet
  boolean trace
  boolean force
  boolean boring
  String outputStyle
  boolean relativeAssets
  boolean noLineComments
  String httpPath
  String generatedImagesPath

  CompassTask() {
    script = new File("compass")
    configuration = CONFIGURATION_NAME
  }

  @Override
  List<String> jrubyArgs() {
    def jrubyArgs = super.jrubyArgs()
    if (!jrubyArgs.contains("-S")) {
      jrubyArgs << "-S"
    }
    return jrubyArgs
  }

  @Override
  List<String> scriptArgs() {
    def scriptArgs = [command]
    scriptArgs << "--sass-dir" << getSassDir().path
    scriptArgs << "--css-dir" << getCssDir().path
    project.configurations.getByName(CONFIGURATION_NAME).dependencies.each {
      if (it.name != "compass") {
        scriptArgs << "--require" << it.name
      }
    }
    for (File importDir in getImportPath()?.files) {
      scriptArgs << "--import-path" << importDir.path
    }
    if (getImagesDir()) {
      scriptArgs << "--images-dir" << getImagesDir().path
    }
    if (getJavascriptsDir()) {
      scriptArgs << "--javascripts-dir" << getJavascriptsDir().path
    }
    if (getFontsDir()) {
      scriptArgs << "--fonts-dir" << getFontsDir().path
    }
    if (getConfig()) {
      scriptArgs << "--config" << getConfig().path
    }
    if (getHttpPath()) {
      scriptArgs << "--http-path" << getHttpPath()
    }
    if (getGeneratedImagesPath()) {
      scriptArgs << "--generated-images-path" << getGeneratedImagesPath()
    }
    if (isSourcemap()) {
      scriptArgs << "--sourcemap"
    }
    if (isTime()) {
      scriptArgs << "--time"
    }
    if (isDebugInfo()) {
      scriptArgs << "--debug-info"
    }
    if (isQuiet()) {
      scriptArgs << "--quiet"
    }
    if (isTrace()) {
      scriptArgs << "--trace"
    }
    if (isForce()) {
      scriptArgs << "--force"
    }
    if (isBoring()) {
      scriptArgs << "--boring"
    }
    if (getOutputStyle()) {
      scriptArgs << "--output-style" << getOutputStyle()
    }
    if (isRelativeAssets()) {
      scriptArgs << "--relative-assets"
    }
    if (isNoLineComments()) {
      scriptArgs << "--no-line-comments"
    }
    scriptArgs.addAll(super.scriptArgs())
    return scriptArgs
  }

  @Override
  List<String> getArgs() {
    JRubyExecUtils.buildArgs(jrubyArgs(), script, scriptArgs())
  }
}
