# Playback Data Service

A microservice fetching recently played tracks for the user logged in.

## Tech Stack

- Java + Spring Boot
- Google Cloud PubSub
- Spotify Web API
- Docker
- Maven
- Modulith Architecture
- Event Driven Architecture

## Endpoints

- GET `/playback/recently-played` - Fetch recently played tracks
- GET `/playback/recently-played/publish` - Publish recently played tracks to PubSub

## Running in Docker locally:

- Build the image \
`mvn jib:dockerBuild`
- Deploy the image to Docker \
`docker-compose up -d spotify-service`
- Visit `/playback/recently-played`
- Login to your Spotify Account

### TODO:

- [ ] Add tests
- [ ] Add CI/CD
- [ ] Deploy on Kubernetes