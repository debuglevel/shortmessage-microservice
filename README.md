<!--- some badges to display on the GitHub page -->

![Travis (.org)](https://img.shields.io/travis/debuglevel/shortmessage-microservice?label=Travis%20build)
![Gitlab pipeline status](https://img.shields.io/gitlab/pipeline/debuglevel/shortmessage-microservice?label=GitLab%20build)
![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/debuglevel/shortmessage-microservice?sort=semver)
![GitHub](https://img.shields.io/github/license/debuglevel/shortmessage-microservice)

# Short Message Microservice

This microservice sends short messages via a various SMS gateways (Twilio, esendex, SMS77).


# HTTP API
## Send message

A short message can be sent via
```shell script
$ curl -X POST 'http://localhost:8080/messages/' \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Basic U0VDUkVUX1VTRVJOQU1FOlNFQ1JFVF9QQVNTV09SRA==' \
  -d '{"recipientNumber": "+49123456789", "body": "Greetings from an ancient messaging world" }'
```

Depending on the configured SMS gateway, the request may return more or less information (Twilio in this case):
```json
{
  "id": "SM7b850b9f483a4fd89e3dDEADCAFEBEEF",
  "body": "Greetings from an ancient messaging world",
  "senderNumber": "+1412345678",
  "recipientNumber": "+49123456789",
  "status": "queued",
  "price": "null USD"
}
```

# Configuration
There is a `application.yml` included in the jar file. Its content can be modified and saved as a separate `application.yml` on the level of the jar file. Configuration can also be applied via the other supported ways of Micronaut (see <https://docs.micronaut.io/latest/guide/index.html#config>). For Docker, the configuration via environment variables is the most interesting one (see `docker-compose.yml`).

See `application.yml` to configure your SMS gateway. 