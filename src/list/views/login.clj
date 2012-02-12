(ns list.views.login
  (:require [clojure.tools.logging :as log]
            [oauth.client :as oauth]
            [noir.response :as response]
            [noir.session :as session]
            [noir.request :as request]
            [com.twinql.clojure.http :as http])
  (:use noir.core))

(defn get-user []
  (session/get :username))

(defn- set-user! [username]
  (session/put! :username username))

(defn- logged-in? []
  (not (nil? (get-user))))

(def consumer (oauth/make-consumer "Il97ggeg5XrBL8njn4WKNgw4nQyPXXzzyf7inJ3p"
                "y3VEDAizTBJnHxG6bnFfHKh0LRURFjMrjFzTHK1D"
                "https://vauth.valtech.se/oauth/request_token"
                "https://vauth.valtech.se/oauth/access_token"
                "https://vauth.valtech.se/oauth/authorize"
                :hmac-sha1))

(def profile-url "https://vauth.valtech.se/users/me")

(defn- load-user-info [token secret]
  (log/info "Loading user info")
  (let [credentials (oauth/credentials consumer token secret :GET profile-url)]
    (http/get profile-url
              :headers {"Authorization" (oauth/authorization-header credentials)
                        "Accept"  "*/*"})))

(defn- start-oauth-flow []
  (log/info "Starting OAuth flow")
  (session/remove! :request-token)
  (let [request-token (oauth/request-token consumer "http://localhost:8080/")]
    (log/info "Got request token " request-token ", redirecting")
    (session/put! :request-token request-token)
    (response/redirect (oauth/user-approval-uri consumer (:oauth_token request-token)))))

(defn- process-oauth-callback [verifier]
  (let [token (session/get :request-token)]
    (session/remove! :request-token)
    (log/info "Processing callback, token:" token " verifier:" verifier)
    (let [access-token-response (oauth/access-token consumer token verifier)]
      (set-user! (load-user-info (:oauth_token access-token-response) (:oauth_token_secret access-token-response))))))

(defn- oauth-flow-started? []
  (not (nil? (session/get :request-token))))

(pre-route "/" {}
  (if-not (logged-in?)
    (if-not (oauth-flow-started?)
      (start-oauth-flow)
      (process-oauth-callback (get (:query-params (request/ring-request)) "oauth_verifier")))))
