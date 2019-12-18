# Some Details

Some project details that you might find interesting are included here.

## WebSockets

My programming career goes back decades. But it was almost all embedded or desktop-based. As such, moving to web-based applications was all new to me -- very exciting. But I never used [AJAX](https://en.wikipedia.org/wiki/Ajax_%28programming%29) and found it confusing and convoluted, at least in appearance.

By comparison, [WebSockets](https://en.wikipedia.org/wiki/WebSocket) seem very easy and straightforward. I think this example program demonstrates that. It is so simple, you can write your own WebSocket handling for the client in well under 100 lines. Many (most?) servers have the capabilities built in and seem just as easy.

## Leiningen

The project does not use [Leiningen](https://leiningen.org) to specify and control the build. But it does use Leiningen to create the uberjar. As such, Leningen is specified as a depenedency for the "prod" script to create the uberjar. That is also the reason for the `project.clj` file in the source directory. Leiningen needs the `project.clj` file to run.

## The CSS

The [CSS](https://www.w3schools.com/Css/css_intro.asp) in the project is a little more complicated than it needs to be. That is because I set it up to be useful for projects as I typically set them up. My own projects usually begin with a page containing a header, sticky footer, and main area that expands to fill all remaining space (and no more.) The stuff inside each of these areas also consists of embedded [Flexbox](https://www.w3schools.com/csS/css3_flexbox.asp) (Flexible Box) components, sometimes quite deeply nested.

Getting the CSS set up so that the CSS `body` element does not extend out of the parent CSS `html` element and does not cause scroll bars to appear, can be error prone. Hence the included CSS. The settings for those two elements are critical to get the box sizing right and to set up embedded components to work correctly across browsers.

## Editorializing

After trying it for awhile, I don't find deps-based project management in any way superior to using Leiningen.

I suppose it's nice to have some sort of project management built into the base Clojue distribution. But then you need to write scripts to do a lot of the work that Leiningen does, like the `clean` and `uberjar` aliases in this example. Scattering the build configuration around into multiple files does not seem to be an improvement.

I have the same feelings about figwheel-main. Scattering build configuration into multiple files is just not a good idea in my opinion. I can't tell you how many times I thought to myself "Now, where is that piece of configuration recorded?" when learning the "new" way of doing things.

One thing that **is** better is the setup for ClojureScript unit testing that is available in figwheel-main. There's no fiddling around downloading some shiny new testing framework that will go out of fashion and support next week, along with all the neccessary JavaScript stuff to support it. Figwheel-main is **so** much better. In fact, that was the impetus that persuaded me to switch to deps and fighwheel-main in the first place.