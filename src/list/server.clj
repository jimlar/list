(ns list.server
  (:require [noir.server :as server]))

(server/load-views "src/list/models/")
(server/load-views "src/list/views/")

(defn -main [port]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. port)]
    (server/start port {:mode mode
                        :ns 'list})))
