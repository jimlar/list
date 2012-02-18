(ns list.models.lists
  (:import  [org.bson.types ObjectId])
  (:require [list.config :as config])
  (:use somnium.congomongo))


; The actual MongoDB connection
(def list-db (make-connection
               (config/value :mongodb-db)
               {:host (config/value :mongodb-host)
                :port (config/int-value :mongodb-port)}
               (mongo-options :auto-connect-retry true)))

(authenticate list-db
  (config/value :mongodb-user)
  (config/value :mongodb-password))

(defn to-objectid [str] (ObjectId. str))

(defn list-id [l] (:_id l))
(defn list-name [l] (:name l))
(defn list-description [l] (:description l))
(defn list-items [l] (sort-by (fn [x] (get x :weight 0)) (filter #(not (get % :deleted false)) (:items l))))

(defn item-id [i] (:id i))
(defn item-name [i] (:name i))

(defn all-lists []
  (with-mongo list-db
    (fetch :lists)))

(defn list-by-id [id]
  (with-mongo list-db
    (fetch-by-id :lists id)))

(defn add-list [name, description]
  (with-mongo list-db
    (insert! :lists {:name name, :description description, :items []})))

(defn add-item [id, name]
  (with-mongo list-db
    (let [list (list-by-id id)]
      (update! :lists {:_id id} {"$push" {:items {:id (ObjectId.) :name name :weight (inc (count (list-items list)))}}}))))

(defn remove-item [listid, itemid]
  (with-mongo list-db
    (update! :lists {:_id listid "items.id" itemid} {"$set" {"items.$.deleted" true}})))

(defn reorder-items [id, item-ids]
  (with-mongo list-db
    (dotimes [index (count item-ids)]
      (update! :lists {:_id id "items.id" (item-ids index)} {"$set" {"items.$.weight" (inc index)}}))))

