(ns list.models.lists
  (:require [monger.collection :as collection]))


(defn all []
  (collection/find "list-items"))