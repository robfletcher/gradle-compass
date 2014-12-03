package com.github.robfletcher.compass

import com.github.jengelman.gradle.plugins.processes.tasks.JavaFork
import com.github.jrubygradle.GemUtils
import com.github.jrubygradle.internal.JRubyExecUtils
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*

class CompassFork extends JavaFork implements CompassTaskOptions {

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

  @TaskAction @Override void javafork() {
    def gemDir = getGemDir()

    gemDir.mkdirs()
    GemUtils.extractGems(
      project,
      project.configurations.getByName("jrubyExec"),
      project.configurations.getByName("compass"),
      gemDir,
      overwriteGems()
    )

    setMain "org.jruby.Main"
    environment GEM_HOME: gemDir, PATH: getComputedPATH(gemDir)
    classpath JRubyExecUtils.classpathFromConfiguration(project.configurations.getByName("jrubyExec"))
    args JRubyExecUtils.buildArgs(["-S"], new File("compass"), ScriptArgumentsBuilder.compassArgs(this))

    super.javafork()
  }

  String getComputedPATH(File gemDir) {
    def path = new File(gemDir, "bin")
    path.absolutePath + File.pathSeparatorChar + System.env.PATH
  }

  private GemUtils.OverwriteAction overwriteGems() {
    project.gradle.startParameter.refreshDependencies ? GemUtils.OverwriteAction.OVERWRITE : GemUtils.OverwriteAction.SKIP
  }
}
