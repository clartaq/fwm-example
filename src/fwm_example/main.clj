(ns fwm-example.main
  (:require [fwm-example.server :as server])
  (:gen-class))

(defn start-app!
  "Initialize and start the major program components. REPL convenience function."
  [& [port]]
  (println "Starting fwm-example.")
  (server/start-server! port))

(defn stop-app!
  "Shut down the application. REPL convenience function."
  []
  (println "Stopping fwm-example.")
  (server/stop-server!))

(defn dev-main [& [port]]
  (println "Starting dev-main.")
  (start-app! port))

(defn -main
  "Program entry point."
  [& port]
  (println "Starting main.")
  (start-app! port))
