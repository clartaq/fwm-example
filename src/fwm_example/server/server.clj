(ns fwm-example.server.server
  (:gen-class)
  (:require [fwm-example.server.handler :refer [all-routes]]
            [org.httpkit.server :as http-kit]))

;;; Just used to demonstrate testing.
(defn multiply [a b] (* a b))

(defonce ^{:private true} web-server_ (atom nil))

(defn stop-server! []
  (println "Stopping web server.")
  (when-let [stop-fn @web-server_]
    (println "stop-fn: " stop-fn)
    (stop-fn)))

(defn start-server! [& [args]]
  (stop-server!)
  ;; A port number to use is the only valid use of the args, so try to
  ;; convert the string argument to an integer and use it for the port.
  (let [http-port (or (and args
                           (first args)
                           (integer? (first args))
                           (Integer. (first args)))
                      3000)
        wrapped-routes all-routes
        [port-used stop-fn] (let [stop-fn (http-kit/run-server
                                            wrapped-routes
                                            {:port http-port :join? false})]
                              [(:local-port (meta stop-fn))
                               (fn [] (stop-fn :timeout 100))])
        uri (format "http://localhost:%s/" port-used)]

    (println "http-port: " http-port)
    (println "Web server is running at: " uri)
    (try
      (.browse (java.awt.Desktop/getDesktop) (java.net.URI. uri))
      (catch java.awt.HeadlessException _))

    (reset! web-server_ stop-fn)))


