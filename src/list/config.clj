(ns list.config
  (:require [clojure.java.io :as javaio]
            [clojure.tools.logging :as log]))

(defn- load-props [file-name]
  (with-open [^java.io.Reader reader (javaio/reader file-name)]
    (let [props (java.util.Properties.)]
      (.load props reader)
      (into {} (for [[k v] props] [(keyword k) v])))))

; Local properties is an optional config file
(def local-properties (load-props "local.properties"))

; Search for config in local.properties or system env
(defn value [key]
  (let [value (get local-properties key (get (System/getenv) (name key)))]
    (log/info "returning" value "for" key "local properties:" local-properties )
    value))
