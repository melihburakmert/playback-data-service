# Playback Data Service

Fetches recently played tracks for the user logged in.

## Running in Docker locally:

- Build the image \
`mvn jib:dockerBuild`
- Deploy the image to Docker \
`docker-compose up -d spotify-service`
- Visit `localhost:8082/playback/test`
- Login to your Spotify Account
