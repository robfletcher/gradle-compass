# Gradle Compass plugin

A [SASS][sass] / [Compass][compass] plugin for [Gradle][gradle]. The plugin uses JRuby to install and run Compass.

## Tasks

The plugin adds the following tasks:

### compassCompile

Compiles all SASS files. Equivalent to the `compass compile` command. The task supports incremental build.

### compassWatch

Compiles and watches all SASS files. Equivalent to the `compass watch` command.

### compassClean

Deletes generated stylesheets.

### compassVersion

Prints out the compass version.

### compassConfig

Writes compass configuration out to `config/compass.rb`.

## Installation

Add the plugin's Bintray repository to your `buildscript` and apply the plugin:

```groovy
apply plugin: 'compass'

buildscript {
	repositories {
		mavenCentral()
		maven { url 'http://dl.bintray.com/robfletcher/gradle-plugins' }
	}
	dependencies {
		classpath 'org.gradle.plugins:gradle-compass:2.0'
	}
}

repositories {
	jcenter() // or any other repository containing JRuby
}
```

## Configuration

General configuration for the plugin goes inside a `compass` block in your build file and will apply to all tasks. You can also specify configuration properties on the individual tasks (for example you may want to set `environment = "production"` on the *compileSass* and `debugInfo = true` on *watchSass*). As a minimum you must specify the target directory where compiled CSS files should go and the source directories containing SASS/SCSS files. For example:

```groovy
compass {
	cssDir = file("public/styles")
	sassDir = file("src/main/sass")
}
```

### Configuration parameters

The full set of parameters supported by the plugin is…

#### Paths

* `cssDir` **required**: the target directory where compiled CSS is output. Equivalent to `--css-dir`.
* `sassDir` **required**: the source directory where you keep *.scss* and/or *.sass* files. Equivalent to `--sass-dir`.
* `imagesDir`: the source directory where you keep image files. Equivalent to `--images-dir`.
* `javascriptsDir`: the source directory where you keep JavaScript files. You don't need to specify this unless you have Compass extensions in your scripts. Equivalent to `--javascripts-dir`.
* `fontsDir`: the source directory where you keep fonts. Equivalent to `--fonts-dir`.
* `importPath`: a set of directories containing other Sass stylesheets. Specifying this allows you to reference those stylesheets in `@import` directives. Equivalent to `--import-paths`.
* `load`: loads a framework or extensions found in the specified directory. Equivalent to `--load`.
* `loadAll`: loads all frameworks or extensions found in the specified directory. Equivalent to `--load-all`.

#### Compilation options

* `sourcemap`: if *true* Compass will generate a sourcemap during compilation. Equivaluent to `--sourcemap`.
* `debugInfo`: if *true* (the default) Compass adds debug information to the compiled CSS. Equivalent to `--debug-info` if set to *true* or `--no-debug-info` if set to *false*.
* `force`: if *true* Compass will overwrite existing files. Equivalent to `--force`.
* `environment`: sets default options when set to *'development'* (the default) or *'production'*. Equivalent to `--environment`.
* `noLineComments`: if *true* Compass will not output line comments to the compiled CSS files. Equivalent to `--no-line-comments`.
* `outputStyle`: selects the style for compiled CSS. One of *nested*, *expanded*, *compact* (the default) or *compressed*. Eqivalent to `--output-style`.
* `relativeAssets`: if *true* Compass will generate relative urls to assets. Equivalent to `--relative-assets`.
* `httpPath`: sets the path to the root of the web application when deployed. Equivalent to `--http-path`.
* `generatedImagesPath`: sets the path where generated images are stored. Equivalent to `--generated-images-path`.

#### Command line output

* `time`: if *true* Compass will print timing information during compilation. Equivaluent to `--time`.
* `boring`: if *true* colorized output is disabled. Equivalent to `--boring`.
* `quiet`: if *true* Compass output is suppressed. Equivalent to `--quiet`.
* `trace`: if *true* Compass displays full stack traces on error. Equivalent to `--trace`.

### Specifying the Compass version

By default the plugin will use the latest version of Compass available. If you need a specific version you can set the version using Gradle's dependency management. For example:

    dependencies {
      compass "rubygems:compass:1.0.1"
    }

Gems are installed using the JRuby Gradle plugin. The Compass plugin creates a special _"compass"_ configuration that is used by all the plugin's tasks.

### Using additional gems

You can use Comass extensions from Ruby gems by adding dependencies to the _compass_ configuration. The plugin will automatically add a `--require` argument for each gem when invoking Compass commands. For example to use the _Breakpoint_ extension:

    dependencies {
      compass "rubygems:breakpoint:2.5.0"
    }

## Using with other tasks

You will typically want to execute the Compass tasks as part of a larger build. For example this configuration will run `compileSass` as part of `processResources`, `watchSass` as part of the [Application plugin][app-plugin]'s `run` task and clean the output of `compileSass` as part of `clean`:

```groovy
processResources.inputs.files compileSass
run.dependsOn watchSass
clean.dependsOn cleanCompileSass
```

# Version history

### 2.0

* JRuby is handled by the [JRuby Gradle plugin](https://github.com/jruby-gradle/jruby-gradle-plugin).

### 1.0.10

* added ability to specify gem versions.

### 1.0.9

* use additional gems without needing a *config.rb* file.

### 1.0.8

* added ability to specify additional gems.

### 1.0.7

* made `javascriptsDir`, `imagesDir` and `importPath` optional.

### 1.0.6

* added ability to specify `importPath`.

### 1.0.5

* added various command line options. Thanks [Ben Groves](http://github.com/bgroves).

### 1.0.4

* added ability to specify file encoding used by JRuby.

[app-plugin]:http://www.gradle.org/docs/current/userguide/application_plugin.html
[compass]:http://compass-style.org/
[gradle]:http://gradle.org/
[sass]:http://sass-lang.com/
