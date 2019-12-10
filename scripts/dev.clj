(require
  '[figwheel.main.api :as fw]
  '[fwm-example.main :refer [dev-main]])

(fw/start {:mode :serve} "dev")
(dev-main)
