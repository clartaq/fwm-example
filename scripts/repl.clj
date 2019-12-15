(require
  '[figwheel.main.api :as fw]
  '[fwm-example.server.main :refer [dev-main]])

(fw/start {:mode :serve} "dev")
(dev-main)
(figwheel.main.api/cljs-repl "dev")
