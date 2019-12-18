# fwm-example

This is an example project for a client/server app in Clojure and ClojureScript using WebSockets and Reagent.

## Overview

This project was intended to serve as a learning experience in writing client/server apps in Clojure and ClojureScript using a [Clojure Deps](https://clojure.org/guides/deps_and_cli) style project and [figwheel-main](https://figwheel.org). It reflects the way that I often start my own projects.

The project is inspired by both the figwheel-main leiningen template [here](https://github.com/bhauman/figwheel-main-template) and the full-stack example [here](https://github.com/oakes/full-stack-clj-example). It combines aspects of both with additional items that I tend to use in my own projects.

What it produces is a simple client/server using [Reagent](https://reagent-project.github.io) to display the current time in a browser with regular, periodic visual updates. It's a foolish but educational example.

## Development Builds

Of course, you need Java and Clojure to get started. I used Java 9 and Clojure 1.10.1.

From a terminal window in the project directory, get an interactive development environment by running:

    clj -A:fig:repl

This will compile a development build, open a tab in the default browser at [`localhost:3000`](http://localhost:3000), and start a ClojureScript REPL in the terminal. Changes made to the ClojureScript portion of the project will be compiled and reloaded in real time. Changes affecting the browser display (Reagent components) will show up in the browser as well.

    clj -A:fig:dev

Similar to the above but does not open a REPL.

## Production Build

To build an uberjar for production run:

    clj -A:fig:prod

You can run the resulting jar from the project directory by entering:

    java -jar target/fwm-example.jar

Launching the uberjar will also open a tab in your default browser.

(The `prod.clj` script, which builds the uberjar, has a setting to generate an uberjar with a name that is easier to type. The setting is marked in the source if you want to change it back to the more traditional `fwm-example-0.1.0-SNAPSHOT-standalone.jar`.)

## Testing

Testing scripts are provided for both Clojure and ClojureScript unit testing.

### ClojureScript

After starting a development session, figwheel.main does automatic testing as well. Navigate your browser to [`http://localhost:9500/figwheel-extra-main/autotesting`](http://localhost:9500/figwheel-extra-main/auto-testing) to see the test results.

To do one-time ClojureScript testing, run:

    clj -A:fig:test-cljs

Test results will show up in the terminal. A testing web page will be opened, but you can just close it after the tests have completed.

### Clojure

To run Clojure unit tests just run:

    clj -A:test-clj

The results will appear in the terminal.

## Other Aliases

To delete all compilation artifacts, use:

    clj -A:clean

To check for outdated dependencies, run:

    clj -A:ancient

## What it Does

The application itself is quite useless and silly. After loading up and making the WebSocket connection, the client asks the server to send over some "user preference" settings, some of which are used to configure the layout of the page. Once received, the main (only) page is laid out.

(I actually found this a bit tricky when starting out. The request is made by the client and rendering the page is blocked until the preferences are received over the WebSocket from the server.)

Upon being asked to send preferences, the server also starts sending updates at regular, 1 second intervals. These updates consist of grabbing the current time and formatting it into a nice string, then sending it over the socket. Once the client receives the time, it modifies a Reagent atom that triggers a re-render of the page with the updated time.

Every so often, the server will also send a random a color to use for displaying the time in an update. When that is received, the client also updates a Reagent atom, triggering a render in the new color.

Finaly, the client increments a counter noting the number of updates it has received and displays the new data (again by updating a Reagent atom used to lay out the page.)

Of course, it's quite pointless to send the time from a server, but it is easy and demonstrates that the WebSocket is working and that the Reagent components are updated as expected.

## License

Copyright © 2020 David D. Clark

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
