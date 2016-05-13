(ns example.core
  (:require [system.repl :refer [set-init! go]]
            [example.systems :refer [prod-system]]))

(defn -main
  "Start a production system, unless a system is passed as argument (as in the dev-run task)."
  [& args]
  (let [system (or (first args) #'prod-system)]
    (set-init! system)
    (go)))

