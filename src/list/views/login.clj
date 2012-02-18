(ns list.views.login
  (:require [clojure.tools.logging :as log]
            [oauth.client :as oauth]
            [noir.response :as response]
            [noir.session :as session]
            [noir.request :as request]
            [com.twinql.clojure.http :as http]
            [list.config :as config])
  (:use noir.core))

(defrecord User [email name given-name family-name])

(defn get-user []
  (session/get :user))

(defn logged-in? []
  (not (nil? (get-user))))

(defn- set-user! [user]
  (session/put! :user user))

(def profile-url "https://vauth.valtech.se/users/me")

(def consumer (oauth/make-consumer (config/value :oauth-consumer-key)
                (config/value :oauth-consumer-secret)
                "https://vauth.valtech.se/oauth/request_token"
                "https://vauth.valtech.se/oauth/access_token"
                "https://vauth.valtech.se/oauth/authorize"
                :hmac-sha1))

(defn- parse-user [response]
  (if-not (= 200 (:code response))
    (do
      (log/warn "could not parse profile response from VAuth:" response)
      nil)
    (:content response)))

(defn- load-user-info [token secret]
  (log/info "Loading user info")
  (let [credentials (oauth/credentials consumer token secret :GET profile-url)]
    (parse-user (http/get profile-url
                  :headers {"Authorization" (oauth/authorization-header credentials)
                            "Accept"  "*/*"}
                  :as :json))))

(defn- callback-url []
  (let [r (request/ring-request)]
    (str (name (:scheme r)) "://" (get (:headers r) "host") "/")))

(defn- start-oauth-flow []
  (log/info "Starting OAuth flow, callback ul" (callback-url))
  (session/remove! :request-token)
  (let [request-token (oauth/request-token consumer (callback-url))]
    (log/info "Got request token " request-token ", redirecting")
    (session/put! :request-token request-token)
    (response/redirect (oauth/user-approval-uri consumer (:oauth_token request-token)))))

(defn- process-oauth-callback [verifier]
  (let [token (session/get :request-token)]
    (session/remove! :request-token)
    (log/info "Processing callback, token:" token " verifier:" verifier)
    (let [access-token-response (oauth/access-token consumer token verifier)
          user-data (load-user-info (:oauth_token access-token-response) (:oauth_token_secret access-token-response))]
      (set-user! (User. (:email user-data) (:name user-data) (:given-name user-data) (:family-name user-data)))
      (response/redirect "/"))))

(defn- oauth-flow-started? []
  (not (nil? (session/get :request-token))))

(pre-route "/*" {}
  (if-not (logged-in?)
    (if-not (oauth-flow-started?)
      (start-oauth-flow)
      (process-oauth-callback (get (:query-params (request/ring-request)) "oauth_verifier")))))
