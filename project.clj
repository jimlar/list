(defproject list "0.1.0-SNAPSHOT"
            :description "A little todo list app"
            :dependencies [[org.clojure/clojure "1.3.0"]
                           [org.clojure/tools.logging "0.2.3"]
                           [noir "1.2.2"]
                           [congomongo "0.1.7"]
                           [org.clojars.adamwynne/clj-oauth "1.2.12"]]
            :jvm-opts ["-Djavax.net.ssl.trustStore=truststore.jks" "-Djavax.net.ssl.trustStorePassword=s3cr3t" "-Djavax.net.debug=all"]
            :main list.server)

