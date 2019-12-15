(ns fwm-example.server.main
  (:require [fwm-example.server.server :as server])
  (:gen-class))

(defn start-app!
  "Initialize and start the major program components. REPL convenience function."
  [& [args]]
  (println "Starting fwm-example.")
  (server/start-server! args))

(defn stop-app!
  "Shut down the application. REPL convenience function."
  []
  (println "Stopping fwm-example.")
  (server/stop-server!))

(defn dev-main [& [args]]
  (println "Starting dev-main.")
  (start-app! args))

(defn -main
  "Program entry point."
  [& args]
  (println "Starting main.")
  (start-app! args))
