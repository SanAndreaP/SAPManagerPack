SAPManagerPack
==============

A Manager Pack which helps me to keep my stuff organized and doing some fancy stuff like adding custom hooks
through ASM.<br>
If you have suggestions to this, feel free to submit a Pull Request or (if you can't do a PR) contact me.<br>
<br>
<b>Install the Manager Pack for development</b><br>
* Fork this repo and download it via a git client (I recommend SourceTree)
* run <tt>gradlew setupDecompWorkspace</tt>
* setup a new workspace in your preferred IDE, here an example for IntelliJ:
  * open IntelliJ and open a new project
  * point to the build.gradle
  * leave the default settings and click "OK"
  * After the project is loaded, open the "Gradle" tab (<i>may be invisible for you, in which case click on the icon in the bottom left corner and select "Gradle" from there</i>)
  * Execute the task "genIntelliJRuns" via double-click on it
  * Reload the project
* after that, add -Dfml.coreMods.load=de.sanandrew.core.manpack.init.ManPackLoadingPlugin to the VM options
* You can now code and stuff :D

<b>ALWAYS USE THE LATEST FORGE VERSION!</b>
