(ns list.views.login
  (:require [oauth.client :as oauth]
            [noir.response :as response])
  (:use noir.core))

(def consumer (oauth/make-consumer "Il97ggeg5XrBL8njn4WKNgw4nQyPXXzzyf7inJ3p"
                "y3VEDAizTBJnHxG6bnFfHKh0LRURFjMrjFzTHK1D"
                "https://vauth.valtech.se/oauth/request_token"
                "https://vauth.valtech.se/oauth/access_token"
                "https://vauth.valtech.se/oauth/authorize"
                :hmac-sha1))

(pre-route "/*" {}
  )