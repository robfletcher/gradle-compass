package co.freeside.gradle.compass

import com.github.jrubygradle.JRubyExec
import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

import static co.freeside.gradle.compass.CompassPlugin.CONFIGURATION_NAME

class CompassTask extends JRubyExec {

  String command

  boolean sourcemap
  boolean time
  boolean debugInfo
  @InputDirectory @Optional File load
  @InputDirectory @Optional File loadAll
  @InputFiles @Optional FileCollection importPath
  boolean quiet
  boolean trace
  boolean force
  boolean boring
  @InputFile @Optional File config
  @InputDirectory File sassDir
  @OutputDirectory File cssDir
  @InputDirectory @Optional File imagesDir
  @InputDirectory @Optional File javascriptsDir
  @InputDirectory @Optional File fontsDir
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

    project.configurations.getByName(CONFIGURATION_NAME).dependencies.each {
      if (it.name != "compass") {
        scriptArgs << "--require" << it.name
      }
    }

    scriptArgs.addAll new ScriptArgumentsBuilder(this)
        .bool("--sourcemap")
        .bool("--time")
        .bool("--debug-info")
        .file("--load")
        .file("--load-all")
        .files("--import-path")
        .bool("--quiet")
        .bool("--trace")
        .bool("--force")
        .bool("--boring")
        .str("--config")
        .file("--sass-dir")
        .file("--css-dir")
        .file("--images-dir")
        .file("--javascripts-dir")
        .file("--fonts-dir")
        .str("--output-style")
        .bool("--relative-assets")
        .bool("--no-line-comments")
        .str("--http-path")
        .str("--generated-images-path")
        .toArgumentList()

    scriptArgs.addAll(super.scriptArgs())
    return scriptArgs
  }

  @Override
  List<String> getArgs() {
    JRubyExecUtils.buildArgs(jrubyArgs(), script, scriptArgs())
  }
}
