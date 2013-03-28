# Setting up a development environment

## Download IDEA and its sources
* Ensure you have the latest version of the open source [Intellij IDEA Community Edition](http://www.jetbrains.com/idea/free_java_ide.html) or the commercial edition.
* Launch your copy of IntelliJ IDEA and find its build number (found under `Intellij IDEA -> About Intellij IDEA` on Mac, and `Help -> About` on Linux and Windows).  You should see something like `IC-123.169`; the `123.169` part is the build number
* Clone the [IntelliJ IDEA Community Edition GitHub repo](https://github.com/JetBrains/intellij-community)
* In your clone of the Community Edition, check out the tag that corresponds to the version of Community Edition you downloaded.  All tags for IDEA have the form `idea/<build_number>`, so the command for our example build number above is:

```
git checkout idea/123.169
```
* To get the latest tags in your clone, issue the following command:
```
git pull --tags
```

## Create an IDEA-SilverStripe project
* Fork this project, clone it to your machine, and launch IDEA (can be the Community Edition you downloaded above, or some other edition you use in your day-to-day work)
* In IDEA, create a new project of the type **Intellij IDEA Plugin**, and configure it like the following, with `/projects/idea-silverstripe` replaced with the path where you cloned your fork of the project
* If you don't have **Intellij IDEA Plugin** as an option, ensure that "Plugin DevKit" is enabled in `Settings->Plugins`
* If you have not configured any SDKs you need to setup a Java SDK which is the JDK type and point it to your installation of JDK 1.6.
* After you've setup the JDK create a plugin SDK pointing to the install folder of IntelliJ IDEA and linked to the JDK.
* Also note that the **Project SDK** is set to `IDEA IC-\<build_number\>`.  To set this, click "New" and choose the location of your Community Edition install, then choose Java 1.6 when prompted
![ProjectSettings](markdown_images/project_setup.png)
* Click `OK`. Once the project loads up, configure the resources and gen directories:
    * right-click the "idea-silverstripe/resources" folder, and choose `Mark Directory As->Source Root`
    * right-click the "idea-silverstripe/gen" folder, and choose `Mark Directory As->Excluded`
* Add the Intellij source code
    * Open the "Project Structure" pane (`File -> Product Structure`)
    * In the `Sourcepath` tab of your IDEA Plugin SDK, add the directory where you cloned the Community Edition source code.  This will make debugging and exploring the source much more pleasant.
* Install the following developer plugins
    * PsiViewer
    * JFlex Support
    * Grammar kit is optional
* Configure JFlex
    * Open `Settings->JFlex` after you have installed the plugin and configure the path to JFlex and the skeleton file. These files are located in `tools/lexer/jflex-1.4` and `tools/lexer/idea-flex.skeleton` in your cloned IntelliJ IDEA repo.
    * You also need to turn off External build in `Settings->Compilation` in order for JFLex to be able to regenerate the lexer when you compile the project.

![ProjectSettings](markdown_images/sdk_setup_1.png)

## Ready to go
And that's it!  At this point, you should automatically have an `idea-handlebars` Run Configuration in IDEA (if not, create one of type "Plugin" and you should be good to go). If you Run/Debug this configuration, it will launch the Community Edition with your build of the plugin installed.

## Tips
* **Enable Internal Tools:** add `-Didea.is.internal=true` to the VM Options of your plugin Run Configurations to add various internal IDEA-development tools to your menu options.  Many of these look interesting, but the one you absolutely need is `Tools -> View PSI Structure of Current File...`
* **Test against any Jetbrains IDE:** you can run your plugin build inside of any IDEA-platform-based IDE (the Ultimate Edition, IDEA 12 EAP, Rubymine, PhpStorm, etc.) by setting up a Plugin SDK pointing at the install location of the IDE you want to test against.  You will not have the full source for most of these, but this comes in handy when backporting features, testing against an IDEA EAP, or troubleshooting IDE-specific problems.
    * *Note:* you do not need a license for any of the products you want to test against.  The plugin SDK will let you use a trial license, and you can "renew" this any time by deleting the **Sandbox Home** folder defined in `Project Structure -> SDKs`
![ProjectSettings](markdown_images/sdk_setup_2.png)
* **Hack on any open source plugin:** these setup instructions should apply for just about any plugin, so now you should be able to easily explore the code of [all of your favorite open source plugins](http://blogs.jetbrains.com/idea/2012/10/check-out-more-than-200-open-source-plugins/).

## Problems with these instructions?
[File an issue!](https://github.com/raket/idea-silverstripe/issues?direction=desc&page=1&sort=created&state=open) Feel free to suggest improvements, point out gaps, or even just ask questions.