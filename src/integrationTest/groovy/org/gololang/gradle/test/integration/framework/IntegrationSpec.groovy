/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gololang.gradle.test.integration.framework

import groovy.transform.CompileStatic
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.GradleProject
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author Marcin Erdmann
 */
abstract class IntegrationSpec extends Specification {

  @Rule final TemporaryFolder dir = new TemporaryFolder()

  private final OutputStream standardError = new ByteArrayOutputStream()
  private final OutputStream standardOutput = new ByteArrayOutputStream()

  protected GradleProject run(String... tasks) {
    ProjectConnection connection = GradleConnector.newConnector().forProjectDirectory(dir.root).connect()

    try {
      BuildLauncher builder = connection.newBuild()
      builder.standardError = standardError
      builder.standardOutput = standardOutput
      builder.forTasks(tasks).run()
      def model = connection.getModel(GradleProject)
      return model
    }
    finally {
      connection?.close()
    }
  }

  protected List<String> getUpToDateTasks() {
    standardOutput.toString().readLines().findResults {
      def match = (it =~ /(:\S+?(:\S+?)*)\s+UP-TO-DATE/)
      match ? match[0][1] : null
    }
  }

  boolean upToDate(String task) {
    task in upToDateTasks
  }

  protected String getStandardErrorOutput() {
    standardError.toString()
  }

  protected File getBuildFile() {
    file('build.gradle')
  }

  protected File directory(String path) {
    new File(dir.root, path).with {
      mkdirs()
      it
    }
  }

  protected File file(String path) {
    def splitted = path.split('/')
    def directory = splitted.size() > 1 ? directory(splitted[0..-2].join('/')) : dir.root
    def file = new File(directory, splitted[-1])
    file.createNewFile()
    file
  }

  protected boolean fileExists(String path) {
    new File(dir.root, path).exists()
  }
}