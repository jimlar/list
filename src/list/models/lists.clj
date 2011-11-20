(ns list.models.lists
  (:require [monger.collection :as collection]))

(monger.core/connect!)
(monger.core/set-db! (monger.core/get-db "list"))

(defn all []
  (collection/find "lists"))