(ns list.models.lists
  (:import  [org.bson.types ObjectId])
  (:use somnium.congomongo))

(def list-db (make-connection "list"))
(set-connection! list-db)

(defn to-objectid [str] (ObjectId. str))

(defn list-id [l] (:_id l))
(defn list-name [l] (:name l))
(defn list-description [l] (:description l))
(defn list-items [l] (sort-by (fn [x] (get x :weight 0)) (filter #(not (get % :deleted false)) (:items l))))

(defn item-id [i] (:id i))
(defn item-name [i] (:name i))

(defn all-lists []
  (fetch :lists))

(defn list-by-id [id]
  (fetch-by-id :lists id))

(defn add-list [name, description]
  (insert! :lists {:name name, :description description, :items []}))

(defn add-item [id, name]
  (let [list (list-by-id id)]
    (update! :lists {:_id id} {"$push" {:items {:id (ObjectId.) :name name :weight (inc (count (list-items list)))}}})))

(defn remove-item [listid, itemid]
  (update! :lists {:_id listid "items.id" itemid} {"$set" {"items.$.deleted" true}}))

(defn reorder-items [id, item-ids]
  (dotimes [index (count item-ids)]
    (update! :lists {:_id id "items.id" (item-ids index)} {"$set" {"items.$.weight" (inc index)}})))

