(ns list.views.login
  (:require [oauth.client :as oauth]
            [noir.response :as response]
            [noir.session :as session])
  (:use noir.core))


(defn get-user []
  (session/get :username))

(defn set-user! [username]
  (session/put! :username username))

(defn logged-in? []
  (not (nil? (get-user))))

(def consumer (oauth/make-consumer "Il97ggeg5XrBL8njn4WKNgw4nQyPXXzzyf7inJ3p"
                "y3VEDAizTBJnHxG6bnFfHKh0LRURFjMrjFzTHK1D"
                "https://vauth.valtech.se/oauth/request_token"
                "https://vauth.valtech.se/oauth/access_token"
                "https://vauth.valtech.se/oauth/authorize"
                :hmac-sha1))

(defn request-login []
  (let [request-token (oauth/request-token consumer "/")]
    (response/redirect (oauth/user-approval-uri consumer (:oauth_token request-token)))))
