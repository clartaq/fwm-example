(require
  '[figwheel.main :as figwheel]
  '[fwm-example.main :refer [dev-main]])

(dev-main)
(figwheel/-main "--build" "dev")
