(ns list.models.lists
  (:require [monger.collection :as collection])
  (:import  [org.bson.types ObjectId]))

(monger.core/connect!)
(monger.core/set-db! (monger.core/get-db "list"))

(defn to-objectid [str] (ObjectId. str))

(defn list-id [l] (get l "_id"))
(defn list-name [l] (get l "name"))
(defn list-description [l] (get l "description"))
(defn list-items [l] (get l "items"))

(defn item-id [i] (get i "id"))
(defn item-name [i] (get i "name"))

(defn all-lists []
  (collection/find "lists"))

(defn list-by-id [id]
  (collection/find-by-id "lists" id))