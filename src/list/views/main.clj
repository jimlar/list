(ns list.views.main
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
        [:fieldset
          [:legend "Ny"]
          [:dl
            [:dt "Namn"]
            [:dd (text-field :name)]
            [:dt]
            [:dd (submit-button "Lägg till")]]]))))

            (defpage new-item [:post "/lists/:id"] {id :id, name :name}
  (do
    (add-item (to-objectid id) name)
    (response/redirect (url-for listview :id id))))

(defpage new-list [:post "/"] {name :name, description :description}
  (do
    (add-list name description)
    (response/redirect "/")))

(defpage index "/" []
  (common/layout
    [:h1 "Listor"]
    [:div#lists
      (for [l (all-lists)]
        (list
          [:h3 [:a {:href (url-for listview :id (list-id l))} (list-name l)]]
          [:div (list-description l)]))]
    (form-to [:post (url-for new-list)]
      [:fieldset
        [:legend "Ny lista"]
        [:dl
          [:dt "Namn"]
          [:dd (text-field :name)]
          [:dt "Beskrivning"]
          [:dd (text-area :description)]
          [:dt]
          [:dd (submit-button "Ny")]]])))

