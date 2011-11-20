(ns list.views.welcome
  (:require [list.views.common :as common]
            [noir.response :as response])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers
        list.models.lists))


(defpage listview "/lists/:id" {id :id}
  (let [lst (list-by-id (to-objectid id))]
    (common/layout
      [:h1 (list-name lst)]
      [:ul.sortable
        (for [item (list-items lst)]
          [:li.ui-state-default {:id (item-id item)} (item-name item)])]
      (form-to [:post (str "/lists/" id)]
        (text-field :name)
        (submit-button "Lägg till")))))

(defpage new-item [:post "/lists/:id"] {id :id, name :name}
  (do
    (add-item (to-objectid id) name)
    (response/redirect (url-for listview :id id))))

(defpage "/" []
  (common/layout
    [:h1 "Listor"]
    [:div#lists
      (for [l (all-lists)]
        (list
          [:h3 [:a {:href (url-for listview :id (list-id l))} (list-name l)]]
          [:div (list-description l)]))]))
