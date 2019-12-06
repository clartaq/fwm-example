# fwm-example

This is an example project for a client/server app in Clojure and ClojureScript.

## Overview

This project was intended to serve as a learning experience in writing client/server apps in Clojure and ClojureScript using a [Clojure Deps](https://clojure.org/guides/deps_and_cli) style project and [figwheel-main](https://figwheel.org). It reflects the way that I often start my own projects.

The project is inspired by both the figwheel-main leiningen template [here](https://github.com/bhauman/figwheel-main-template) and the full-stack example [here](https://github.com/oakes/full-stack-clj-example). It combines aspects of both with additional items that I tend to use in my own projects.

What it produces is a simple client/server using [Reagent](https://reagent-project.github.io) for display in a browser.

## Development

To get an interactive development environment run:

    clojure -A:fig:dev

This will auto compile and open a browser to `localhost:9500`. Changes made to the ClojureScript portion of the project will be compile and reloaded in real time. Changes affecting the browser display (Reagent components) will show up in the browser as well.

The Clojure-based server will also be running and can be viewed by navigating to `localhost:3000`. The two views are usually identical.

## Production Build

To build an uberjar for production use, run:

    clj -A:fig:prod

You can run the resulting jar from the project directory by entering:

    java -jar target/fwm-example-0.1.0-SNAPSHOT-standalone.jar

Then open a browser to `localhost:3000` to interact with the running application.

(The `prod.clj` script includes a tip on how to change the name of the standalone uberjar to something less tedious to type.)

## Testing

Testing scripts are provided for both Clojure and ClojureScript unit testing.

### ClojureScript

After starting a development session, figwheel.main does automatic testing as well. Navigate your browser to `localhost:9500/figwheel-extra-main/autotesting` to see the test results.

To do one-time ClojureScript testing, run:

    clj -A:fig:test-cljs

Test results will show up in the terminal. A testing web page will be opened, but you can just close it after the tests have completed.

### Clojure

Just run:

    clj -A:test-clj

To run Clojure unit tests. The results will appear in the terminal.

## Other Aliases

To delete all compilation artifacts, use:

    clj -A:clean

To check for outdated dependencies, run:

    clj -A:ancient

## Running REPLs

## License

Copyright © 2020 David D. Clark

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
