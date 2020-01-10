package com.github.robfletcher.compass

import com.github.jrubygradle.JRubyExec
import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*

import static CompassPlugin.CONFIGURATION_NAME

class CompassTask extends JRubyExec implements CompassTaskOptions {

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
  String env
  String outputStyle
  boolean relativeAssets
  boolean noLineComments
  String httpPath
  String generatedImagesPath
  File gemDir

  CompassTask() {
    script = new File("compass")
    defaultCharacterEncoding = "UTF-8"
  }

  @Override
  Provider<File> getGemWorkDir() {
    project.provider { ->
      getGemDir()
    }
  }

  @Override
  List<String> getArgs() {
    JRubyExecUtils.buildArgs([], jrubyArgs, getScript(), ScriptArgumentsBuilder.compassArgs(this))
  }
}
