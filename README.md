# [SilverStripe](http://silverstripe.org/) template plugin for Jetbrains IDEs

IDEA-SilverStripe adds support for [SilverStripe](http://silverstripe.org/) templates to IDEs based on the IntelliJ IDEA platform (IntelliJ IDEA, IDEA Community Edition, RubyMine, PhpStorm, WebStorm, PyCharm, AppCode).
The primary focus is PHPStorm since SilverStripe is a PHP framework but the plugin should work in all the versions.

## Installing

* To install the latest release (and get automatic updates), install this plugin using your IDE's plugin manager:
  * In Settings->Plugins, choose "Browse repositories". Search for "SilverStripe" or find "SilverStripe template language support" on the list, right-click, and select "Download and Install"
* To install the most bleeding edge version, download or clone this repo and install this plugin using your IDE's plugin manager:
  * In Settings->Plugins, choose "Install plugin from disk...".  Point it to the SilverStripe.jar file and restart the IDE.
  * If you have already added `*.ss` files to your file types manually you need to remove it for the plugin to work. Go to `Settings->File Types` and remove `*.ss` from the file type you added it to (most likely html).
  * If you want to update either do a git pull if you've cloned the repo or download the repo again and simply reinstall the plugin.

## Features
* Basic syntax error highlighting with wanrings for mismatching blocks and unclosed blocks.
* Code folding for if, with, loop and control blocks.
* Full highlighting of the basic SilverStripe template language.
* By default, files matching `*.ss` are handled by this plugin.  Configure other file patterns in `Settings->File Types`.

## Future directions
* Make the parser better. It parses the basic syntax but there's still alot to do.
  * Handle operators in var statements.
  * The translation tag.
  * else_if and else needs to be handled properly.
* Code completion.
* Custom method and variable resolutions. IE for example resolving $ThemeDir to the actual dir in the project. Figure out variable scope etc.
* Everything else.

## Contributing
Contributions welcome!

This is my first outing in Java programming so any help you can give me is greatly appreciated.
If you make a feature request please keep in mind that I will most likely not know how to implement the feature and just might need your help.
I'm well aware that I'm not the best candidate for this project knowledge wise but hey, I have the spare time for it!

There's a variety of ways you can help this project out:

* Contributing without coding
    * [File issues](https://github.com/raket/idea-silverstripe/issues/new) for bugs, suggestions and requests.  This is a great and easy way to help out.
* Contributing code
    * The awesome [developer setup instructions](https://github.com/dmarcotte/idea-handlebars/blob/master/developer_environment.md) provided by [@dmarcotte](https://github.com/dmarcotte) should get you up and running in no time.
    * After the initial setup clone or fork this repo and create an IntelliJ IDEA plugin project. When the project is created you need to mark the gen package and the resources package as source roots for the project to compile properly.
    * Look at [contrib-welcome issues](https://github.com/raket/idea-silverstripe/issues?direction=desc&labels=contrib-welcome&page=1&sort=created&state=open) for ideas, or [submit an idea of your own](https://github.com/raket/idea-silverstripe/issues/new).

## Acknowledgements
A big thank you goes out to [Daniel Marcotte](https://github.com/dmarcotte) and his [Handlebars plugin](https://github.com/dmarcotte/idea-handlebars).
Without his help this plugin would never have seen the light of day and he is totally awesome. Thank you [@dmarcotte](https://github.com/dmarcotte)!
Head on over and fork his modules and give him some love. This will hopefully be enough to make him forgive me for stealing his readme as a basis for my own.
I'm sorry but it was just so good!

Thank you also to @juzna for his [Latte in IntelliJ IDEA / PhpStorm](https://github.com/juzna/intellij-latte) with its simpler parser implementation which gave me enough confidence to roll my own.
Last but not least a big thank you to the [SilverStripe team](https://github.com/silverstripe) and its community for making an awesome framework and CMS.
Hopefully this plugin will help SilverStripe developers with their template development.