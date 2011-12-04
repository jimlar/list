(ns list.models.lists
  (:require [monger.collection :as collection])
  (:import  [org.bson.types ObjectId]))

(monger.core/connect!)
(monger.core/set-db! (monger.core/get-db "list"))

(defn to-objectid [str] (ObjectId. str))

(defn list-id [l] (get l "_id"))
(defn list-name [l] (get l "name"))
(defn list-description [l] (get l "description"))
(defn list-items [l] (sort-by (fn [x] (get x "weight" 0)) (get l "items")))

(defn item-id [i] (get i "id"))
(defn item-name [i] (get i "name"))

(defn all-lists []
  (collection/find "lists"))

(defn list-by-id [id]
  (collection/find-by-id "lists" id))

(defn add-list [name, description]
  (collection/insert "lists" {:name name, :description description, :items []}))

(defn add-item [id, name]
  (let [list (list-by-id id)]
    (collection/update "lists" {:_id id} {"$push" {:items {:id (ObjectId.) :name name :weight (inc (count (list-items list)))}}})))

(defn reorder-items [id, item-ids]
  (dotimes [index (count item-ids)]
    (collection/update "lists" {:_id id "items.id" (item-ids index)} {"$set" {"items.$.weight" (inc index)}})))

