(ns list.views.main
  (:require [list.views.layout :as layout]
            [list.views.login :as login]
            [clojure.string :as string]
            [noir.response :as response]
            [list.views.login :as login])
  (:use noir.core
        hiccup.core
        hiccup.page-helpers
        hiccup.form-helpers
        list.models.lists))

(defpage listdetails "/lists/:id/" {id :id}
  (let [lst (list-by-id (to-objectid id))]
    (layout/layout
      [:h1 (list-name lst)]
      [:form.add-item {:method :post :action (str "/lists/" id "/")}
        (text-field :name)
        (submit-button "Lägg till")]
      [:ol.sortable
        (for [item (list-items lst)]
          [:li.ui-state-default {:id (item-id item)}
            (item-name item)
            [:form.delete {:method :post :action (str "/lists/" id "/" (item-id item))}
              [:button {:type "submit"} "&#x2716;"]]])])))

(defpage new-item [:post "/lists/:id/"] {id :id, name :name}
  (do
    (add-item (to-objectid id) name)
    (response/redirect (url-for listdetails {:id id}))))

(defpage delete-item [:post "/lists/:listid/:itemid"] {listid :listid, itemid :itemid}
  (do
    (remove-item (to-objectid listid) (to-objectid itemid))
    (response/redirect (url-for listdetails {:id listid}))))

(defpage items [:post "/lists/:id/items/order"] {id :id, order :order}
  (do
    (reorder-items (to-objectid id) (vec (map to-objectid (string/split order #","))))
    "OK"))

(defpage new-list [:post "/"] {name :name, description :description}
  (do
    (add-list name description)
    (response/redirect "/")))

(defpage index "/" []
  (layout/layout
    [:h1 "Listor" (login/get-user)]
    [:div#lists
      (for [l (all-lists)]
        (list
          [:h3 [:a {:href (url-for listdetails {:id (list-id l)})} (list-name l)]]
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

