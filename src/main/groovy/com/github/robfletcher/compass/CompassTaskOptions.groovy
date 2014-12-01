package com.github.robfletcher.compass

import org.gradle.api.Task
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.IConventionAware

interface CompassTaskOptions extends Task, IConventionAware {

  String getCommand()

  void setCommand(String command)

  boolean getSourcemap()

  void setSourcemap(boolean sourcemap)

  boolean getTime()

  void setTime(boolean time)

  boolean getDebugInfo()

  void setDebugInfo(boolean debugInfo)

  File getLoad()

  void setLoad(File load)

  File getLoadAll()

  void setLoadAll(File loadAll)

  FileCollection getImportPath()

  void setImportPath(FileCollection importPath)

  boolean getQuiet()

  void setQuiet(boolean quiet)

  boolean getTrace()

  void setTrace(boolean trace)

  boolean getForce()

  void setForce(boolean force)

  boolean getBoring()

  void setBoring(boolean boring)

  File getConfig()

  void setConfig(File config)

  File getSassDir()

  void setSassDir(File sassDir)

  File getCssDir()

  void setCssDir(File cssDir)

  File getImagesDir()

  void setImagesDir(File imagesDir)

  File getJavascriptsDir()

  void setJavascriptsDir(File javascriptsDir)

  File getFontsDir()

  void setFontsDir(File fontsDir)

  String getEnv()

  void setEnv(String env)

  String getOutputStyle()

  void setOutputStyle(String outputStyle)

  boolean getRelativeAssets()

  void setRelativeAssets(boolean relativeAssets)

  boolean getNoLineComments()

  void setNoLineComments(boolean noLineComments)

  String getHttpPath()

  void setHttpPath(String httpPath)

  String getGeneratedImagesPath()

  void setGeneratedImagesPath(String generatedImagesPath)
}
