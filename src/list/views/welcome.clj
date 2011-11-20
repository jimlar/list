(ns list.views.welcome
  (:require [list.views.common :as common])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        list.models.lists))

(defpage listview "/lists/:id" {id :id}
  (let [lst (list-by-id (to-objectid id))]
    (common/layout
      [:h1 (list-name lst)]
      [:ul.sortable
        (for [item (list-items lst)]
          [:li.ui-state-default {:id (item-id item)} (item-name item)])])))

(defpage "/" []
  (common/layout
    [:div#lists
      (for [l (all-lists)]
        (list
          [:h3 [:a {:href (url-for listview :id (list-id l))} (list-name l)]]
          [:div (list-description l)]))]))

